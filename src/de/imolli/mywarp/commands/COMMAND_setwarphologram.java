package de.imolli.mywarp.commands;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.warp.Warp;
import de.imolli.mywarp.warp.WarpHologramManager;
import de.imolli.mywarp.warp.WarpManager;
import de.imolli.mywarp.warpcosts.WarpCosts;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_setwarphologram implements CommandExecutor {

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

        if (!p.hasPermission("MyWarp.warp.hologram.create")) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("Warp.noperm.msg"));
            return false;
        }

        if (args.length == 1) {
            String warpname = args[0];

            if (!WarpManager.existWarp(warpname)) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.notexist").replaceAll("%name%", warpname));
                return true;
            }

            if (MyWarp.isWarpcostsEnabled() && WarpCosts.CREATEHOLOGRAM.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore")) {
                if (WarpCostsManager.hasEnoughFor(p, WarpCosts.CREATEHOLOGRAM)) {
                    WarpCostsManager.removeWarpCoins(p, WarpCosts.CREATEHOLOGRAM.getCosts());
                } else {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.CREATEHOLOGRAM.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                    return true;
                }
            }

            Warp warp = WarpManager.getWarp(warpname.toLowerCase());
            WarpHologramManager.addWarpHologram(p, warp, p.getLocation());

            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.hologram.create").replaceAll("%name%", warp.getName()));

            return true;
        }

        return false;
    }
}
