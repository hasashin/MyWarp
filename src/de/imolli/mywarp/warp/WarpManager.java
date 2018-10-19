package de.imolli.mywarp.warp;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.utils.MathUtils;
import de.imolli.mywarp.utils.SQLHandle;
import de.imolli.mywarp.warp.warpflags.WarpFlag;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class WarpManager {

    private static HashMap<String, Warp> warps;
    private static HashMap<String, LoadError> failedWarps;

    public static void init() {
        warps = new HashMap<>();
        failedWarps = new HashMap<>();

        checkForOldWarpSystem();

        try {
            loadWarps();
        } catch (SQLException e) {
            e.printStackTrace();
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while loading warps from the database...");
        }

    }

    public static void notifyPlayer(Player p) {

        if (p.isOp() || p.hasPermission("MyWarp.notify.warpfix")) {
            if (!failedWarps.isEmpty()) {

                Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(), () -> {

                    TextComponent comp = new TextComponent(MyWarp.getPrefix() + "§7There were §e" + failedWarps.size() + " §7errors during loading the warps! §7For more information ");
                    TextComponent component = new TextComponent("§eClick here!");

                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eClick to open the WarpFix GUI")));
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mywarp fix"));
                    comp.addExtra(component);

                    p.sendMessage(" ");
                    p.spigot().sendMessage(comp);

                }, 20);
            }
        }
    }

    private static void checkForOldWarpSystem() {

        File file = new File("plugins//Mywarp//warps.yml");

        if (file.exists()) {

            MyWarp.getPlugin().getLogger().log(Level.INFO, "Found old MyWarp warp file. Starting importing of old warppoints...");

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            Integer loaded = 0;

            for (String key : config.getKeys(false)) {

                if (SQLHandle.contains("warps", "name", key)) {
                    MyWarp.getPlugin().getLogger().log(Level.WARNING, "Failed to import warppoint '" + key + "' failed! A warppoint with that id already exists!");

                } else {
                    //MyWarp.getPlugin().getLogger().log(Level.INFO, "    Importing warppoint with id '" + key + "'");

                    if (Bukkit.getWorld(config.getString(key + ".world")) == null) {
                        MyWarp.getPlugin().getLogger().log(Level.SEVERE, "Failed to import warppoint '" + key + "'! World '" + config.getString(key + ".world") + "' not found!");
                    } else {
                        Location location = new Location(Bukkit.getWorld(config.getString(key + ".world")), config.getDouble(key + ".x"), config.getDouble(key + ".y"), config.getDouble(key + ".z"));

                        addWarp(config.getString(key + ".name"), config.getString(key + ".creator"), location, null);
                        loaded++;
                    }
                }
            }

            MyWarp.getPlugin().getLogger().log(Level.INFO, "Deleting old warppoint system file...");
            config = null;

            if (!file.delete())
                MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while deleting the old warppoint system file!");

            if (loaded == 0) {
                MyWarp.getPlugin().getLogger().log(Level.WARNING, "Oh no. No warppoints where imported. :(");
            } else {
                MyWarp.getPlugin().getLogger().log(Level.INFO, "Successfully imported " + loaded + " warppoints from the old system.");
            }
        }
    }

    private static void loadWarps() throws SQLException {

        ResultSet rs = SQLHandle.query("SELECT * FROM `warps`");

        while (rs.next()) {

            String name = rs.getString("name");
            String displayname = rs.getString("displayname");
            String creator = rs.getString("creator");
            String[] locationRaw = rs.getString("location").split(":");
            String flagsRaw = rs.getString("flags");
            ArrayList<WarpFlag> flags = new ArrayList<>();

            if (!flagsRaw.equalsIgnoreCase("-")) {
                if (flagsRaw.contains(":")) {
                    String[] flagValues = flagsRaw.split(":");

                    for (String s : flagValues) {
                        try {
                            flags.add(WarpFlag.valueOf(s));
                        } catch (Exception e) {
                            e.printStackTrace();
                            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while loading warpflag '" + s + "' of warp '" + name + "'!");
                        }
                    }
                } else {
                    flags.add(WarpFlag.valueOf(flagsRaw));
                }
            }

            if (locationRaw.length == 4) {

                World world = Bukkit.getWorld(locationRaw[0]);

                try {
                    Double x = Double.parseDouble(locationRaw[1]);
                    Double y = Double.parseDouble(locationRaw[2]);
                    Double z = Double.parseDouble(locationRaw[3]);

                    if (world != null) {
                        warps.put(name, new Warp(displayname, new Location(world, x, y, z), creator, flags));
                    } else {
                        MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while loading warp '" + name + "'. World '" + locationRaw[0] + "' does not exist!");
                        failedWarps.put(name, LoadError.WorldDoNotExist);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while loading warp '" + name + "'. Could not eject location data!");
                    failedWarps.put(name, LoadError.LocationIsWrong);
                }
            } else {
                MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while loading warp '" + name + "'. Could not eject location data!");
                failedWarps.put(name, LoadError.LocationIsWrong);
            }
        }
    }


    public static Boolean existWarp(String name) {
        return (SQLHandle.contains("warps", "name", name.toLowerCase()) && warps.containsKey(name.toLowerCase()));
    }

    public static Boolean addWarp(String name, String creator, Location loc, ArrayList<WarpFlag> flags) {

        if (!existWarp(name)) {

            String location = loc.getWorld().getName().toLowerCase();
            location = location + ":" + MathUtils.round(loc.getX(), 1) + ":" + MathUtils.round(loc.getY(), 1) + ":" + MathUtils.round(loc.getZ(), 1);

            SQLHandle.update("INSERT INTO `warps` (`name`, `displayname`, `location`, `creator`, `flags`) VALUES ('" + name.toLowerCase() + "', '" + name + "', '" + location + "', '" + creator + "', '-')");

            warps.put(name.toLowerCase(), new Warp(name, loc, creator, flags));
            return true;
        }
        return false;
    }

    public static Boolean removeWarp(String name) {
        if (existWarp(name)) {

            SQLHandle.update("DELETE FROM `warps` WHERE `name` = '" + name.toLowerCase() + "'");

            warps.remove(name.toLowerCase());
            return true;
        }
        return false;
    }

    public static void removeFailedWarp(String key) {
        if (failedWarps.containsKey(key)) failedWarps.remove(key);
    }

    public static void updateWarp(String key, String name, Warp warp) {
        if (warps.containsKey(key)) {
            warps.remove(key);
            warps.put(name, warp);
        } else {
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while updating warp name. Warp does not exist!");
        }
    }

    public static void updateWarpFlags(Warp warp) {

        if (warp != null && existWarp(warp.getName())) {

            StringBuilder flags = new StringBuilder();
            if (!warp.getFlags().isEmpty()) {
                if (warp.getFlags().size() > 1) {
                    for (WarpFlag flag : warp.getFlags()) {
                        if (flags.toString().equals("")) {
                            flags = new StringBuilder(flag.getName());
                        } else {
                            flags.append(":").append(flag.getName());
                        }
                    }
                } else {
                    flags = new StringBuilder(warp.getFlags().get(0).getName());
                }
            } else {
                flags = new StringBuilder("-");
            }

            SQLHandle.update("UPDATE `warps` SET `flags` = '" + flags.toString() + "' WHERE `name` = '" + warp.getName() + "';");

        } else {
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while updating warpflags. Warp does not exist.");
        }

    }

    public static boolean isCreator(Warp warp, Player p) {
        return warp.getCreator().equalsIgnoreCase(p.getName());
    }

    public static boolean isPermittedToWarp(Player p) {
        return p.isOp() || p.hasPermission("MyWarp.warpflag.onlypermittedcanwarp.use");
    }

    public static Warp getWarp(String name) {
        return (existWarp(name)) ? warps.get(name) : null;
    }

    public static HashMap<String, Warp> getWarps() {
        return warps;
    }

    public static ArrayList<Warp> getFilteredWarps() {

        ArrayList<Warp> filteredwarps = new ArrayList<>();

        for (Warp warp : warps.values()) {
            if (!warp.getFlags().contains(WarpFlag.GUIINVISIBLE)) {
                filteredwarps.add(warp);
            }
        }

        return filteredwarps;
    }

    public static ArrayList<Warp> getWarpList() {
        return new ArrayList<>(getWarps().values());
    }

    public static Integer getCurrentWarpCount(Player p) {
        Integer warps = 0;

        for (Warp warp : getWarpList()) {
            if (warp.getCreator().equalsIgnoreCase(p.getName())) {
                warps++;
            }
        }

        return warps;
    }

    public static HashMap<String, LoadError> getFailedWarps() {
        return failedWarps;
    }

}
