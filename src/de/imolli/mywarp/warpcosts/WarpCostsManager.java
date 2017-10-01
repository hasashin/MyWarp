package de.imolli.mywarp.warpcosts;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.utils.MathUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class WarpCostsManager {

    private static File file;
    private static YamlConfiguration config;
    private static String currency;
    private static Boolean enabled;
    private static Double defaultValue;

    public static void init() {

        enabled = MyWarp.isWarpcostsEnabled();

        for (WarpCosts warpCosts : WarpCosts.values()) {
            warpCosts.init(MyWarp.getPlugin().getConfig());

            double round = MathUtils.round(warpCosts.getCosts(), 2);

            warpCosts.setCosts(round);
            MyWarp.getPlugin().getConfig().set(warpCosts.getConfigKey(), round);

        }

        MyWarp.getPlugin().saveConfig();

        defaultValue = MyWarp.getPlugin().getConfig().getDouble("WarpCosts.DefaultValue");

        if (MyWarp.isVaultEnabled()) {
            try {
                if (MyWarp.getEcon() != null) {
                    if (MyWarp.getEcon().currencyNamePlural() != null) {
                        currency = MyWarp.getEcon().currencyNamePlural();
                    }
                } else {
                    currency = "";
                }

                if (currency.equalsIgnoreCase("") || currency.equalsIgnoreCase(" ")) {
                    MyWarp.getPlugin().getLogger().log(Level.WARNING, "Currencyprefix is empty! Using default currencyprefix");
                    currency = "Money";
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyWarp.setVault(false);
                MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while getting currency name. Disabled Vault support!");
                currency = MessageManager.getMessage("MyWarp.warpcosts.currency");
            }
        } else {
            currency = MessageManager.getMessage("MyWarp.warpcosts.currency");
        }

        file = new File("plugins//MyWarp//warpcoins.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while creating 'warpcoins.yml'!");
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        config.options().copyDefaults(true);
        config.options().header("WarpCoins Storage File of MyWarp \n\n" +
                "Please note that editing the configurations while the server is running is not recommended.\n");

        config.addDefault("PlayerUUID", defaultValue);

        if (!MyWarp.isVaultEnabled()) {
            for (String name : config.getValues(false).keySet()) {
                config.set(name, MathUtils.round(config.getDouble(name), 2));
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void removeWarpCoins(Player p, Double value) {
        if (MyWarp.isVaultEnabled()) {
            MyWarp.getEcon().withdrawPlayer(p, value);
        } else {
            setWarpCoins(p, getBalance(p) - value);
        }
    }

    public static void addWarpCoins(Player p, Double value) {
        if (MyWarp.isVaultEnabled()) {
            MyWarp.getEcon().depositPlayer(p, value);
        } else {
            setWarpCoins(p, getBalance(p) + value);
        }
    }

    public static void setWarpCoins(Player p, Double value) {

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        value = MathUtils.round(value, 2);

        config.set(p.getUniqueId().toString(), value);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean exists(Player p) {
        if (MyWarp.isVaultEnabled()) {
            return MyWarp.getEcon().hasAccount(p);
        } else {
            return YamlConfiguration.loadConfiguration(file).contains(p.getUniqueId().toString());
        }
    }

    public static void createDefaultAccount(Player p) {

        if (!MyWarp.isVaultEnabled()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.set(p.getUniqueId().toString(), defaultValue);

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Double getBalance(Player p) {

        if (!exists(p)) {
            return 0.0;
        }

        if (MyWarp.isVaultEnabled()) {
            return MyWarp.getEcon().getBalance(p);
        } else {

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            return config.getDouble(p.getUniqueId().toString());
        }
    }

    public static boolean hasEnougtFor(Player p, WarpCosts warpCosts) {

        Double balance = getBalance(p);

        if (balance == 0) return false;

        return !(balance < warpCosts.getCosts());
    }

    public static String getCurrency() {
        return currency;
    }
}
