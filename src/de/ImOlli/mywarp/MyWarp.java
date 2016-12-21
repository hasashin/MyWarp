package de.ImOlli.mywarp;

import de.ImOlli.commands.*;
import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class MyWarp extends JavaPlugin {

    private static Plugin plugin;
    private static String prefix;
    private static Boolean onlyOp;
    private static Boolean playSoundOnTeleport;
    private static Boolean playParticleOnTeleport;
    private static ArrayList<String> warpnames;

    @Override
    public void onEnable() {
        plugin = this;

        checkConfig();
        loadConfig();
        MessageManager.init();
        MessageManager.loadConfig();
        WarpManager.init();
        registerCommands();
    }

    @SuppressWarnings("unchecked")
    public static void loadConfig() {
        onlyOp = plugin.getConfig().getBoolean("OnlyOp");
        playSoundOnTeleport = plugin.getConfig().getBoolean("PlaySoundOnTeleport");
        playParticleOnTeleport = plugin.getConfig().getBoolean("PlayParticleOnTeleport");

        if (plugin.getConfig().contains("warpnames")) {
            warpnames = (ArrayList<String>) plugin.getConfig().getList("warpnames");
        }
    }

    public static void checkConfig() {

        plugin.getConfig().options().copyDefaults(true);
        plugin.getConfig().options().header("Configuration of MyWarp");
        plugin.saveConfig();

        plugin.getConfig().addDefault("OnlyOp", false);
        plugin.getConfig().addDefault("PlaySoundOnTeleport", true);
        plugin.getConfig().addDefault("PlayParticleOnTeleport", true);
        plugin.saveConfig();

    }

    private void registerCommands() {

        Bukkit.getPluginCommand("warp").setExecutor(new COMMAND_warp());
        Bukkit.getPluginCommand("setwarp").setExecutor(new COMMAND_setwarp());
        Bukkit.getPluginCommand("warps").setExecutor(new COMMAND_warps());
        Bukkit.getPluginCommand("delwarp").setExecutor(new COMMAND_delwarp());
        Bukkit.getPluginCommand("mywarp").setExecutor(new COMMAND_mywarp());

    }

    public static void setPrefix(String newprefix) {
        prefix = newprefix;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static Boolean isOnlyOp() {
        return onlyOp;
    }

    public static ArrayList<String> getWarpnames() {
        return warpnames;
    }

    public static Boolean getPlaySoundOnTeleport() {
        return playSoundOnTeleport;
    }

    public static Boolean getPlayParticleOnTeleport() {
        return playParticleOnTeleport;
    }
}
