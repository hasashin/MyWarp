package de.imolli.mywarp;

import de.imolli.mywarp.commands.*;
import de.imolli.mywarp.listeners.*;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.managers.PlayerManager;
import de.imolli.mywarp.utils.SQLHandle;
import de.imolli.mywarp.warp.WarpHologramManager;
import de.imolli.mywarp.warp.WarpManager;
import de.imolli.mywarp.warp.gui.COMMAND_warpgui;
import de.imolli.mywarp.warp.gui.RemoveHologramGUI;
import de.imolli.mywarp.warp.gui.WarpFixGUI;
import de.imolli.mywarp.warp.gui.WarpGui;
import de.imolli.mywarp.warpcosts.WarpCosts;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
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
    private static Boolean warplimit;
    private static Boolean GUISound;
    private static Integer cooldown;
    private static Integer teleportDelay;
    private static Integer defaultLimit;
    private static Boolean updateCheck;
    private static Boolean updateNotify;
    private static Boolean deleteUnknownMessages;

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
        SQLHandle.init();
        WarpManager.init();
        PlayerManager.init();
        WarpCostsManager.init();
        WarpHologramManager.init();
        registerCommands();
        registerListeners();
        loadPlayers();

        Metrics metrics = new Metrics(this);

        metrics.addCustomChart(new Metrics.SimplePie("using_vault", () -> vault.toString()));

        metrics.addCustomChart(new Metrics.SimplePie("only_op", () -> onlyOp.toString()));

        metrics.addCustomChart(new Metrics.SimplePie("using_warpsigns", () -> warpSigns.toString()));

        metrics.addCustomChart(new Metrics.SingleLineChart("average_warps", () -> WarpManager.getWarps().size()));

        plugin.getLogger().log(Level.INFO, "MyWarp successfully loaded!");

        UpdateChecker.init();
        UpdateChecker.checkForUpdate();

        for (Player all : Bukkit.getOnlinePlayers()) {
            UpdateChecker.notifyPlayer(all);
            WarpManager.notifyPlayer(all);
        }
    }

    @Override
    public void onDisable() {

        //TODO: Improve Enable/Disable/Reload/Unload!

        WarpHologramManager.disappearAll();
        SQLHandle.disconnect();

        plugin.getLogger().log(Level.INFO, "MyWarp successfully unloaded!");
    }

    public static void reload() {

        plugin.getLogger().log(Level.INFO, "Reloading MyWarp...");

        WarpHologramManager.disappearAll();

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
        defaultLimit = null;
        warplimit = null;
        GUISound = null;
        updateCheck = null;
        updateNotify = null;
        deleteUnknownMessages = null;

        plugin.reloadConfig();
        checkConfig();
        loadConfig();

        if (vault) {
            if (!setupEconomy()) {
                plugin.getLogger().log(Level.SEVERE, "Disabled Vault support because no supported Vault plugin was found!");
                vault = false;
            } else {
                plugin.getLogger().log(Level.INFO, "Found Vault plugin, enabled Vault support");
            }
        }

        MessageManager.init();
        MessageManager.loadConfig();
        SQLHandle.init();
        WarpManager.init();
        PlayerManager.init();
        WarpCostsManager.init();
        WarpHologramManager.init();
        loadPlayers();


        for (Player all : Bukkit.getOnlinePlayers()) {
            UpdateChecker.notifyPlayer(all);
            WarpManager.notifyPlayer(all);
        }

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

            if (!vault && warpcosts && !WarpCostsManager.exists(p)) {
                WarpCostsManager.createDefaultAccount(p);
            }

        }
    }

    public static void loadConfig() {
        onlyOp = plugin.getConfig().getBoolean("OnlyOp");
        playSoundOnTeleport = plugin.getConfig().getBoolean("PlaySoundOnTeleport");
        GUISound = plugin.getConfig().getBoolean("PlayGUISounds");
        playParticleOnTeleport = plugin.getConfig().getBoolean("PlayParticleOnTeleport");
        warpSigns = plugin.getConfig().getBoolean("WarpSigns");
        cancelTeleportOnMove = plugin.getConfig().getBoolean("CancelTeleportOnMove");
        cooldown = plugin.getConfig().getInt("WarpCooldown");
        teleportDelay = plugin.getConfig().getInt("TeleportDelay");
        vault = plugin.getConfig().getBoolean("Vault");
        warpcosts = plugin.getConfig().getBoolean("WarpCosts.Enabled");
        defaultLimit = plugin.getConfig().getInt("WarpLimit.Default");
        warplimit = plugin.getConfig().getBoolean("WarpLimit.Enabled");
        updateCheck = plugin.getConfig().getBoolean("UpdateCheck");
        updateNotify = plugin.getConfig().getBoolean("UpdateNotify");
        deleteUnknownMessages = plugin.getConfig().getBoolean("DeleteUnknownMessages");

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
        plugin.getConfig().addDefault("UpdateCheck", true);
        plugin.getConfig().addDefault("UpdateNotify", true);
        plugin.getConfig().addDefault("DeleteUnknownMessages", false);
        plugin.getConfig().addDefault("PlaySoundOnTeleport", true);
        plugin.getConfig().addDefault("PlayGUISounds", true);
        plugin.getConfig().addDefault("PlayParticleOnTeleport", true);
        plugin.getConfig().addDefault("WarpSigns", true);
        plugin.getConfig().addDefault("CancelTeleportOnMove", false);
        plugin.getConfig().addDefault("WarpCooldown", 5);
        plugin.getConfig().addDefault("TeleportDelay", 0);
        plugin.getConfig().addDefault("Vault", false);
        plugin.getConfig().addDefault("WarpCosts.Enabled", false);
        plugin.getConfig().addDefault("WarpCosts.DefaultValue", 1000.0);

        //Create default values of warpcosts via the warpcosts enum
        for (WarpCosts warpCosts : WarpCosts.values()) {
            plugin.getConfig().addDefault(warpCosts.getConfigKey(), warpCosts.getDefaultValue());
        }

        plugin.getConfig().addDefault("WarpLimit.Enabled", false);
        plugin.getConfig().addDefault("WarpLimit.Default", 4);
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
        Bukkit.getPluginCommand("setWarpHologram").setExecutor(new COMMAND_setwarphologram());
        Bukkit.getPluginCommand("removeWarpHologram").setExecutor(new COMMAND_removewarphologram());
        Bukkit.getPluginCommand("warprename").setExecutor(new COMMAND_warprename());

    }

    private void registerListeners() {

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new SignChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTabCompleteListener(), this);
        Bukkit.getPluginManager().registerEvents(new WarpGui(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new WarpHologramManager(), this);
        Bukkit.getPluginManager().registerEvents(new RemoveHologramGUI(), this);
        Bukkit.getPluginManager().registerEvents(new WarpFixGUI(), this);

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

    public static Boolean isWarplimit() {
        return warplimit;
    }

    public static Integer getDefaultLimit() {
        return defaultLimit;
    }

    public static boolean isGUISoundEnabled() {
        return GUISound;
    }

    public static Boolean getUpdateCheck() {
        return updateCheck;
    }

    public static Boolean getUpdateNotify() {
        return updateNotify;
    }

    public static Boolean getDeleteUnknownMessages() {
        return deleteUnknownMessages;
    }
}
