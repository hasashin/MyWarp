package de.imolli.mywarp.warpgui;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.warp.Warp;
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

import java.util.ArrayList;
import java.util.List;

public class WarpGui implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        if (e.getClickedInventory() == null) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
//Teleport WarpGUI======================================================================================================
        if (e.getClickedInventory().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.warp"))) {
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.EMPTY_MAP) {

                String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§e", "");

                p.closeInventory();
                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                p.performCommand("warp " + name.toLowerCase());

            }

            if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {

                String displayname = e.getCurrentItem().getItemMeta().getDisplayName();


                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.nextpage"))) {
                    try {

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().replace("§7Page: §e", ""));

                        Integer size = WarpManager.getWarps().size();
                        Integer maxpages = size / 36;

                        if ((size - maxpages * 36) > 0) {
                            maxpages++;
                        }

                        if (currentpage.equals(maxpages)) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);
                            return;
                        }

                        openWarpTeleportGui(p, currentpage + 1);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }
                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.previouspage"))) {
                    try {

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().replace("§7Page: §e", ""));

                        if (currentpage == 1) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);
                            return;
                        }

                        openWarpTeleportGui(p, currentpage + -1);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }
            }
        }
//Delete WarpGUI========================================================================================================
        if (e.getClickedInventory().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.delete"))) {
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.EMPTY_MAP) {

                String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§e", "");

                p.closeInventory();
                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                p.performCommand("delwarp " + name.toLowerCase());

            }

            if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {

                String displayname = e.getCurrentItem().getItemMeta().getDisplayName();


                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.nextpage"))) {
                    try {

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().replace("§7Page: §e", ""));

                        Integer size = WarpManager.getWarps().size();
                        Integer maxpages = size / 36;

                        if ((size - maxpages * 36) > 0) {
                            maxpages++;
                        }

                        if (currentpage.equals(maxpages)) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);
                            return;
                        }

                        openWarpDeleteGui(p, currentpage + 1);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }
                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.previouspage"))) {
                    try {

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().replace("§7Page: §e", ""));

                        if (currentpage == 1) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);
                            return;
                        }

                        openWarpDeleteGui(p, currentpage + -1);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }
            }
        }
    }

    public static void openWarpTeleportGui(Player p, Integer page) {

        Inventory inv = Bukkit.createInventory(null, 9 * 5, MessageManager.getMessage("MyWarp.warp.gui.title.warp"));

        if (WarpManager.getWarps().isEmpty()) {

            ItemStack nowarps = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta nowarpsMeta = (SkullMeta) nowarps.getItemMeta();
            nowarpsMeta.setOwner("MHF_Question");
            nowarpsMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.nowarps"));
            nowarps.setItemMeta(nowarpsMeta);

            inv.setItem(22, nowarps);

            Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), () -> p.openInventory(inv), 2);
            return;

        } else {

            ArrayList<Warp> warps = WarpManager.getWarpList();

            for (int i = 0; i < 36 * (page - 1) || warps.size() == 0; i++) {
                warps.remove(0);
            }

            Integer amount = 0;

            for (Warp warp : warps) {

                amount++;

                if (amount == 37) {
                    break;
                }

                ItemStack item = new ItemStack(Material.EMPTY_MAP);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§e" + warp.getName());
                List<String> lore = new ArrayList<>();
                String[] loreRaw = MessageManager.getMessage("MyWarp.warp.gui.lore.warp").replace("%name%", warp.getCreator()).split("\n");

                for (String string : loreRaw) {
                    lore.add(string);
                }

                meta.setLore(lore);
                item.setItemMeta(meta);

                inv.addItem(item);

            }
        }

        ItemStack previousPage = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta previousPageMeta = (SkullMeta) previousPage.getItemMeta();
        previousPageMeta.setOwner("MHF_ArrowLeft");
        previousPageMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.previouspage"));
        previousPage.setItemMeta(previousPageMeta);

        inv.setItem(36, previousPage);

        ItemStack pitem = new ItemStack(Material.PAPER);
        ItemMeta pitemMeta = pitem.getItemMeta();
        pitemMeta.setDisplayName("§7Page: §e" + page);
        pitem.setItemMeta(pitemMeta);

        inv.setItem(40, pitem);

        ItemStack nextPage = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta nextPageMeta = (SkullMeta) nextPage.getItemMeta();
        nextPageMeta.setOwner("MHF_ArrowRight");
        nextPageMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.nextpage"));
        nextPage.setItemMeta(nextPageMeta);

        inv.setItem(44, nextPage);

        Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), () -> p.openInventory(inv), 2);

    }

    public static void openWarpDeleteGui(Player p, Integer page) {

        Inventory inv = Bukkit.createInventory(null, 9 * 5, MessageManager.getMessage("MyWarp.warp.gui.title.delete"));

        if (WarpManager.getWarps().isEmpty()) {

            ItemStack nowarps = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta nowarpsMeta = (SkullMeta) nowarps.getItemMeta();
            nowarpsMeta.setOwner("MHF_Question");
            nowarpsMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.nowarps"));
            nowarps.setItemMeta(nowarpsMeta);

            inv.setItem(22, nowarps);

            Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), () -> p.openInventory(inv), 2);
            return;

        } else {

            ArrayList<Warp> warps = WarpManager.getWarpList();

            for (int i = 0; i < 36 * (page - 1) || warps.size() == 0; i++) {
                warps.remove(0);
            }

            Integer amount = 0;

            for (Warp warp : warps) {

                amount++;

                if (amount == 37) {
                    break;
                }

                ItemStack item = new ItemStack(Material.EMPTY_MAP);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§e" + warp.getName());
                List<String> lore = new ArrayList<>();
                String[] loreRaw = MessageManager.getMessage("MyWarp.warp.gui.lore.delete").replace("%name%", warp.getCreator()).split("\n");

                for (String string : loreRaw) {
                    lore.add(string);
                }

                meta.setLore(lore);
                item.setItemMeta(meta);

                inv.addItem(item);

            }
        }

        ItemStack previousPage = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta previousPageMeta = (SkullMeta) previousPage.getItemMeta();
        previousPageMeta.setOwner("MHF_ArrowLeft");
        previousPageMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.previouspage"));
        previousPage.setItemMeta(previousPageMeta);

        inv.setItem(36, previousPage);

        ItemStack pitem = new ItemStack(Material.PAPER);
        ItemMeta pitemMeta = pitem.getItemMeta();
        pitemMeta.setDisplayName("§7Page: §e" + page);
        pitem.setItemMeta(pitemMeta);

        inv.setItem(40, pitem);

        ItemStack nextPage = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta nextPageMeta = (SkullMeta) nextPage.getItemMeta();
        nextPageMeta.setOwner("MHF_ArrowRight");
        nextPageMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.nextpage"));
        nextPage.setItemMeta(nextPageMeta);

        inv.setItem(44, nextPage);

        Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), () -> p.openInventory(inv), 2);

    }
}
