package de.imolli.mywarp.warp.gui;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.utils.SQLHandle;
import de.imolli.mywarp.utils.SimpleLore;
import de.imolli.mywarp.warp.LoadError;
import de.imolli.mywarp.warp.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;

public class WarpFixGUI implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        if (e.getClickedInventory() == null) { //Check if clicked inventory equals null
            return;
        }

        Player p = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equalsIgnoreCase(MessageManager.getMessage("MyWarp.warpfix.gui.title.warpfix.menu"))) {
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.PAPER) {

                WarpGui.playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                openDeleteConfirmInv(p, getKeyFromItem(e.getCurrentItem()));

            }
        }

        if (e.getView().getTitle().startsWith(MessageManager.getMessage("MyWarp.warpfix.gui.title.warpfix.confirm"))) {
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) { //Confirm

                WarpGui.playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                p.closeInventory();

                String key = getKeyFromItem(e.getClickedInventory().getItem(4));

                SQLHandle.update("DELETE FROM `warps` WHERE `name` = '" + key + "';");
                WarpManager.removeFailedWarp(key);
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("Mywarp.warpfix.msg.confirm").replaceAll("%name%", key));

            }

            if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) { //Cancel

                WarpGui.playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                p.closeInventory();

            }
        }

    }

    public static void openGUI(Player p) {

        Inventory inv = Bukkit.createInventory(null, 45, MessageManager.getMessage("MyWarp.warpfix.gui.title.warpfix.menu"));
        HashMap<String, LoadError> failedWarps = WarpManager.getFailedWarps();

        ItemStack nowarps = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta nowarpsMeta = (SkullMeta) nowarps.getItemMeta();
        nowarpsMeta.setOwner("MHF_Question");
        nowarpsMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.nowarps"));
        nowarps.setItemMeta(nowarpsMeta);

        if (!failedWarps.isEmpty()) {
            for (String name : failedWarps.keySet()) {

                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§e" + name);

                String errorMsg = "";

                if (failedWarps.get(name) == LoadError.WorldDoNotExist) {
                    errorMsg = MessageManager.getMessage("MyWarp.warpfix.gui.error.worlddonotexist").replaceAll("%world%", "world");
                } else if (failedWarps.get(name) == LoadError.LocationIsWrong) {
                    errorMsg = MessageManager.getMessage("MyWarp.warpfix.gui.error.locationiswrong");
                }

                meta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warpfix.gui.lore.warp").replaceAll("%error%", errorMsg) + "\n§9" + name).getLore());
                item.setItemMeta(meta);
                inv.addItem(item);

            }

        } else {

            inv.setItem(22, nowarps);

        }

        Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), () -> p.openInventory(inv), 2);

    }

    private static void openDeleteConfirmInv(Player p, String key) {

        Inventory inv = Bukkit.createInventory(null, 36, MessageManager.getMessage("MyWarp.warpfix.gui.title.warpfix.confirm") + key);

        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MessageManager.getMessage("MyWarp.warpfix.gui.item.information"));
        infoMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warpfix.gui.lore.delete.info").replaceAll("%key%", key) + "\n§9" + key).getLore());
        info.setItemMeta(infoMeta);

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(MessageManager.getMessage("MyWarp.warpfix.gui.item.confirm"));
        confirmMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warpfix.gui.lore.delete.confirm")).getLore());
        confirm.setItemMeta(confirmMeta);

        ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(MessageManager.getMessage("MyWarp.warpfix.gui.item.cancel"));
        cancelMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warpfix.gui.lore.delete.cancel")).getLore());
        cancel.setItemMeta(cancelMeta);

        inv.setItem(4, info);
        inv.setItem(20, confirm);
        inv.setItem(24, cancel);

        p.openInventory(inv);

    }

    private String getKeyFromItem(ItemStack item) {
        return item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).replaceAll("§9", "");
    }

}
