package de.imolli.mywarp.warp;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.utils.MathUtils;
import de.imolli.mywarp.utils.SQLHandle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

public class WarpHologramManager implements Listener {

    private static ArrayList<WarpHologram> holograms;

    public static void init() {

        holograms = new ArrayList<>();

        removeOldHolograms();

        try {
            loadHolograms();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (WarpHologram hologram : holograms) hologram.spawn();

    }

    private static void removeOldHolograms() { //TODO: Check

        int i = 0;

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                    ArmorStand as = (ArmorStand) entity;
                    if (as.hasMetadata("mywarp")) {
                        as.remove();
                        i++;
                    }
                }
            }
        }

        if (i != 0) {
            MyWarp.getPlugin().getLogger().log(Level.INFO, "Removed " + i + " old WarpHolograms...");

            if (i > 3) {
                MyWarp.getPlugin().getLogger().log(Level.INFO, "Seems like the server has crashed last time! Please shut down the server correctly. Else the MyWarp plugin can't save all data correctly.");
            }
        }

    }

    private static void loadHolograms() throws SQLException {

        ResultSet rs = SQLHandle.query("Select * FROM `holograms`");
        while (rs.next()) {

            String[] locRaw = rs.getString("location").split(":");
            Location loc = new Location(Bukkit.getWorld(locRaw[0]), Double.valueOf(locRaw[1]), Double.valueOf(locRaw[2]), Double.valueOf(locRaw[3]));
            WarpHologram hologram = new WarpHologram(WarpManager.getWarp(rs.getString("warp")), loc, rs.getInt("id"));

            holograms.add(hologram);
        }

        if (holograms.size() != 0)
            MyWarp.getPlugin().getLogger().log(Level.INFO, "Loaded " + holograms.size() + " holograms from database!");

    }

    @EventHandler
    public static void onInteract(PlayerArmorStandManipulateEvent e) {

        for (WarpHologram hologram : holograms) {
            if (e.getRightClicked().getEntityId() == hologram.getArmorStand().getEntityId()) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public static void onInteract(PlayerInteractAtEntityEvent e) {

        Player p = e.getPlayer();

        if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) {

            System.out.println(e.getRightClicked().getScoreboardTags());

            for (WarpHologram hologram : holograms) {
                if (hologram.isSpawned(hologram.getArmorStand2())) {
                    if (hologram.getArmorStand2().getEntityId() == e.getRightClicked().getEntityId()) {

                        confirmInteract(p, hologram);
                        e.setCancelled(true);
                    }
                }

                if (hologram.isSpawned(hologram.getArmorStand())) {

                    if (hologram.getArmorStand().getEntityId() == e.getRightClicked().getEntityId()) {

                        confirmInteract(p, hologram);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    private static void confirmInteract(Player p, WarpHologram hologram) {
        p.performCommand("warp " + hologram.getWarp().getName());
    }

    public static void addWarpHologram(Player p, Warp warp, Location location) {

        String x = String.valueOf(MathUtils.round(location.getX(), 1));
        String y = String.valueOf(MathUtils.round(location.getY(), 1));
        String z = String.valueOf(MathUtils.round(location.getZ(), 1));

        //TODO: Check if id throws sync errors.

        int id = 0;

        try {
            id = SQLHandle.query("SELECT `seq` FROM `sqlite_sequence` WHERE `name` = 'holograms';").getInt("seq");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SQLHandle.update("INSERT INTO `holograms` (`warp`,`location`, `active`) VALUES ('" + warp.getName() + "','" + location.getWorld().getName() + ":" + x + ":" + y + ":" + z + "', '1')");

        WarpHologram hologram = new WarpHologram(warp, location, id + 1);

        hologram.spawn();
        holograms.add(hologram);

    }

    public static void removeWarpHolograms(ArrayList<WarpHologram> hologramlist) {

        if (hologramlist == null) return;

        String query = "DELETE FROM `holograms` WHERE `id` = '" + hologramlist.get(0).getId() + "'";

        for (WarpHologram hologram : hologramlist) {

            if (hologram != null) {

                if (!hologramlist.get(0).equals(hologram)) query = query + " OR `id` = '" + hologram.getId() + "'";

                hologram.disappear();
                holograms.remove(hologram);
            }
        }

        System.out.println(query); //TODO: Remove debug message!

        SQLHandle.update(query);

    }

    public static void removeWarpHologram(WarpHologram hologram) {

        if (hologram == null) return;

        //TODO: Was wenn das hologram nicht mehr existiert?

        SQLHandle.update("DELETE FROM `holograms` WHERE `id` = '" + hologram.getId() + "'");

        hologram.disappear();
        holograms.remove(hologram);

    }


    public static void disappearAll() {

        for (WarpHologram hologram : holograms) {

            hologram.disappear();

        }
    }

    public static Boolean isHologram(Entity entity) {

        for (WarpHologram hologram : holograms) {
            if (hologram.isSpawned(hologram.getArmorStand2())) {
                if (hologram.getArmorStand2().getEntityId() == entity.getEntityId()) {
                    return true;
                }
            }
            if (hologram.isSpawned(hologram.getArmorStand())) {
                if (hologram.getArmorStand().getEntityId() == entity.getEntityId()) {

                    return true;
                }
            }
        }

        return false;
    }

    public static WarpHologram getHologram(Integer id) {

        for (WarpHologram hologram : holograms) {
            if (hologram.getId().equals(id)) {
                return hologram;
            }
        }

        return null;
    }

    public static WarpHologram getHologram(Entity entity) {

        for (WarpHologram hologram : holograms) {
            if (hologram.isSpawned(hologram.getArmorStand2())) {
                if (hologram.getArmorStand2().getEntityId() == entity.getEntityId()) {
                    return hologram;
                }
            }
            if (hologram.isSpawned(hologram.getArmorStand())) {
                if (hologram.getArmorStand().getEntityId() == entity.getEntityId()) {
                    return hologram;
                }
            }
        }

        return null;
    }
}
