package de.ImOlli.managers;

import de.ImOlli.mywarp.MyWarp;
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
                "Please note that you can only edit the config when the server is off.");

        config.addDefault("MyWarp.prefix", "&eMyWarp &8» ");
        config.addDefault("MyWarp.reload", "&aReloaded all configurations.");
        config.addDefault("MyWarp.warp.msg", "&7You were warped to &e%name%§7.");
        config.addDefault("MyWarp.warps.msg", "&7List of all Warps (&e%warps%&7)");
        config.addDefault("MyWarp.noperm.msg", "&cYou don't have enough permissions for that!");
        config.addDefault("MyWarp.warp.notexist", "&cThe warppoint &e%name% &cdidn't exists!");
        config.addDefault("MyWarp.warp.exist", "&cThe warppoint &e%name% &calready exist!");
        config.addDefault("MyWarp.warp.create", "&7You successfully created the warppoint &e%name%&7.");
        config.addDefault("MyWarp.warp.remove", "&7You successfully removed the warppoint &e%name%&7.");
        config.addDefault("MyWarp.warp.gui.title.warp", "&eWarpGUI §5Teleport");
        config.addDefault("MyWarp.warp.gui.title.delete", "&eWarpGUI §cDelete");
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

        String prefix = config.getString("MyWarp.prefix");
        String reload = config.getString("MyWarp.reload");
        String warp = config.getString("MyWarp.warp.msg");
        String warps = config.getString("MyWarp.warps.msg");
        String noperm = config.getString("MyWarp.noperm.msg");
        String notexist = config.getString("MyWarp.warp.notexist");
        String exist = config.getString("MyWarp.warp.exist");
        String create = config.getString("MyWarp.warp.create");
        String remove = config.getString("MyWarp.warp.remove");
        String guititlewarp = config.getString("MyWarp.warp.gui.title.warp");
        String guititledelete = config.getString("MyWarp.warp.gui.title.delete");
        String guinextpage = config.getString("MyWarp.warp.gui.nextpage");
        String guipreviouspage = config.getString("MyWarp.warp.gui.previouspage");
        String guinowarps = config.getString("MyWarp.warp.gui.nowarps");
        String guilorewarp = config.getString("MyWarp.warp.gui.lore.warp");
        String guiloredelete = config.getString("MyWarp.warp.gui.lore.delete");
        String error = config.getString("MyWarp.error.msg");
        String cmderror = config.getString("MyWarp.cmd.error");

        prefix = prefix.replaceAll("&", "§");
        reload = reload.replaceAll("&", "§");
        warp = warp.replaceAll("&", "§");
        warps = warps.replaceAll("&", "§");
        noperm = noperm.replaceAll("&", "§");
        notexist = notexist.replaceAll("&", "§");
        exist = exist.replaceAll("&", "§");
        create = create.replaceAll("&", "§");
        remove = remove.replaceAll("&", "§");
        guititlewarp = guititlewarp.replaceAll("&", "§");
        guititledelete = guititledelete.replaceAll("&", "§");
        guinextpage = guinextpage.replaceAll("&", "§");
        guipreviouspage = guipreviouspage.replaceAll("&", "§");
        guinowarps = guinowarps.replaceAll("&", "§");
        guilorewarp = guilorewarp.replaceAll("&", "§");
        guiloredelete = guiloredelete.replaceAll("&", "§");
        error = error.replaceAll("&", "§");
        cmderror = cmderror.replaceAll("&", "§");

        messages.put("MyWarp.prefix", prefix);
        messages.put("MyWarp.reload", reload);
        messages.put("MyWarp.warp.msg", warp);
        messages.put("MyWarp.warps.msg", warps);
        messages.put("MyWarp.noperm.msg", noperm);
        messages.put("MyWarp.warp.notexist", notexist);
        messages.put("MyWarp.warp.exist", exist);
        messages.put("MyWarp.warp.create", create);
        messages.put("MyWarp.warp.remove", remove);
        messages.put("MyWarp.warp.gui.title.warp", guititlewarp);
        messages.put("MyWarp.warp.gui.title.delete", guititledelete);
        messages.put("MyWarp.warp.gui.nextpage", guinextpage);
        messages.put("MyWarp.warp.gui.previouspage", guipreviouspage);
        messages.put("MyWarp.warp.gui.nowarps", guinowarps);
        messages.put("MyWarp.warp.gui.lore.warp", guilorewarp);
        messages.put("MyWarp.warp.gui.lore.delete", guiloredelete);
        messages.put("MyWarp.error.msg", error);
        messages.put("MyWarp.cmd.error", cmderror);

        MyWarp.setPrefix(prefix);
    }

    public static String getMessage(String name) {
        return (messages.containsKey(name)) ? messages.get(name) : name;
    }

}
