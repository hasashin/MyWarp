package de.imolli.mywarp.warpgui;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.warpcosts.WarpCosts;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_warpgui implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String args, String[] label) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageManager.getMessage("MyWarp.console.notplayer"));
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

        if (MyWarp.isWarpcostsEnabled() && WarpCosts.LISTWARPS.isActive()) {
            if (WarpCostsManager.hasEnougtFor(p, WarpCosts.LISTWARPS)) {
                WarpCostsManager.removeWarpCoins(p, WarpCosts.LISTWARPS.getCosts());
            } else {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.LISTWARPS.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                return true;
            }
        }

        WarpGui.openWarpTeleportGui(p, 1);

        return true;
    }
}
