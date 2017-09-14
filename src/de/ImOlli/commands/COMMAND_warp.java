package de.ImOlli.commands;

import de.ImOlli.managers.*;
import de.ImOlli.mywarp.MyWarp;
import de.ImOlli.objects.Warp;
import de.ImOlli.objects.WarpDelay;
import de.ImOlli.objects.WarpGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_warp implements CommandExecutor {

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

        if (!p.hasPermission("MyWarp.warp.warp")) {
            return false;
        }

        if (args.length == 1) {
            String warpname = args[0];

            Warp warp = WarpManager.getWarp(warpname.toLowerCase());

            if (warp == null) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.notexist").replaceAll("%name%", warpname));
                return true;
            }

            if (MyWarp.isDelayEnabled()) {
                if (PlayerManager.isCurrentlyWarping(p)) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.currentlywarping"));
                    return true;
                }
            }

            if (MyWarp.isCooldownEnabled()) {
                if (PlayerManager.getCooldown(p) > 0) {

                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.cooldown").replaceAll("%time%", PlayerManager.getCooldown(p).toString()));
                    return true;

                } else {
                    if (MyWarp.isWarpcostsEnabled()) {
                        if (WarpCostsManager.hasEnougtFor(p, WarpCosts.WARP)) {
                            WarpCostsManager.removeWarpCoins(p, WarpCosts.WARP.getCosts());
                        } else {
                            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.WARP.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                            return true;
                        }
                    }

                    PlayerManager.addCooldown(p);
                }
            } else {
                if (MyWarp.isWarpcostsEnabled() && WarpCosts.WARP.isActive()) {
                    if (WarpCostsManager.hasEnougtFor(p, WarpCosts.WARP)) {
                        WarpCostsManager.removeWarpCoins(p, WarpCosts.WARP.getCosts());
                    } else {
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.WARP.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                        return true;
                    }
                }
            }

            if (MyWarp.isDelayEnabled()) {

                WarpDelay warpDelay = new WarpDelay(p, warp);
                warpDelay.start();

                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.beforedelayedwarping").replaceAll("%time%", MyWarp.getTeleportDelay().toString()));

            } else {
                warp.teleport(p);
            }
            return true;
        } else {

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
}
