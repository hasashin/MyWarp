package de.ImOlli.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.mywarp.MyWarp;

public class COMMAND_mywarp implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(!(cs instanceof Player)){
			cs.sendMessage("Du bist kein Spieler");
			return true;
		}
		
		Player p = (Player)cs;
		
		if(MyWarp.isOnlyOp() && !p.isOp()){
			p.sendMessage(MyWarp.getPrefix()+MessageManager.getMessage("MyWarp.noperm.msg"));
			return true;
		}
		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("reload")){
				MyWarp.checkConfig();
				MyWarp.loadConfig();
				MessageManager.init();
				MessageManager.loadConfig();
				WarpManager.init();
				p.sendMessage(MyWarp.getPrefix()+MessageManager.getMessage("MyWarp.reload"));
				return true;
			}else{
				p.sendMessage(MyWarp.getPrefix()+MessageManager.getMessage("MyWarp.cmderror.msg").replaceAll("%cmd%", "/Mywarp [reload]"));
				return true;
			}
		}else{
			p.sendMessage(MyWarp.getPrefix()+MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/Mywarp [reload]"));
			return true;
		}
	}
}
