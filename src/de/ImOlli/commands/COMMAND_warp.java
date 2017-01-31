package de.ImOlli.commands;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.mywarp.MyWarp;
import de.ImOlli.objects.Warp;
import de.ImOlli.objects.WarpGui;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

            if (MyWarp.getPlayParticleOnTeleport()) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.spawnParticle(Particle.PORTAL, p.getLocation(), 100);
                }
            }

            warp.teleport(p);
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.msg").replaceAll("%name%", warp.getName()));

            if (MyWarp.getPlaySoundOnTeleport()) {
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 20, 20);
            }

            if (MyWarp.getPlayParticleOnTeleport()) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.spawnParticle(Particle.PORTAL, p.getLocation(), 100);
                }
            }
            return true;
        } else {
            WarpGui.openWarpTeleportGui(p, 1);
            return true;
        }
    }
}
