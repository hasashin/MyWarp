package de.imolli.mywarp.warp;

import de.imolli.mywarp.MyWarp;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class WarpHologram {

    private Warp warp;
    private Integer id;
    private Location location;
    private ArmorStand armorStand, armorStand2;

    public WarpHologram(Warp warp, Location location, Integer id) {
        this.warp = warp;
        this.location = location;
        this.id = id;
    }

    public Boolean isSpawned(ArmorStand armorStand) {
        return armorStand != null && !armorStand.isDead();
    }

    public void spawn() {

        if (armorStand != null) disappear();
        if (armorStand2 != null) disappear();

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.addScoreboardTag("MyWarp");
        armorStand.setCustomName("§7Click to teleport to");
        armorStand2.setMetadata("MyWarp", new FixedMetadataValue(MyWarp.getPlugin(), "mywarp"));
        armorStand.setGravity(false);

        armorStand2 = (ArmorStand) location.getWorld().spawnEntity(location.subtract(0, 0.3, 0), EntityType.ARMOR_STAND);

        armorStand2.setVisible(false);
        armorStand2.setCustomNameVisible(true);
        armorStand2.setCustomName("§e" + warp.getName());
        armorStand2.addScoreboardTag("MyWarp");
        //TODO: Add Metadata! Check
        armorStand2.setMetadata("MyWarp", new FixedMetadataValue(MyWarp.getPlugin(), "mywarp"));
        armorStand2.setGravity(false);

    }

    public void disappear() {

        if (isSpawned(armorStand)) {
            armorStand.remove();
            armorStand = null;
        }
        if (isSpawned(armorStand2)) {
            armorStand2.remove();
            armorStand2 = null;
        }
    }

    public Warp getWarp() {
        return warp;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public ArmorStand getArmorStand2() {
        return armorStand2;
    }

    public Location getLocation() {
        return location;
    }

    public Integer getId() {
        return id;
    }
}
