package de.ImOlli.commands;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpCosts;
import de.ImOlli.managers.WarpCostsManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.mywarp.MyWarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_setwarp implements CommandExecutor {

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

        if (!p.hasPermission("MyWarp.warp.create")) {
            return false;
        }

        if (args.length == 1) {
            String warpname = args[0];

            if (WarpManager.existWarp(warpname.toLowerCase())) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.exist").replaceAll("%name%", warpname));
                return true;
            }

            if (MyWarp.isWarpcostsEnabled() && WarpCosts.CREATEWARP.isActive()) {
                if (WarpCostsManager.hasEnougtFor(p, WarpCosts.CREATEWARP)) {
                    System.out.println("ERERER");
                    WarpCostsManager.removeWarpCoins(p, WarpCosts.CREATEWARP.getCosts());
                } else {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.CREATEWARP.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                    return true;
                }
            }

            WarpManager.addWarp(warpname, p, p.getLocation());

            if (MyWarp.isWarpcostsEnabled() && WarpCosts.CREATEWARP.isActive()) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.createwithwarpcosts").replaceAll("%name%", warpname).replaceAll("%amount%", WarpCosts.CREATEWARP.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
            } else {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.create").replaceAll("%name%", warpname));
            }
            return true;
        } else {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/setwarp [Name]"));
            return true;
        }
    }
}
