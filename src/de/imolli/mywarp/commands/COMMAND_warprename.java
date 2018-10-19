package de.imolli.mywarp.commands;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.utils.SQLHandle;
import de.imolli.mywarp.warp.Warp;
import de.imolli.mywarp.warp.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_warprename implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageManager.getMessage("MyWarp.console.notplayer"));
            return true;
        }

        Player p = (Player) cs;

        if (MyWarp.isOnlyOp() && !p.isOp()) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
            return true;
        }

        if (!p.hasPermission("MyWarp.warp.rename")) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
            return false;
        }

        if (args.length == 2) {
            String warpname = args[0];
            String newname = args[1];

            if (!WarpManager.existWarp(warpname.toLowerCase())) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.notexist").replaceAll("%name%", warpname));
                return true;
            }

            if (WarpManager.existWarp(newname)) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.nameexist").replaceAll("%name%", newname));
                return true;
            }
            Warp warp = WarpManager.getWarp(warpname);

            SQLHandle.update("UPDATE `warps` SET `name` = '" + newname.toLowerCase() + "', `displayname` = '" + newname + "' WHERE `name` = '" + warpname + "';");

            warp.setName(newname);
            WarpManager.updateWarp(warpname, newname, warp);

            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.namechanged").replaceAll("%name%", newname));

            return true;
        } else {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/warprename [warp] [newname]"));
            return true;
        }
    }
}
