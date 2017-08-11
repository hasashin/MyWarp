package de.ImOlli.objects;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.mywarp.MyWarp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Warp {

    private final String name;
    private Location location;
    private final String creator;

    public Warp(String name, Location loc, String creator) {
        this.name = name;
        this.location = loc;
        this.creator = creator;
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

        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.msg").replaceAll("%name%", this.getName()));

        if (MyWarp.getPlaySoundOnTeleport()) {
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 20, 20);
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
}
