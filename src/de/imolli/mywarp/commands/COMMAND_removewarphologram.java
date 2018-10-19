package de.imolli.mywarp.commands;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.utils.MathUtils;
import de.imolli.mywarp.warp.WarpHologram;
import de.imolli.mywarp.warp.WarpHologramManager;
import de.imolli.mywarp.warp.gui.RemoveHologramGUI;
import de.imolli.mywarp.warpcosts.WarpCosts;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class COMMAND_removewarphologram implements CommandExecutor {

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

        if (!p.hasPermission("MyWarp.warp.hologram.remove")) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("Warp.noperm.msg"));
            return false;
        }

        ArrayList<WarpHologram> holograms = new ArrayList<>();

        for (Entity entity : p.getNearbyEntities(3, 3, 3)) {
            if (WarpHologramManager.isHologram(entity)) {
                WarpHologram hologram = WarpHologramManager.getHologram(entity);
                if (!holograms.contains(hologram)) {
                    holograms.add(hologram);
                }
            }
        }

        if (holograms.size() > 1) {

            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.hologram.more"));

            RemoveHologramGUI.openGUI(p, holograms);
            return true;

        } else if (holograms.size() == 1) {

            if (MyWarp.isWarpcostsEnabled() && WarpCosts.DELETEHOLOGRAM.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore")) {
                if (WarpCostsManager.hasEnoughFor(p, WarpCosts.DELETEHOLOGRAM)) {
                    WarpCostsManager.removeWarpCoins(p, WarpCosts.DELETEHOLOGRAM.getCosts());
                } else {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.DELETEHOLOGRAM.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                    return true;
                }
            }

            WarpHologram hologram = holograms.get(0);
            Location loc = hologram.getLocation();

            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.hologram.remove.single").replaceAll("%location%", MathUtils.round(loc.getX(), 1) + " " + MathUtils.round(loc.getY(), 1) + " " + MathUtils.round(loc.getZ(), 1)));

            WarpHologramManager.removeWarpHologram(hologram);
            return true;
        } else {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.hologram.notfound"));
            return true;
        }
    }
}
