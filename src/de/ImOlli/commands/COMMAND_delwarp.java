package de.ImOlli.commands;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.mywarp.MyWarp;
import de.ImOlli.objects.Warp;
import de.ImOlli.objects.WarpGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_delwarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("You are not an player!");
            return true;
        }

        Player p = (Player) cs;

        if (MyWarp.isOnlyOp() && !p.isOp()) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
            return true;
        }

        if (!p.hasPermission("MyWarp.warp.delete")) {
            return false;
        }

        if (args.length == 1) {
            String warpname = args[0];

            if (!WarpManager.existWarp(warpname)) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.notexist").replaceAll("%name%", warpname));
                return true;
            }

            Warp warp = WarpManager.getWarp(warpname.toLowerCase());

            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.remove").replaceAll("%name%", warp.getName()));
            WarpManager.removeWarp(warp.getName());
            return true;
        } else {
            WarpGui.openWarpDeleteGui(p, 1);
            return true;
        }
    }
}
