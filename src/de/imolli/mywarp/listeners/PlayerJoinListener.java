package de.imolli.mywarp.listeners;

import de.imolli.mywarp.UpdateChecker;
import de.imolli.mywarp.managers.PlayerManager;
import de.imolli.mywarp.utils.SQLHandle;
import de.imolli.mywarp.warp.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        System.out.println(Bukkit.getOnlinePlayers().size());

        if (!SQLHandle.isConnected()) SQLHandle.connect();

        PlayerManager.registerPlayer(p);

        UpdateChecker.notifyPlayer(p);
        WarpManager.notifyPlayer(p);

    }

}
