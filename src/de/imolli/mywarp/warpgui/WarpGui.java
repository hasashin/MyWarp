package de.imolli.mywarp.warpgui;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.warp.Warp;
import de.imolli.mywarp.warp.WarpManager;
import net.wesjd.anvilgui.AnvilGUI;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class WarpGui implements Listener {

    private enum WarpGUIType {
        Teleport,
        Delete;
    }

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

            if (e.getCurrentItem().getType() == Material.COMPASS) {

                if (e.getClick().isLeftClick()) {
                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                    openSearchGUI(p, WarpGUIType.Teleport);
                } else if (e.getClick().isRightClick()) {

                    Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 20, 20);
                    openWarpTeleportGui(p, currentpage, null);

                }
            }

            if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {

                String displayname = e.getCurrentItem().getItemMeta().getDisplayName();


                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.nextpage"))) {
                    try {

                        String keyword = getKeyword(e.getClickedInventory());

                        Integer size;

                        if (keyword != null) {
                            size = getSearchedWarps(keyword).size();
                        } else {
                            size = WarpManager.getWarps().size();
                        }

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                        Integer maxpages = size / 36;

                        if ((size - maxpages * 36) > 0) {
                            maxpages++;
                        }

                        if (currentpage.equals(maxpages)) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);
                            return;
                        }

                        openWarpTeleportGui(p, currentpage + 1, keyword);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }
                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.previouspage"))) {
                    try {

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                        if (currentpage == 1) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);
                            return;
                        }

                        openWarpTeleportGui(p, currentpage + -1, getKeyword(e.getClickedInventory()));
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

            if (e.getCurrentItem().getType() == Material.COMPASS) {

                if (e.getClick().isLeftClick()) {
                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                    openSearchGUI(p, WarpGUIType.Teleport);
                } else if (e.getClick().isRightClick()) {

                    Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 20, 20);
                    openWarpDeleteGui(p, currentpage, null);

                }
            }

            if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {

                String displayname = e.getCurrentItem().getItemMeta().getDisplayName();


                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.nextpage"))) {
                    try {

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                        Integer size = WarpManager.getWarps().size();
                        Integer maxpages = size / 36;

                        if ((size - maxpages * 36) > 0) {
                            maxpages++;
                        }

                        if (currentpage.equals(maxpages)) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);
                            return;
                        }

                        openWarpDeleteGui(p, currentpage + 1, null);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }
                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.previouspage"))) {
                    try {

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                        if (currentpage == 1) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 20, 20);
                            return;
                        }

                        openWarpDeleteGui(p, currentpage + -1, null);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }
            }
        }

    }

    public static void openWarpTeleportGui(Player p, Integer page, String keyword) {

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

            ArrayList<Warp> warps = getSearchedWarps(keyword);

            for (int i = 0; i < 36 * (page - 1) && warps.size() != 0; i++) {
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


        // TODO: Add Message String
        ItemStack sitem = new ItemStack(Material.COMPASS);
        if (keyword != null) sitem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        ItemMeta sitemMeta = sitem.getItemMeta();
        if (keyword != null) {
            sitemMeta.setDisplayName("§eSearch §8| §7Current: §e" + keyword);
            sitemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            sitemMeta.setDisplayName("§eSearch");
        }
        sitem.setItemMeta(sitemMeta);

        inv.setItem(39, sitem);

        Integer size = getSearchedWarps(keyword).size();
        Integer maxpages = size / 36;

        if ((size - maxpages * 36) > 0) {
            maxpages++;
        }

        if (maxpages < 1) maxpages = 1;

        ItemStack pitem = new ItemStack(Material.PAPER);
        ItemMeta pitemMeta = pitem.getItemMeta();
        pitemMeta.setDisplayName("§7Page: §e" + page + "§7/§e" + maxpages);
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

    public static void openWarpDeleteGui(Player p, Integer page, String keyword) {

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

            if (keyword != null) {
                for (Warp warp : warps) {
                    if (!warp.getName().contains(keyword)) {
                        warps.remove(warp);
                    }
                }
            }

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

        Integer size = WarpManager.getWarps().size();
        Integer maxpages = size / 36;

        if ((size - maxpages * 36) > 0) {
            maxpages++;
        }

        ItemStack pitem = new ItemStack(Material.PAPER);
        ItemMeta pitemMeta = pitem.getItemMeta();
        pitemMeta.setDisplayName("§7Page: §e" + page + "§7/§e" + maxpages);
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

    public static void openSearchGUI(Player p, WarpGUIType type) {

        new AnvilGUI(MyWarp.getPlugin(), p, "Search Here", (player, reply) -> {

            if (reply == null || reply.equals("")) {
                return "Search Here";
            }

            Bukkit.broadcastMessage(reply);

            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 20, 20);

            if (type.equals(WarpGUIType.Teleport)) {
                openWarpTeleportGui(p, 1, reply);
            } else if (type.equals(WarpGUIType.Delete)) {
                openWarpDeleteGui(p, 1, reply);
            }

            return null;
        });

    }

    private static ArrayList<Warp> getSearchedWarps(String keyword) {

        ArrayList<Warp> allwarps = WarpManager.getWarpList();
        ArrayList<Warp> warps = new ArrayList<>();

        if (keyword != null) {

            for (Warp warp : allwarps) {
                // TODO: Also search in lore
                if (warp.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    warps.add(warp);
                }
            }
        } else {
            warps = allwarps;
        }

        return warps;
    }

    private static String getKeyword(Inventory inv) {

        String keyword;
        if (inv.getItem(39).getEnchantments().isEmpty()) {
            keyword = null;
        } else {
            keyword = inv.getItem(39).getItemMeta().getDisplayName().replace("§eSearch §8| §7Current: §e", "");
        }

        return keyword;
    }
}
