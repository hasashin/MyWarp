package de.imolli.mywarp.managers;

import de.imolli.mywarp.MyWarp;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

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
                MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while creating 'messages.yml'!");
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        config.options().copyDefaults(true);
        config.options().header("Messages configuration of MyWarp \n\n" +
                "Please note that editing the configurations while the server is running is not recommended.\n");

        config.addDefault("MyWarp.prefix", "&aMyWarp &8» ");
        config.addDefault("MyWarp.console.notplayer", "You are not an player!");
        config.addDefault("MyWarp.reload", "&aReloaded all configurations.");
        config.addDefault("MyWarp.noperm.msg", "&cYou don't have enough permissions for that!");

        config.addDefault("MyWarp.warpcosts.currency", "&eWarpCoins");
        config.addDefault("MyWarp.warpcosts.notenough", "&cYou do not have enough &e%currency% &cto perform that! &7You need &e%amount% %currency% &7to perform this!");
        config.addDefault("MyWarp.warpcoins.onlyavailable", "&c/warpcoins is only available if vault support is disabled in the config!");
        config.addDefault("MyWarp.warpcoins.showcoins", "&7You have currently &e%amount% %currency%&7.");
        config.addDefault("MyWarp.warpcoins.add", "&7You gave the player &e%name% %amount% %currency%&7.");
        config.addDefault("MyWarp.warpcoins.add2", "&7You got &e%amount% %currency%&7.");
        config.addDefault("MyWarp.warpcoins.remove", "&7You have deducted &e%amount% %currency% &7from the player &e%name%&7.");
        config.addDefault("MyWarp.warpcoins.remove2", "&7You got &e%amount% %currency% &7deducted.");
        config.addDefault("MyWarp.warpcoins.get", "&7The player &e%name% &7have currently &e%amount% %currency%&7.");

        config.addDefault("MyWarp.warps.msg", "&7List of all Warps (&e%warps%&7)");
        config.addDefault("MyWarp.warps.msgwithwarpcosts", "&7You listed all warps for &e%amount% %currency%&7.");

        config.addDefault("MyWarp.warp.msgwithwarpcosts", "&7You were warped to &e%name% &7for &e%amount% %currency%&7.");
        config.addDefault("MyWarp.warp.msg", "&7You were warped to &e%name%&7.");
        config.addDefault("MyWarp.warp.notexist", "&cThe warppoint &e%name% &cdon't exists!");
        config.addDefault("MyWarp.warp.exist", "&cThe warppoint &e%name% &calready exist!");
        config.addDefault("MyWarp.warp.create", "&7You successfully created the warppoint &e%name%&7.");
        config.addDefault("MyWarp.warp.createwithwarpcosts", "&7You successfully created the warppoint &e%name% &7for &e%amount% %currency%&7.");
        config.addDefault("MyWarp.warp.remove", "&7You successfully removed the warppoint &e%name%&7.");
        config.addDefault("MyWarp.warp.removewithwarpcosts", "&7You successfully removed the warppoint &e%name% &7for &e%amount% %currency%&7.");
        config.addDefault("MyWarp.warp.cooldown", "&7You must wait &e%time% seconds &7before you can warp again.");
        config.addDefault("MyWarp.warp.beforedelayedwarping", "&7You will be warped in &e%time% seconds&7.");
        config.addDefault("MyWarp.warp.currentlywarping", "&cYou are currently warping!");
        config.addDefault("MyWarp.warp.movecancel", "&cYour warp was canceled because you moved!");

        config.addDefault("MyWarp.warp.sign.success", "&7You successfully created an warpsign &7to &e%name%&7.");
        config.addDefault("MyWarp.warp.sign.empty", "&cPlease write an warpname in the second line!");

        config.addDefault("MyWarp.warp.gui.title.menu", "&e&lWarpGUI &8» &6Menu");
        config.addDefault("MyWarp.warp.gui.title.selwarp", "&e&lWarpGUI &8» &5Warp");
        config.addDefault("MyWarp.warp.gui.title.modify", "&e&lWarpGUI &8» &aModify");
        config.addDefault("MyWarp.warp.gui.title.rename", "&e&lWarpGUI &8» &bRename");
        config.addDefault("MyWarp.warp.gui.title.delete", "&e&lWarpGUI &8» &cDelete");

        config.addDefault("MyWarp.warp.gui.lore.warp", "&7by %name%\n \n&aClick to teleport.");
        config.addDefault("MyWarp.warp.gui.lore.delete", "&7by %name%\n \n&cClick to delete.");

        config.addDefault("MyWarp.warp.gui.lore.search", " \n&eLeft click &8| &7Open the Search GUI.\n&eRight click &8| &7Reset the search.\n ");

        config.addDefault("MyWarp.warp.gui.lore.modify.info", " \n§7Warp: §e%name%\n§7Owner: §e%creator%\n§7Location: §e%location%\n ");
        config.addDefault("MyWarp.warp.gui.lore.modify.rename", " \n&7Click on &aconfirm &7to delete the warppoint &e%name%&7.\n ");

        config.addDefault("MyWarp.warp.gui.lore.delete.info", " \n&7Click on &aconfirm &7to delete the warppoint &e%name%&7.\n ");
        config.addDefault("MyWarp.warp.gui.lore.delete.confirm", " \n&7Click to confirm &edelete &7warppoint &e%name%&7!\n ");
        config.addDefault("MyWarp.warp.gui.lore.delete.cancel", " \n&7Click to cancel &edelete&7.\n ");

        config.addDefault("MyWarp.warp.gui.lore.rename.info", " \n&7Old name: &e%name% \n&7New name: &e%newname%\n \n&7Click &aconfirm &7to rename the\n&7warppoint &e%name% &7into &e%newname%&7.\n ");
        config.addDefault("MyWarp.warp.gui.lore.rename.confirm", " \n&7Click to &erename &7to &e%newname%&7!\n ");
        config.addDefault("MyWarp.warp.gui.lore.rename.cancel", " \n&7Click to cancel &erename&7.\n ");

        config.addDefault("MyWarp.warp.gui.lore.option.info", " \n§7Warp: §e%name%\n§7Owner: §e%creator%\n§7Location: §e%location%\n ");
        config.addDefault("MyWarp.warp.gui.lore.option.teleport", " \n&7Click to &ewarp &7to the warpoint\n ");
        config.addDefault("MyWarp.warp.gui.lore.option.modify", " \n&7Click to &emodify &7the warpoint\n ");
        config.addDefault("MyWarp.warp.gui.lore.option.delete", " \n&7Click to &edelete &7the warpoint\n ");

        config.addDefault("MyWarp.warp.gui.search.basic", "Search Here");

        config.addDefault("MyWarp.warp.gui.item.back", "&cBack");
        config.addDefault("MyWarp.warp.gui.item.warp", "&aWarp");
        config.addDefault("MyWarp.warp.gui.item.modify", "&cModify");
        config.addDefault("MyWarp.warp.gui.item.delete", "&cdelete");
        config.addDefault("MyWarp.warp.gui.item.information", "&aInformation");
        config.addDefault("MyWarp.warp.gui.item.confirm", "&aConfirm");
        config.addDefault("MyWarp.warp.gui.item.cancel", "&cCancel");
        config.addDefault("MyWarp.warp.gui.item.nextpage", "&eNext Page");
        config.addDefault("MyWarp.warp.gui.item.previouspage", "&ePrevious Page");
        config.addDefault("MyWarp.warp.gui.item.nowarps", "&cSorry, but no warps exist.");
        config.addDefault("MyWarp.warp.gui.item.rename", "&aRename");

        config.addDefault("MyWarp.error.msg", "&cAn error occurred!");
        config.addDefault("MyWarp.error.warp", "&cThis warppoint does not longer exists!");
        config.addDefault("MyWarp.error.number", "&cPlease enter only numbers!");
        config.addDefault("MyWarp.error.notonline", "&cThe player &e%name% %cisn't online!");
        config.addDefault("MyWarp.error.onlypositvenumbers", "&cPlease enter only positive numbers!");
        config.addDefault("MyWarp.cmd.error", "&cUse %cmd%&c!");

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {

        ArrayList<String> unknown = new ArrayList<>();

        for (String key : config.getKeys(true)) {

            if (!config.getDefaults().contains(key)) {
                unknown.add(key);
            }

            String value = config.getString(key);
            value = ChatColor.translateAlternateColorCodes('&', value);

            messages.put(key, value);
        }

        MyWarp.setPrefix(messages.getOrDefault("MyWarp.prefix", ""));

        if (!unknown.isEmpty()) {
            MyWarp.getPlugin().getLogger().log(Level.WARNING, "Found " + unknown.size() + " unknown messages in messages.yml");

            for (String key : unknown) {
                MyWarp.getPlugin().getLogger().log(Level.WARNING, "     " + key);
            }
        }

    }

    public static String getMessage(String name) {
        return messages.getOrDefault(name, name);
    }

}
