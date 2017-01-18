package de.ImOlli.listeners;

import de.ImOlli.mywarp.MyWarp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (MyWarp.getWarpSigns()) {
            if (e.getLine(1).equalsIgnoreCase("[Warp]")) {
                e.setLine(1, "§8[§aWarp§8]");
            }
        }
    }
}
