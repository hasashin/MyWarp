package de.ImOlli.listeners;

import de.ImOlli.managers.WarpManager;
import de.ImOlli.objects.Warp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerTabCompleteListener implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {

        System.out.println(e.getBuffer());

        String buffer = e.getBuffer().trim();

        if (buffer.equalsIgnoreCase("/mywarp")) {

            List<String> completions = new ArrayList<>();
            completions.add("reload");
            e.setCompletions(completions);

        } else if (buffer.equalsIgnoreCase("/warp")) {

            List<String> completions = new ArrayList<>();
            for (Warp warp : WarpManager.getWarps().values()) {
                completions.add(warp.getName());
            }
            e.setCompletions(completions);

        } else if (buffer.equalsIgnoreCase("/delwarp")) {

            List<String> completions = new ArrayList<>();
            for (Warp warp : WarpManager.getWarps().values()) {
                completions.add(warp.getName());
            }
            e.setCompletions(completions);

        } else if (buffer.equalsIgnoreCase("/warps")) {

            e.setCancelled(true);

        } else if (buffer.equalsIgnoreCase("/setwarp")) {

            e.setCancelled(true);

        }
    }
}
