package de.ImOlli.listeners;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.mywarp.MyWarp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {

        Player p = e.getPlayer();

        if (MyWarp.getWarpSigns()) {
            if (e.getLine(1).equalsIgnoreCase("[Warp]")) {
                if (!e.getLine(2).equalsIgnoreCase("") && !e.getLine(2).equalsIgnoreCase(" ")) {

                    e.setLine(1, "§8[§aWarp§8]");

                    String warpname = e.getLine(2);

                    if (WarpManager.existWarp(warpname)) {
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.sign.success").replaceAll("%name%", warpname));
                    } else {
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.notexist").replaceAll("%name%", warpname));
                    }
                } else {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.sign.empty"));
                }
            }
        }
    }
}
