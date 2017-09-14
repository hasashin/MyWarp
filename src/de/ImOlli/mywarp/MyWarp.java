package de.ImOlli.mywarp;

import de.ImOlli.commands.*;
import de.ImOlli.listeners.*;
import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.PlayerManager;
import de.ImOlli.managers.WarpCostsManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.objects.WarpGui;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MyWarp extends JavaPlugin {

    private static Plugin plugin;
    private static String prefix;
    private static Boolean onlyOp;
    private static Boolean playSoundOnTeleport;
    private static Boolean playParticleOnTeleport;
    private static Boolean warpSigns;
    private static Boolean cancelTeleportOnMove;
    private static Boolean vault;
    private static Boolean warpcosts;
    private static Integer cooldown;
    private static Integer teleportDelay;

    private static Economy econ = null;

    @Override
    public void onEnable() {
        plugin = this;

        checkConfig();
        loadConfig();

        if (vault) {
            if (!setupEconomy()) {
                plugin.getLogger().log(Level.SEVERE, "Disabled Vault support because no supported Vault plugin was found!");
                vault = false;
            } else {
                plugin.getLogger().log(Level.INFO, "Found Vault plugin, Enabled Vault support");
            }
        }

        MessageManager.init();
        MessageManager.loadConfig();
        WarpManager.init();
        PlayerManager.init();
        WarpCostsManager.init();
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
        vault = null;
        warpcosts = null;

        plugin.reloadConfig();
        checkConfig();
        loadConfig();

        if (vault) {
            if (!setupEconomy()) {
                plugin.getLogger().log(Level.SEVERE, "Disabled Vault support because no supported Vault plugin was found!");
                vault = false;
            } else {
                plugin.getLogger().log(Level.INFO, "Found Vault plugin, Enabled Vault support");
            }
        }

        MessageManager.init();
        MessageManager.loadConfig();
        WarpManager.init();
        PlayerManager.init();
        WarpCostsManager.init();
        loadPlayers();

    }

    private static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();

        return econ != null;
    }

    private static void loadPlayers() {

        for (Player p : Bukkit.getOnlinePlayers()) {

            PlayerManager.registerPlayer(p);

            if (warpcosts && !WarpCostsManager.exists(p)) {
                WarpCostsManager.createDefault(p);
            }

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
        vault = plugin.getConfig().getBoolean("Vault");
        warpcosts = plugin.getConfig().getBoolean("WarpCosts.Enabled");

        if (warpcosts) {
            if (vault) {
                plugin.getLogger().log(Level.INFO, "WarpCosts are enabled. Overwriting it with Vault.");
            }
        } else {
            vault = false;
        }

        if (cooldown <= 0) {
            cooldown = 0;
        }

        if (teleportDelay <= 0) {
            teleportDelay = 0;
            cancelTeleportOnMove = false;
        }
    }

    public static void checkConfig() {

        plugin.getConfig().options().copyDefaults(true);
        plugin.getConfig().options().header("Configuration of MyWarp \n\n" +
                "Please note that editing the configurations while the server is running is not recommended.\n");

        plugin.getConfig().addDefault("OnlyOp", false);
        plugin.getConfig().addDefault("PlaySoundOnTeleport", true);
        plugin.getConfig().addDefault("PlayParticleOnTeleport", true);
        plugin.getConfig().addDefault("WarpSigns", true);
        plugin.getConfig().addDefault("CancelTeleportOnMove", false);
        plugin.getConfig().addDefault("WarpCooldown", 5);
        plugin.getConfig().addDefault("TeleportDelay", 0);
        plugin.getConfig().addDefault("Vault", false);
        plugin.getConfig().addDefault("WarpCosts.Enabled", false);
        plugin.getConfig().addDefault("WarpCosts.DefaultValue", 1000.0);
        plugin.getConfig().addDefault("WarpCosts.Warp", 10.0);
        plugin.getConfig().addDefault("WarpCosts.CreateWarp", 100.0);
        plugin.getConfig().addDefault("WarpCosts.DeleteWarp", 50.0);
        plugin.getConfig().addDefault("WarpCosts.ListWarps", 5.0);
        plugin.saveConfig();

    }

    private void registerCommands() {

        Bukkit.getPluginCommand("warp").setExecutor(new COMMAND_warp());
        Bukkit.getPluginCommand("setwarp").setExecutor(new COMMAND_setwarp());
        Bukkit.getPluginCommand("warps").setExecutor(new COMMAND_warps());
        Bukkit.getPluginCommand("delwarp").setExecutor(new COMMAND_delwarp());
        Bukkit.getPluginCommand("mywarp").setExecutor(new COMMAND_mywarp());
        Bukkit.getPluginCommand("warpgui").setExecutor(new COMMAND_warpgui());
        Bukkit.getPluginCommand("warpcoins").setExecutor(new COMMAND_warpcoins());

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

    public static void setVault(boolean vault) {
        MyWarp.vault = vault;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static Boolean isOnlyOp() {
        return onlyOp;
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

    public static Boolean isVaultEnabled() {
        return vault;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static Boolean isWarpcostsEnabled() {
        return warpcosts;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
