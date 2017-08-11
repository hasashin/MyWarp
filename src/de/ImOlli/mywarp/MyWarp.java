package de.ImOlli.mywarp;

import de.ImOlli.commands.*;
import de.ImOlli.listeners.*;
import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.PlayerManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.objects.WarpGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public class MyWarp extends JavaPlugin {

    private static Plugin plugin;
    private static String prefix;
    private static Boolean onlyOp;
    private static Boolean playSoundOnTeleport;
    private static Boolean playParticleOnTeleport;
    private static Boolean warpSigns;
    private static Boolean cancelTeleportOnMove;
    private static Integer cooldown;
    private static Integer teleportDelay;
    private static ArrayList<String> warpnames;

    @Override
    public void onEnable() {
        plugin = this;

        checkConfig();
        loadConfig();
        MessageManager.init();
        MessageManager.loadConfig();
        WarpManager.init();
        PlayerManager.init();
        registerCommands();
        registerListeners();
        loadPlayers();

    }

    public static void reload() {

        plugin.getLogger().log(Level.INFO, "Reloading MyWarp...");

        prefix = null;
        onlyOp = null;
        playSoundOnTeleport = null;
        playParticleOnTeleport = null;
        warpSigns = null;
        cancelTeleportOnMove = null;
        cooldown = null;
        teleportDelay = null;

        plugin.reloadConfig();
        checkConfig();
        loadConfig();
        MessageManager.init();
        MessageManager.loadConfig();
        WarpManager.init();
        PlayerManager.init();
        loadPlayers();

    }

    private static void loadPlayers() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerManager.registerPlayer(p);
        }

    }

    @SuppressWarnings("unchecked")
    public static void loadConfig() {
        onlyOp = plugin.getConfig().getBoolean("OnlyOp");
        playSoundOnTeleport = plugin.getConfig().getBoolean("PlaySoundOnTeleport");
        playParticleOnTeleport = plugin.getConfig().getBoolean("PlayParticleOnTeleport");
        warpSigns = plugin.getConfig().getBoolean("WarpSigns");
        cancelTeleportOnMove = plugin.getConfig().getBoolean("CancelTeleportOnMove");
        cooldown = plugin.getConfig().getInt("WarpCooldown");
        teleportDelay = plugin.getConfig().getInt("TeleportDelay");

        if (cooldown <= 0) {
            cooldown = 0;
        }

        if (teleportDelay <= 0) {
            teleportDelay = 0;
            cancelTeleportOnMove = false;
        }

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
        plugin.getConfig().addDefault("CancelTeleportOnMove", false);
        plugin.getConfig().addDefault("WarpCooldown", 5);
        plugin.getConfig().addDefault("TeleportDelay", 0);
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
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);

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

    public static boolean isCooldownEnabled() {
        return !(cooldown == 0);
    }

    public static Integer getCooldown() {
        return cooldown;
    }

    public static boolean isDelayEnabled() {
        return !(teleportDelay == 0);
    }

    public static Integer getTeleportDelay() {
        return teleportDelay;
    }

    public static Boolean getCancelTeleportOnMove() {
        return cancelTeleportOnMove;
    }
}
