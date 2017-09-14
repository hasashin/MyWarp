package de.imolli.mywarp.warp;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class WarpDelay {

    private Player p;
    private Warp warp;
    private BukkitTask task;
    private Boolean canceled = false;

    public WarpDelay(Player p, Warp warp) {
        this.p = p;
        this.warp = warp;

        PlayerManager.setWarpDelay(p, this);

    }

    public void start() {

        task = Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), new Runnable() {
            @Override
            public void run() {
                if (!canceled) {
                    warp.teleport(p);
                }

                PlayerManager.removeWarpDelay(p);

            }
        }, 20 * MyWarp.getTeleportDelay());

    }

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
