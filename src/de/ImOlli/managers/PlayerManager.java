package de.ImOlli.managers;

import de.ImOlli.mywarp.MyWarp;
import de.ImOlli.objects.WarpDelay;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.logging.Level;

public class PlayerManager {

    private static HashMap<Player, Integer> cooldowns;
    private static HashMap<Player, WarpDelay> warpdelays;

    public static void init() {

        cooldowns = new HashMap<>();
        warpdelays = new HashMap<>();

    }

    public static void registerPlayer(Player p) {
        if (!cooldowns.containsKey(p)) {
            cooldowns.put(p, 0);
        }
    }

    public static void unregisterPlayer(Player p) {
        if (cooldowns.containsKey(p)) {
            cooldowns.remove(p);
        }
    }

    public static void addCooldown(Player p) {

        if (cooldowns.containsKey(p)) {
            cooldowns.replace(p, MyWarp.getCooldown());

            new Cooldown(p).start();

        } else {
            registerPlayer(p);
        }

    }

    public static void setCooldown(Player p, Integer i) {

        if (cooldowns.containsKey(p)) {
            cooldowns.replace(p, i);
        } else {
            registerPlayer(p);
        }

    }

    public static void setWarpDelay(Player p, WarpDelay warpDelay) {

        if (!warpdelays.containsKey(p)) {
            warpdelays.put(p, warpDelay);
        }

    }

    public static void removeWarpDelay(Player p) {

        if (warpdelays.containsKey(p)) {
            warpdelays.remove(p);
        }

    }

    public static Boolean isCurrentlyWarping(Player p) {
        return warpdelays.containsKey(p);
    }

    public static Integer getCooldown(Player p) {

        if (cooldowns.containsKey(p)) {
            return cooldowns.get(p);
        } else {
            MyWarp.getPlugin(MyWarp.class).getLogger().log(Level.SEVERE, "Error while getting cooldown of player '" + p.getName() + "'");
            return 0;
        }

    }

    public static WarpDelay getWarpDelay(Player p) {

        if (warpdelays.containsKey(p)) {
            return warpdelays.get(p);
        } else {
            return null;
        }
    }
}
