package de.imolli.mywarp.listeners;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.warp.WarpManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    @EventHandler
    public static void onSignChange(SignChangeEvent e) {

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
