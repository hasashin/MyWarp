package de.imolli.mywarp.managers;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.warp.Cooldown;
import de.imolli.mywarp.warp.WarpDelay;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;

public class PlayerManager {

    private static HashMap<Player, Integer> cooldowns;
    private static HashMap<Player, WarpDelay> warpdelays;
    private static HashMap<Player, Integer> warplimits;

    public static void init() {

        cooldowns = new HashMap<>();
        warpdelays = new HashMap<>();
        warplimits = new HashMap<>();

    }

    public static void registerPlayer(Player p) {
        if (!cooldowns.containsKey(p)) {
            cooldowns.put(p, 0);
        }

        if (!warplimits.containsKey(p)) {
            setWarpLimit(p);
        }
    }

    public static void unregisterPlayer(Player p) {
        if (cooldowns.containsKey(p)) {
            cooldowns.remove(p);
        }

        if (warplimits.containsKey(p)) {
            warplimits.remove(p);
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

    public static Integer getWarpLimitViaPermission(Player p) {
        ArrayList<String> limits = new ArrayList<>();

        for (PermissionAttachmentInfo pio : p.getEffectivePermissions()) {
            String perm = pio.getPermission();

            if (perm.startsWith("mywarp.warp.limit.")) {
                String ending = perm.substring(perm.lastIndexOf(".") + 1, perm.length());
                limits.add(ending);

                System.out.println(perm);
                System.out.println(ending);
            }
        }

        if (limits.isEmpty()) {
            return -1;
        }

        Integer base = 0;

        if (limits.contains("basic")) {

            base = MyWarp.getDefaultLimit();
            limits.remove("basic");

            if (limits.size() == 1) {
                return base;
            }
        }

        ArrayList<Integer> limitsInt = new ArrayList<>();

        for (String s : limits) {
            limitsInt.add(Integer.parseInt(s));
        }

        Integer max = Collections.max(limitsInt);

        if (max > base) return max;
        else return base;

    }

    public static void setWarpLimit(Player p) {

        Integer limit = getWarpLimitViaPermission(p);
        System.out.println("Limit: " + limit);

        warplimits.put(p, limit);
    }

    public static Integer getWarpLimit(Player p) {
        return warplimits.getOrDefault(p, 0);
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
        return warpdelays.getOrDefault(p, null);
    }
}
