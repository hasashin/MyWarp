package de.ImOlli.listeners;

import de.ImOlli.mywarp.MyWarp;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (MyWarp.getWarpSigns()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {

                    Sign sign = (Sign) e.getClickedBlock().getState();

                    if (sign.getLine(1).equalsIgnoreCase("§8[§aWarp§8]")) {
                        if (!sign.getLine(2).equalsIgnoreCase("") && !sign.getLine(2).equalsIgnoreCase(" ")) {
                            String warpname = sign.getLine(2).toLowerCase();

                            p.performCommand("warp " + warpname);
                        }
                    }
                }
            }
        }
    }
}
