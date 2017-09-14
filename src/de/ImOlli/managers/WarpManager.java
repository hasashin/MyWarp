package de.ImOlli.managers;

import de.ImOlli.objects.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
                System.out.println("[MyWarp] An error occurred while creating 'warps.yml'!");
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        config.options().copyDefaults(true);
        config.options().header("Warps of MyWarp \n\n" +
                "Please note that you can only edit the config when the server is off.");

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String name : config.getValues(false).keySet()) {

            String creator = config.getString(name.toLowerCase() + ".creator");
            String warpname = config.getString(name.toLowerCase() + ".name");
            String world = config.getString(name.toLowerCase() + ".world");
            Double x = config.getDouble(name.toLowerCase() + ".x");
            Double y = config.getDouble(name.toLowerCase() + ".y");
            Double z = config.getDouble(name.toLowerCase() + ".z");

            warps.put(name.toLowerCase(), new Warp(warpname, new Location(Bukkit.getWorld(world), x, y, z), creator));
        }
    }

    public static Boolean existWarp(String name) {
        return (config.contains(name.toLowerCase()) && warps.containsKey(name.toLowerCase()));
    }

    public static Boolean addWarp(String name, Player creator, Location loc) {
        if (!existWarp(name)) {
            config.set(name.toLowerCase() + ".name", name);
            config.set(name.toLowerCase() + ".creator", creator.getName());
            config.set(name.toLowerCase() + ".world", loc.getWorld().getName());
            config.set(name.toLowerCase() + ".x", loc.getX());
            config.set(name.toLowerCase() + ".y", loc.getY());
            config.set(name.toLowerCase() + ".z", loc.getZ());

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            warps.put(name.toLowerCase(), new Warp(name, loc, creator.getName()));
            return true;
        }
        return false;
    }

    public static Boolean removeWarp(String name) {
        if (existWarp(name.toLowerCase())) {
            config.set(name.toLowerCase(), null);

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            warps.remove(name.toLowerCase());
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

    public static ArrayList<Warp> getWarpList() {

        ArrayList<Warp> warplist = new ArrayList<>();

        for (Warp warp : getWarps().values()) {
            warplist.add(warp);
        }

        return warplist;
    }
}
