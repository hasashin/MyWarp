package de.ImOlli.managers;

import de.ImOlli.mywarp.MyWarp;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Cooldown {

    private Player p;
    private BukkitTask task;

    public Cooldown(Player p) {
        this.p = p;
    }

    public void start() {

        task = Bukkit.getScheduler().runTaskTimer(MyWarp.getPlugin(MyWarp.class), new Runnable() {
            @Override
            public void run() {

                Integer cooldown = PlayerManager.getCooldown(p);

                if (cooldown <= 0) {
                    task.cancel();
                } else {
                    PlayerManager.setCooldown(p, cooldown - 1);
                }

            }
        }, 20, 20);

    }
}
