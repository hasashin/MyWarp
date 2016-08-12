package de.ImOlli.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import de.ImOlli.mywarp.MyWarp;

public class MessageManager {
	
	private static File file;
	private static YamlConfiguration config;
	private static HashMap<String, String> messages;
	
	public static void init() {
	
		messages = new HashMap<String, String>();
		file = new File("plugins//MyWarp//messages.yml");
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[MyWarp] Fehler beim erstellen der Datei 'messages.yml'!");
				return;
			}
		}
		
		config = YamlConfiguration.loadConfiguration(file);
		
		config.options().copyDefaults(true);
		config.options().header("Messages Configuration of MyWarp");
			
		config.addDefault("MyWarp.prefix", "&eMyWarp &8> ");
		config.addDefault("MyWarp.reload", "&aAlle Konfigurationen wurden neugeladen.");
		config.addDefault("MyWarp.warp.msg", "&7Du wurdest zum Warp Punkt &e%name% &7teleportiert.");
		config.addDefault("MyWarp.noperm.msg", "&cDu hast keine Rechte dazu!");
		config.addDefault("MyWarp.warp.notexist", "&cDer Warp Punkt &e%name% &cexistiert nicht!");
		config.addDefault("MyWarp.warp.exist", "&cDer Warp Punkt &e%name% &cexistiert schon!");
		config.addDefault("MyWarp.warp.create", "&7Du hast erfolgreich den Warp Punkt &e%name% &7erstellt.");
		config.addDefault("MyWarp.warp.remove", "&7Du hast erfolgreich den Warp Punkt &e%name% &7entfernt.");
		config.addDefault("MyWarp.error.msg", "&cEin Fehler ist aufgetreten!");
		config.addDefault("MyWarp.cmd.error", "&cNutze %cmd%&c!");
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadConfig(){
		
		String prefix = config.getString("MyWarp.prefix");
		String reload = config.getString("MyWarp.reload");
		String warp = config.getString("MyWarp.warp.msg");
		String noperm = config.getString("MyWarp.noperm.msg");
		String notexist = config.getString("MyWarp.warp.notexist");
		String exist = config.getString("MyWarp.warp.exist");
		String create = config.getString("MyWarp.warp.create");
		String remove = config.getString("MyWarp.warp.remove");
		String error = config.getString("MyWarp.error.msg");
		String cmderror = config.getString("MyWarp.cmd.error");
		
		prefix = prefix.replaceAll("&", "§");
		reload = reload.replaceAll("&", "§");
		warp = warp.replaceAll("&", "§");
		noperm = noperm.replaceAll("&", "§");
		notexist = notexist.replaceAll("&", "§");
		exist = exist.replaceAll("&", "§");
		create = create.replaceAll("&", "§");
		remove = remove.replaceAll("&", "§");
		error = error.replaceAll("&", "§");
		cmderror = cmderror.replaceAll("&", "§");
		
		messages.put("MyWarp.prefix", prefix);
		messages.put("MyWarp.reload", reload);
		messages.put("MyWarp.warp.msg", warp);
		messages.put("MyWarp.noperm.msg", noperm);
		messages.put("MyWarp.warp.notexist", notexist);
		messages.put("MyWarp.warp.exist", exist);
		messages.put("MyWarp.warp.create", create);
		messages.put("MyWarp.warp.remove", remove);
		messages.put("MyWarp.error.msg", error);
		messages.put("MyWarp.cmd.error", cmderror);
		
		MyWarp.setPrefix(prefix);
	}
	
	public static String getMessage(String name){
		return (messages.containsKey(name)) ? messages.get(name) : name;
	}
	
}
