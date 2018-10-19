package de.imolli.mywarp.listeners;

import de.imolli.mywarp.managers.PlayerManager;
import de.imolli.mywarp.utils.SQLHandle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public static void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        PlayerManager.unregisterPlayer(p);

        if (Bukkit.getOnlinePlayers().size() <= 1) {
            if (SQLHandle.isConnected()) {
                SQLHandle.disconnect();
            }
        }
    }
}
