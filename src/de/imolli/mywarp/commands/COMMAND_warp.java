package de.imolli.mywarp.commands;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.managers.PlayerManager;
import de.imolli.mywarp.warp.Warp;
import de.imolli.mywarp.warp.WarpDelay;
import de.imolli.mywarp.warp.WarpManager;
import de.imolli.mywarp.warp.gui.WarpGui;
import de.imolli.mywarp.warp.warpflags.WarpFlag;
import de.imolli.mywarp.warpcosts.WarpCosts;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
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
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("Warp.noperm.msg"));
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

            if (warp.getFlags().contains(WarpFlag.ONLYCREATORCANWARP)) {
                if (!WarpManager.isCreator(warp, p)) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.notcreator"));
                    return true;
                }
            }

            if (warp.getFlags().contains(WarpFlag.ONLYPERMITTEDCANWARP)) {
                if (!WarpManager.isPermittedToWarp(p)) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.notpermittedtowarp"));
                    return true;
                }
            }

            if (MyWarp.isCooldownEnabled()) {
                if (PlayerManager.getCooldown(p) > 0) {

                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.cooldown").replaceAll("%time%", PlayerManager.getCooldown(p).toString()));
                    return true;

                } else {
                    if (MyWarp.isWarpcostsEnabled() && WarpCosts.WARP.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore") && !warp.getFlags().contains(WarpFlag.NOCOSTS)) {
                        if (WarpCostsManager.hasEnoughFor(p, WarpCosts.WARP)) {
                            WarpCostsManager.removeWarpCoins(p, WarpCosts.WARP.getCosts());
                        } else {
                            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.WARP.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                            return true;
                        }
                    }

                    PlayerManager.addCooldown(p);
                }
            } else {
                if (MyWarp.isWarpcostsEnabled() && WarpCosts.WARP.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore") && !warp.getFlags().contains(WarpFlag.NOCOSTS)) {
                    if (WarpCostsManager.hasEnoughFor(p, WarpCosts.WARP)) {
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

            if (MyWarp.isWarpcostsEnabled() && WarpCosts.LISTWARPS.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore")) {
                if (WarpCostsManager.hasEnoughFor(p, WarpCosts.LISTWARPS)) {
                    WarpCostsManager.removeWarpCoins(p, WarpCosts.LISTWARPS.getCosts());
                } else {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.LISTWARPS.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                    return true;
                }
            }

            WarpGui.openWarpGui(p, 1, null);
            return true;
        }
    }
}
