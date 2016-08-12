package de.ImOlli.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ImOIli.objects.Warp;
import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.mywarp.MyWarp;

public class COMMAND_warp implements CommandExecutor{

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
			String warpname = args[0];
			
			Warp warp = WarpManager.getWarp(warpname);
			
			if(warp == null){
				p.sendMessage(MyWarp.getPrefix()+MessageManager.getMessage("MyWarp.warp.notexist").replaceAll("%name%", warpname));
				return true;
			}
			
			warp.teleport(p);
			p.sendMessage(MyWarp.getPrefix()+MessageManager.getMessage("MyWarp.warp.msg").replaceAll("%name%", warpname));
			return true;
		}else{
			p.sendMessage(MyWarp.getPrefix()+MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/warp [Name]"));
			return true;
		}
	}
}
