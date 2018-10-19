package de.imolli.mywarp.warp;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.warp.warpflags.WarpFlag;
import de.imolli.mywarp.warpcosts.WarpCosts;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Warp {

    private String name;
    private Location location;
    private final String creator;
    private ArrayList<WarpFlag> flags;

    public Warp(String name, Location loc, String creator, ArrayList<WarpFlag> flags) {
        this.name = name;
        this.location = loc;
        this.creator = creator;

        if (flags == null) {
            this.flags = new ArrayList<>();
        } else {
            this.flags = flags;
        }
    }

    public void teleport(Player p) {

        if (MyWarp.getPlayParticleOnTeleport()) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.spawnParticle(Particle.PORTAL, p.getLocation(), 100);
            }
        }

        p.teleport(location);

        if (MyWarp.getPlayParticleOnTeleport()) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.spawnParticle(Particle.PORTAL, p.getLocation(), 100);
            }
        }

        if (MyWarp.isWarpcostsEnabled() && WarpCosts.WARP.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore")) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.msgwithwarpcosts").replaceAll("%name%", this.getName()).replaceAll("%amount%", WarpCosts.WARP.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
        } else {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.msg").replaceAll("%name%", this.getName()));
        }

        if (MyWarp.getPlaySoundOnTeleport()) {
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 20, 20);
        }
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location loc) {
        this.location = loc;
    }

    public String getCreator() {
        return creator;
    }

    public ArrayList<WarpFlag> getFlags() {
        return flags;
    }

    public void addFlag(WarpFlag flag) {
        flags.add(flag);
        WarpManager.updateWarpFlags(this);
    }

    public void removeFlag(WarpFlag flag) {
        if (flags.contains(flag)) {
            flags.remove(flag);
            WarpManager.updateWarpFlags(this);
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
