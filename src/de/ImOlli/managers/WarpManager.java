package de.ImOlli.managers;

import de.ImOlli.mywarp.MyWarp;
import de.ImOlli.objects.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WarpManager {

    private static HashMap<String, Warp> warps;
    private static File file;
    private static YamlConfiguration config;

    public static void init() {
        warps = new HashMap<>();

        file = new File("plugins//MyWarp//warps.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[MyWarp] Fehler beim erstellen der Datei 'warps.yml'!");
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        config.options().copyDefaults(true);
        config.options().header("Warps of MyWarp");

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (MyWarp.getWarpnames() != null && !MyWarp.getWarpnames().isEmpty()) {
            for (String name : MyWarp.getWarpnames()) {

                String creator = config.getString(name + ".creator");
                String world = config.getString(name + ".world");
                Double x = config.getDouble(name + ".x");
                Double y = config.getDouble(name + ".y");
                Double z = config.getDouble(name + ".z");

                warps.put(name, new Warp(name, new Location(Bukkit.getWorld(world), x, y, z), creator));
            }
        }

    }

    public static Boolean existWarp(String name) {
        return (config.contains(name) && warps.containsKey(name));
    }

    public static Boolean addWarp(String name, Player creator, Location loc) {
        if (!existWarp(name)) {
            config.set(name + ".name", name);
            config.set(name + ".creator", creator.getName());
            config.set(name + ".world", loc.getWorld().getName());
            config.set(name + ".x", loc.getX());
            config.set(name + ".y", loc.getY());
            config.set(name + ".z", loc.getZ());

            Plugin plugin = MyWarp.getPlugin(MyWarp.class);

            if (plugin.getConfig().contains("warpnames")) {
                @SuppressWarnings("unchecked")
                ArrayList<String> list = (ArrayList<String>) plugin.getConfig().getList("warpnames");
                list.add(name);
                plugin.getConfig().set("warpnames", list);
            } else {
                ArrayList<String> list = new ArrayList<>();
                list.add(name);
                plugin.getConfig().set("warpnames", list);
            }

            plugin.saveConfig();

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            warps.put(name, new Warp(name, loc, creator.getName()));
            return true;
        }
        return false;
    }

    public static Boolean removeWarp(String name) {
        if (existWarp(name)) {
            config.set(name, null);

            Plugin plugin = MyWarp.getPlugin(MyWarp.class);

            if (plugin.getConfig().contains("warpnames")) {
                @SuppressWarnings("unchecked")
                ArrayList<String> list = (ArrayList<String>) plugin.getConfig().getList("warpnames");
                list.remove(name);
                plugin.getConfig().set("warpnames", list);
            } else {
                plugin.getConfig().set("warpnames", null);
            }

            plugin.saveConfig();

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            warps.remove(name);
            return true;
        }
        return false;
    }


    public static Warp getWarp(String name) {
        return (existWarp(name)) ? warps.get(name) : null;
    }

    public static HashMap<String, Warp> getWarps() {
        return warps;
    }
}
