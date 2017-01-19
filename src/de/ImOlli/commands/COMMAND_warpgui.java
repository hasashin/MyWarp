package de.ImOlli.commands;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.mywarp.MyWarp;
import de.ImOlli.objects.WarpGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_warpgui implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String args, String[] label) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("You are not a player!");
            return true;
        }

        Player p = (Player) cs;

        if (MyWarp.isOnlyOp() && !p.isOp()) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
            return true;
        }

        if (!p.hasPermission("MyWarp.warp.warp")) {
            return false;
        }

        WarpGui.openWarpTeleportGui(p, 1);

        return true;
    }
}
