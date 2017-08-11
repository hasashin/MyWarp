package de.ImOlli.listeners;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.PlayerManager;
import de.ImOlli.mywarp.MyWarp;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if ((((int) e.getFrom().getX()) == ((int) e.getTo().getX())) && (((int) e.getFrom().getZ()) == ((int) e.getTo().getZ()))) {
            return;
        }

        if (MyWarp.getCancelTeleportOnMove()) {
            if (PlayerManager.isCurrentlyWarping(p) && !PlayerManager.getWarpDelay(p).isCanceled()) {

                PlayerManager.getWarpDelay(p).cancel();

                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.movecancel"));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);

                PlayerManager.removeWarpDelay(p);

            }
        }

    }
}
