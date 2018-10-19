package de.imolli.mywarp.listeners;

import de.imolli.mywarp.warp.Warp;
import de.imolli.mywarp.warp.WarpManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerTabCompleteListener implements Listener {

    @EventHandler
    public static void onTabComplete(TabCompleteEvent e) {

        //TODO: Improve tab complete for /warp and /mywarp

        String buffer = e.getBuffer();

        if (buffer.equalsIgnoreCase("/mywarp ")) {

            List<String> completions = new ArrayList<>();
            completions.add("fix");
            completions.add("reload");
            completions.add("reset");
            e.setCompletions(completions);

        } else if (buffer.equalsIgnoreCase("/warpcoins ")) {

            List<String> completions = new ArrayList<>();
            completions.add("add");
            completions.add("get");
            completions.add("remove");
            e.setCompletions(completions);

        } else if (buffer.equalsIgnoreCase("/setwarphologram ")) {

            List<String> completions = new ArrayList<>();
            for (Warp warp : WarpManager.getWarps().values()) completions.add(warp.getName());
            e.setCompletions(completions);

        } else if (buffer.equalsIgnoreCase("/warp ")) {

            List<String> completions = new ArrayList<>();
            for (Warp warp : WarpManager.getWarps().values()) completions.add(warp.getName());
            e.setCompletions(completions);

        } else if (buffer.equalsIgnoreCase("/delwarp ")) {

            List<String> completions = new ArrayList<>();
            for (Warp warp : WarpManager.getWarps().values()) completions.add(warp.getName());
            e.setCompletions(completions);

        } else if (buffer.equalsIgnoreCase("/warps ")) {

            e.setCancelled(true);

        } else if (buffer.equalsIgnoreCase("/setwarp ")) {

            e.setCancelled(true);

        }
    }
}
