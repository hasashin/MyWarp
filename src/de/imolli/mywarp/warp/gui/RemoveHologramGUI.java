package de.imolli.mywarp.warp.gui;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.utils.MathUtils;
import de.imolli.mywarp.utils.SimpleLore;
import de.imolli.mywarp.warp.WarpHologram;
import de.imolli.mywarp.warp.WarpHologramManager;
import de.imolli.mywarp.warpcosts.WarpCosts;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoveHologramGUI implements Listener {

    public static void openGUI(Player p, ArrayList<WarpHologram> holograms) {

        Inventory inv = Bukkit.createInventory(null, 45, MessageManager.getMessage("MyWarp.warp.gui.title.removeholograms"));

        ItemStack itemCancel = new ItemStack(Material.BARRIER);
        ItemMeta itemCancelMeta = itemCancel.getItemMeta();
        itemCancelMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.cancel"));
        itemCancelMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.hologram.cancel")).getLore());
        itemCancel.setItemMeta(itemCancelMeta);

        ItemStack itemInfo = new ItemStack(Material.PAPER);
        ItemMeta itemInfoMeta = itemInfo.getItemMeta();
        itemInfoMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.information"));
        itemInfoMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.hologram.information")).getLore());
        itemInfo.setItemMeta(itemInfoMeta);

        ItemStack itemRemove = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta itemRemoveMeta = itemRemove.getItemMeta();
        itemRemoveMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.removeholograms"));
        itemRemoveMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.hologram.removeholograms")).getLore());
        itemRemove.setItemMeta(itemRemoveMeta);

        for (WarpHologram hologram : holograms) {
            ItemStack item = new ItemStack(Material.ARMOR_STAND);
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.hologram").replaceAll("%id%", hologram.getId().toString()));

            String location = MathUtils.round(hologram.getLocation().getX(), 1) + " " + MathUtils.round(hologram.getLocation().getY(), 1) + " " + MathUtils.round(hologram.getLocation().getZ(), 1);

            meta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.hologram.hologram.deselected").replaceAll("%warp%", hologram.getWarp().getName()).replaceAll("%location%", location) + "\n§9" + hologram.getId()).getLore());
            item.setItemMeta(meta);
            inv.addItem(item);
        }

        inv.setItem(36, itemCancel);
        inv.setItem(40, itemInfo);
        inv.setItem(44, itemRemove);

        p.openInventory(inv);

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        if (e.getClickedInventory() == null) { //Check if clicked inventory equals null
            return;
        }

        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory().getTitle().equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.title.removeholograms"))) {
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.BARRIER) {

                WarpGui.playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);

                p.closeInventory();
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.hologram.remove.cancel"));
                return;
            }

            if (e.getCurrentItem().getType() == Material.LAVA_BUCKET) {

                WarpGui.playGUISound(p, p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 20, 20);

                HashMap<Integer, ? extends ItemStack> items = e.getClickedInventory().all(Material.ARMOR_STAND);
                ArrayList<WarpHologram> holograms = new ArrayList<>();

                p.closeInventory();

                Integer count = 0;

                //TODO: Improve performance!

                for (ItemStack item : items.values()) {
                    if (!item.getEnchantments().isEmpty()) {
                        holograms.add(getHologramFromItem(item));
                    }
                }

                count = holograms.size();

                if (MyWarp.isWarpcostsEnabled() && WarpCosts.DELETEHOLOGRAM.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore")) {
                    if (WarpCostsManager.hasEnoughFor(p, WarpCosts.DELETEHOLOGRAM, holograms.size())) {
                        WarpCostsManager.removeWarpCoins(p, WarpCosts.DELETEHOLOGRAM.getCosts() * holograms.size());
                    } else {
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", ((Double) (WarpCosts.DELETEHOLOGRAM.getCosts() * holograms.size())).toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                        return;
                    }
                }

                WarpHologramManager.removeWarpHolograms(holograms);

                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.hologram.remove.multi").replaceAll("%count%", count.toString()));
            }

            if (e.getCurrentItem().getType() == Material.ARMOR_STAND) {

                WarpGui.playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                if (e.getCurrentItem().getEnchantments().isEmpty()) {
                    e.getCurrentItem().addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);

                    WarpHologram hologram = getHologramFromItem(e.getCurrentItem());
                    String location = MathUtils.round(hologram.getLocation().getX(), 1) + " " + MathUtils.round(hologram.getLocation().getY(), 1) + " " + MathUtils.round(hologram.getLocation().getZ(), 1);
                    ItemMeta meta = e.getCurrentItem().getItemMeta();

                    meta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.hologram.hologram.selected").replaceAll("%warp%", hologram.getWarp().getName()).replaceAll("%location%", location) + "\n§9" + hologram.getId()).getLore());
                    e.getCurrentItem().setItemMeta(meta);

                    p.updateInventory();

                } else {
                    e.getCurrentItem().removeEnchantment(Enchantment.ARROW_DAMAGE);

                    WarpHologram hologram = getHologramFromItem(e.getCurrentItem());
                    String location = MathUtils.round(hologram.getLocation().getX(), 1) + " " + MathUtils.round(hologram.getLocation().getY(), 1) + " " + MathUtils.round(hologram.getLocation().getZ(), 1);
                    ItemMeta meta = e.getCurrentItem().getItemMeta();

                    meta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.hologram.hologram.deselected").replaceAll("%warp%", hologram.getWarp().getName()).replaceAll("%location%", location) + "\n§9" + hologram.getId()).getLore());
                    e.getCurrentItem().setItemMeta(meta);

                    p.updateInventory();
                }
            }
        }
    }

    private WarpHologram getHologramFromItem(ItemStack item) {

        try {
            Integer id = Integer.parseInt(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).replace("§9", ""));

            return WarpHologramManager.getHologram(id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
