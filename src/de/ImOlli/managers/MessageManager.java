package de.ImOlli.managers;

import de.ImOlli.mywarp.MyWarp;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MessageManager {

    private static File file;
    private static YamlConfiguration config;
    private static HashMap<String, String> messages;

    public static void init() {

        messages = new HashMap<>();
        file = new File("plugins//MyWarp//messages.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[MyWarp] An error occurred while creating 'messages.yml'!");
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        config.options().copyDefaults(true);
        config.options().header("Messages configuration of MyWarp \n\n" +
                "Please note that editing the configurations while the server is running is not recommended.");

        config.addDefault("MyWarp.prefix", "&aMyWarp &8» ");
        config.addDefault("MyWarp.console.notplayer", "You are not an player!");
        config.addDefault("MyWarp.reload", "&aReloaded all configurations.");
        config.addDefault("MyWarp.warp.msg", "&7You were warped to &e%name%§7.");
        config.addDefault("MyWarp.warps.msg", "&7List of all Warps (&e%warps%&7)");
        config.addDefault("MyWarp.noperm.msg", "&cYou don't have enough permissions for that!");
        config.addDefault("MyWarp.warp.notexist", "&cThe warppoint &e%name% &cdidn't exists!");
        config.addDefault("MyWarp.warp.exist", "&cThe warppoint &e%name% &calready exist!");
        config.addDefault("MyWarp.warp.create", "&7You successfully created the warppoint &e%name%&7.");
        config.addDefault("MyWarp.warp.remove", "&7You successfully removed the warppoint &e%name%&7.");
        config.addDefault("MyWarp.warp.cooldown", "&7You must wait &e%time% seconds &7before you can warp again.");
        config.addDefault("MyWarp.warp.beforedelayedwarping", "&7You will be warped in &e%time% seconds&7.");
        config.addDefault("MyWarp.warp.currentlywarping", "&cYou are currently warping!");
        config.addDefault("MyWarp.warp.movecancel", "&cYour warp was canceled because you moved!");
        config.addDefault("MyWarp.warp.sign.success", "&7You successfully created an warpsign §7to &e%name%&7.");
        config.addDefault("MyWarp.warp.sign.empty", "&cPlease write an warpname in the second line!");
        config.addDefault("MyWarp.warp.gui.title.warp", "&e&lWarpGUI &8| §5Teleport");
        config.addDefault("MyWarp.warp.gui.title.delete", "&e&lWarpGUI &8| §cDelete");
        config.addDefault("MyWarp.warp.gui.nextpage", "&eNext Page");
        config.addDefault("MyWarp.warp.gui.previouspage", "&ePrevious Page");
        config.addDefault("MyWarp.warp.gui.nowarps", "&cSorry, but no warps exist.");
        config.addDefault("MyWarp.warp.gui.lore.warp", "§7by %name%\n \n§aClick to teleport.");
        config.addDefault("MyWarp.warp.gui.lore.delete", "§7by %name%\n \n§cClick to delete.");
        config.addDefault("MyWarp.error.msg", "&cAn error occurred!");
        config.addDefault("MyWarp.cmd.error", "&cUse %cmd%&c!");

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {

        for (String key : config.getKeys(true)) {
            String value = config.getString(key);
            value = ChatColor.translateAlternateColorCodes('&', value);

            messages.put(key, value);
        }

        MyWarp.setPrefix(messages.getOrDefault("MyWarp.prefix", ""));
    }

    public static String getMessage(String name) {
        return (messages.containsKey(name)) ? messages.get(name) : name;
    }

}
