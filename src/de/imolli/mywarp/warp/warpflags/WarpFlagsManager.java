package de.imolli.mywarp.warp.warpflags;

import de.imolli.mywarp.MyWarp;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class WarpFlagsManager {

    private static File file = new File("plugins/MyWarp/warpflags.yml");
    private static YamlConfiguration config;

    public static void init() {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while creating 'warpflags.yml'!");
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        loadConfig();
        loadWarpFlagsDefaults();

    }

    private static void loadConfig() {

        config.options().header("WarpFlags of MyWarp \n\n" +
                "Please note that editing the configurations while the server is running is not recommended.\n");
        config.options().copyDefaults(true);


        config.addDefault("Permissions.NoCost", "default");
        config.addDefault("Permissions.OnlyOpCanWarp", "default");
        config.addDefault("Permissions.OnlyCreatorCanWarp", "everyone");
        config.addDefault("Permissions.GuiInvisible", "default");

        config.addDefault("Default.NoCost", "false");
        config.addDefault("Default.OnlyOpCanWarp", "false");
        config.addDefault("Default.OnlyCreatorCanWarp", "false");
        config.addDefault("Default.GuiInvisible", "false");

    }

    private static void loadWarpFlagsDefaults() {

        for (String key : config.getKeys(true)) {
            if (key.startsWith("Permissions.")) {
                key = key.replace("Permissions.", "");
                WarpFlag.valueOf(key.toLowerCase()).setDefault(config.getBoolean(key));

            }
        }

    }

}
