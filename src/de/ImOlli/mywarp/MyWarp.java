package de.ImOlli.mywarp;

import de.ImOlli.commands.*;
import de.ImOlli.listeners.PlayerInteractListener;
import de.ImOlli.listeners.PlayerTabCompleteListener;
import de.ImOlli.listeners.SignChangeListener;
import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.objects.WarpGui;
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
    private static Boolean warpSigns;
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
        registerListeners();
    }

    @SuppressWarnings("unchecked")
    public static void loadConfig() {
        onlyOp = plugin.getConfig().getBoolean("OnlyOp");
        playSoundOnTeleport = plugin.getConfig().getBoolean("PlaySoundOnTeleport");
        playParticleOnTeleport = plugin.getConfig().getBoolean("PlayParticleOnTeleport");
        warpSigns = plugin.getConfig().getBoolean("WarpSigns");

        if (plugin.getConfig().contains("warpnames")) {
            warpnames = (ArrayList<String>) plugin.getConfig().getList("warpnames");
        }
    }

    public static void checkConfig() {

        plugin.getConfig().options().copyDefaults(true);
        plugin.getConfig().options().header("Configuration of MyWarp \n\n" +
                "Please note that editing the configurations while the server is running is not recommended.");

        plugin.getConfig().addDefault("OnlyOp", false);
        plugin.getConfig().addDefault("PlaySoundOnTeleport", true);
        plugin.getConfig().addDefault("PlayParticleOnTeleport", true);
        plugin.getConfig().addDefault("WarpSigns", true);
        plugin.saveConfig();

    }

    private void registerCommands() {

        Bukkit.getPluginCommand("warp").setExecutor(new COMMAND_warp());
        Bukkit.getPluginCommand("setwarp").setExecutor(new COMMAND_setwarp());
        Bukkit.getPluginCommand("warps").setExecutor(new COMMAND_warps());
        Bukkit.getPluginCommand("delwarp").setExecutor(new COMMAND_delwarp());
        Bukkit.getPluginCommand("mywarp").setExecutor(new COMMAND_mywarp());
        Bukkit.getPluginCommand("warpgui").setExecutor(new COMMAND_warpgui());

    }

    private void registerListeners() {

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new SignChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTabCompleteListener(), this);
        Bukkit.getPluginManager().registerEvents(new WarpGui(), this);

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

    public static Boolean getWarpSigns() {
        return warpSigns;
    }
}
