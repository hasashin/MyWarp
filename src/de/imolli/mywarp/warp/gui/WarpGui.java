package de.imolli.mywarp.warp.gui;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.utils.SimpleLore;
import de.imolli.mywarp.warp.Warp;
import de.imolli.mywarp.warp.WarpManager;
import de.imolli.mywarp.warp.warpflags.WarpFlag;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

public class WarpGui implements Listener {

    @EventHandler
    public static void onInvClick(InventoryClickEvent e) {

        if (e.getClickedInventory() == null) { //Check if clicked inventory equals null
            return;
        }

        Player p = (Player) e.getWhoClicked();

        if (e.getView().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.menu"))) {

            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.MAP) {

                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                openOptionMenu(p, getWarpFromItem(e.getCurrentItem()));
                return;
            }

            if (e.getCurrentItem().getType() == Material.GUNPOWDER) {

                if (p.hasPermission("MyWarp.warp.showhiddenwarps")) {

                    playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                    openWarpInvisibleGui(p, 1);

                    return;
                } else {

                    playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);

                    return;
                }
            }

            if (e.getCurrentItem().getType() == Material.COMPASS) {

                if (e.getClick().isLeftClick()) {

                    playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                    openSearchGUI(p);

                } else if (e.getClick().isRightClick()) {

                    Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                    playGUISound(p, p.getLocation(), Sound.UI_BUTTON_CLICK, 20, 20);
                    openWarpGui(p, currentpage, null);

                }
            }

            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {

                String displayname = e.getCurrentItem().getItemMeta().getDisplayName();

                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.item.nextpage"))) {
                    try {

                        String keyword = getKeyword(e.getClickedInventory());
                        Integer size;

                        if (keyword != null) {
                            size = getSearchedWarps(keyword).size();
                        } else {
                            size = WarpManager.getFilteredWarps().size();
                        }

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));
                        Integer maxpages = size / 36;

                        if ((size - maxpages * 36) > 0) {
                            maxpages++;
                        }

                        if (currentpage.equals(maxpages) || currentpage > maxpages) {
                            playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
                            return;
                        }

                        openWarpGui(p, currentpage + 1, keyword);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }

                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.item.previouspage"))) {
                    try {

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                        if (currentpage == 1) {
                            playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
                            return;
                        }

                        openWarpGui(p, currentpage + -1, getKeyword(e.getClickedInventory()));
                        playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }
            }
        } else if (e.getView().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.selwarp"))) {

            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.MINECART) {
                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                openWarpGui(p, 1, null);
                return;
            }

            if (e.getCurrentItem().getType() == Material.COMPARATOR) {
                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                openModifyMenu(p, getSelectedWarp(e.getClickedInventory()));
                return;
            }

            if (e.getCurrentItem().getType() == Material.ENDER_PEARL) {
                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                Warp warp = getSelectedWarp(e.getClickedInventory());
                p.closeInventory();

                if (!checkWarpNullException(p, warp)) {
                    p.performCommand("warp " + warp.getName());
                    return;
                }
            }

            if (e.getCurrentItem().getType() == Material.BARRIER) {
                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                openDeleteConfirmGUI(p, getSelectedWarp(e.getClickedInventory()));
            }
        } else if (e.getView().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.modify"))) {

            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.MINECART) {
                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                openOptionMenu(p, getSelectedWarp(e.getClickedInventory()));
                return;
            }

            if (e.getCurrentItem().getType() == Material.NAME_TAG) {
                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                openRenameGUI(p, getSelectedWarp(e.getClickedInventory()));
                return;
            }

            if (e.getCurrentItem().getType() == Material.OAK_SIGN) {
                if (p.hasPermission("MyWarp.warp.modify.warpflags")) {
                    playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                    openWarpFlagsMenu(p, getSelectedWarp(e.getClickedInventory()));
                } else {
                    playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
                }
            }

        } else if (e.getView().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.rename"))) {

            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 20, 20);
                openModifyMenu(p, getSelectedWarp(e.getClickedInventory()));
                return;
            }

            if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                playGUISound(p, p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 20);

                Warp warp = getSelectedWarp(e.getClickedInventory());

                p.closeInventory();

                if (!checkWarpNullException(p, warp)) {
                    p.performCommand("warprename " + warp.getName() + " " + getRenamedName(e.getClickedInventory()));
                }
            }
        } else if (e.getView().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.delete"))) {

            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 20, 20);
                openOptionMenu(p, getSelectedWarp(e.getClickedInventory()));
                return;
            }

            if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                playGUISound(p, p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 20, 20);

                Warp warp = getSelectedWarp(e.getClickedInventory());

                if (warp == null) {
                    p.closeInventory();
                    p.performCommand("delwarp " + getSelectedWarpName(e.getClickedInventory()));
                    return;
                }

                p.closeInventory();
                p.performCommand("delwarp " + warp.getName());
            }
        } else if (e.getView().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.warpflags"))) {

            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.MINECART) {
                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);
                openModifyMenu(p, getSelectedWarpFromWarpFlagInv(e.getClickedInventory()));
                return;
            }

            if (e.getCurrentItem().getType() == Material.OAK_SIGN) {

                WarpFlag flag = getWarpFlagFromItem(e.getCurrentItem());
                Warp warp = getSelectedWarpFromWarpFlagInv(e.getClickedInventory());

                if (!p.hasPermission("MyWarp.warpflag." + flag.getName().toLowerCase())) {
                    playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
                    return;
                }

                playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                if (e.getCurrentItem().getEnchantments().isEmpty()) {

                    warp.addFlag(flag);
                    e.getCurrentItem().addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);

                    ItemMeta meta = e.getCurrentItem().getItemMeta();
                    meta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.warpflags.warpflag.selected") + "\n§9" + flag.getName().toUpperCase()).getLore());
                    e.getCurrentItem().setItemMeta(meta);
                    ((Player) e.getWhoClicked()).updateInventory();

                } else {

                    warp.removeFlag(flag);
                    e.getCurrentItem().removeEnchantment(Enchantment.ARROW_DAMAGE);

                    ItemMeta meta = e.getCurrentItem().getItemMeta();
                    meta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.warpflags.warpflag.deselected") + "\n§9" + flag.getName().toUpperCase()).getLore());
                    e.getCurrentItem().setItemMeta(meta);
                    ((Player) e.getWhoClicked()).updateInventory();

                }
            }
        } else if (e.getView().getTitle().startsWith(MessageManager.getMessage("MyWarp.warp.gui.title.invisible"))) {
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.MINECART) {

                playGUISound(p, p.getLocation(), Sound.UI_BUTTON_CLICK, 20, 20);
                openWarpGui(p, 1, null);
                return;
            }

            if (e.getCurrentItem().getType() == Material.MAP) {

                playGUISound(p, p.getLocation(), Sound.UI_BUTTON_CLICK, 20, 20);
                openOptionMenu(p, getWarpFromItem(e.getCurrentItem()));
                return;
            }

            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {

                String displayname = e.getCurrentItem().getItemMeta().getDisplayName();

                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.item.nextpage"))) {
                    try {

                        int size;

                        ArrayList<Warp> warps = new ArrayList<>();

                        for (Warp warp : WarpManager.getWarps().values()) {
                            if (warp.getFlags().contains(WarpFlag.GUIINVISIBLE)) {
                                warps.add(warp);
                            }
                        }

                        size = warps.size();

                        Integer currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));
                        Integer maxpages = size / 36;

                        if ((size - maxpages * 36) > 0) {
                            maxpages++;
                        }

                        if (currentpage.equals(maxpages) || currentpage > maxpages) {
                            playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
                            return;
                        }

                        openWarpInvisibleGui(p, currentpage + 1);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }

                if (displayname.equalsIgnoreCase(MessageManager.getMessage("MyWarp.warp.gui.item.previouspage"))) {
                    try {

                        int currentpage = Integer.parseInt(e.getClickedInventory().getItem(40).getItemMeta().getDisplayName().split("§7/")[0].replace("§7Page: §e", ""));

                        if (currentpage == 1) {
                            playGUISound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
                            return;
                        }

                        openWarpInvisibleGui(p, currentpage + -1);
                        playGUISound(p, p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20, 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.msg"));
                    }
                }

            }
        }
    }

    private static void openWarpFlagsMenu(Player p, Warp warp) {

        Inventory inv = Bukkit.createInventory(null, 9 * 4, MessageManager.getMessage("MyWarp.warp.gui.title.warpflags"));

        for (WarpFlag flag : WarpFlag.values()) {

            ItemStack itemFlag = new ItemStack(Material.OAK_SIGN);
            if (warp.getFlags().contains(flag)) itemFlag.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            ItemMeta itemFlagMeta = itemFlag.getItemMeta();
            itemFlagMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemFlagMeta.setDisplayName("§7" + flag.getDisplayname());

            if (p.hasPermission("MyWarp.warpflag." + flag.getName().toLowerCase())) {
                if (warp.getFlags().contains(flag)) {
                    itemFlagMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.warpflags.warpflag.selected") + "\n§9" + flag.getName().toUpperCase()).getLore());
                } else {
                    itemFlagMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.warpflags.warpflag.deselected") + "\n§9" + flag.getName().toUpperCase()).getLore());
                }
            } else {
                itemFlagMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.warpflags.noperm") + "\n§9" + flag.getName().toUpperCase()).getLore());
            }

            itemFlag.setItemMeta(itemFlagMeta);
            inv.addItem(itemFlag);
        }

        ItemStack back = new ItemStack(Material.MINECART);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.back"));
        back.setItemMeta(backMeta);

        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.information"));
        infoMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.warpflags.info").replaceAll("%name%", warp.getName()) + "\n§9" + warp.getName().toLowerCase()).getLore());
        info.setItemMeta(infoMeta);

        inv.setItem(27, back);
        inv.setItem(31, info);

        p.openInventory(inv);

    }

    private static void openModifyMenu(Player p, Warp warp) {

        if (checkWarpNullException(p, warp)) return;

        Inventory inv = Bukkit.createInventory(null, 9 * 3, MessageManager.getMessage("MyWarp.warp.gui.title.modify"));

        ItemStack info = new ItemStack(Material.MAP);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.information"));

        String location = ((int) warp.getLocation().getX()) + " " + ((int) warp.getLocation().getY()) + " " + ((int) warp.getLocation().getZ());

        infoMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.modify.info").replaceAll("%name%", warp.getName()).replaceAll("%creator%", warp.getCreator()).replaceAll("%location%", location) + "\n§9" + warp.getName().toLowerCase()).getLore());
        info.setItemMeta(infoMeta);

        ItemStack rename = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = rename.getItemMeta();
        renameMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.rename"));
        renameMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.modify.rename").replaceAll("%name%", warp.getName())).getLore());
        rename.setItemMeta(renameMeta);

        ItemStack flags = new ItemStack(Material.OAK_SIGN);
        ItemMeta flagsMeta = flags.getItemMeta();
        flagsMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.warpflags"));

        StringBuilder flagsMSG = new StringBuilder();
        String loreMSG = MessageManager.getMessage("MyWarp.warp.gui.lore.modify.warpflags");

        if (warp.getFlags().isEmpty()) {
            flagsMSG.append("§e-");
        } else {
            for (WarpFlag flag : warp.getFlags()) {
                flagsMSG.append("§e").append(flag.getDisplayname()).append("\n");
            }
        }

        flagsMSG.append("\n");

        flagsMeta.setLore(new SimpleLore(loreMSG.replaceAll("%flags%", flagsMSG.toString())).getLore());
        flags.setItemMeta(flagsMeta);

        ItemStack back = new ItemStack(Material.MINECART);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.back"));
        back.setItemMeta(backMeta);

        inv.setItem(4, info);
        inv.setItem(11, rename);
        inv.setItem(15, flags);
        inv.setItem(18, back);

        p.openInventory(inv);
    }

    private static void openOptionMenu(Player p, Warp warp) {

        if (checkWarpNullException(p, warp)) return;

        Inventory inv = Bukkit.createInventory(null, 9 * 3, MessageManager.getMessage("MyWarp.warp.gui.title.selwarp"));

        ItemStack info = new ItemStack(Material.MAP);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.information"));
        String location = ((int) warp.getLocation().getX()) + " " + ((int) warp.getLocation().getY()) + " " + ((int) warp.getLocation().getZ());

        infoMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.modify.info").replaceAll("%name%", warp.getName()).replaceAll("%creator%", warp.getCreator()).replaceAll("%location%", location) + "\n§9" + warp.getName().toLowerCase()).getLore());
        info.setItemMeta(infoMeta);

        ItemStack tp = new ItemStack(Material.ENDER_PEARL);
        ItemMeta tpM = tp.getItemMeta();
        tpM.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.warp"));
        tpM.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.option.teleport")).getLore());
        tp.setItemMeta(tpM);

        ItemStack settings = new ItemStack(Material.COMPARATOR);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.modify"));
        settingsMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.option.modify")).getLore());
        settings.setItemMeta(settingsMeta);

        ItemStack delete = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = delete.getItemMeta();
        deleteMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.delete"));
        deleteMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.option.delete")).getLore());
        delete.setItemMeta(deleteMeta);

        ItemStack back = new ItemStack(Material.MINECART);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.back"));
        back.setItemMeta(backMeta);

        inv.setItem(4, info);
        inv.setItem(11, settings);
        inv.setItem(13, tp);
        inv.setItem(15, delete);
        inv.setItem(18, back);

        p.openInventory(inv);
    }

    private static void openWarpInvisibleGui(Player p, Integer page) {

        Inventory inv = Bukkit.createInventory(null, 9 * 5, MessageManager.getMessage("MyWarp.warp.gui.title.invisible"));

        ArrayList<Warp> warps = new ArrayList<>();

        for (Warp warp : WarpManager.getWarps().values()) {
            if (warp.getFlags().contains(WarpFlag.GUIINVISIBLE)) {
                warps.add(warp);
            }
        }

        int warpAmount = warps.size();

        if (warps.isEmpty()) {

            ItemStack nowarps = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
            SkullMeta nowarpsMeta = (SkullMeta) nowarps.getItemMeta();
            nowarpsMeta.setOwner("MHF_Question");
            nowarpsMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.nowarps"));
            nowarps.setItemMeta(nowarpsMeta);

            inv.setItem(22, nowarps);

        } else {

            for (int i = 0; i < 36 * (page - 1) && warps.size() != 0; i++) {
                warps.remove(0);
            }

            int amount = 0;

            for (Warp warp : warps) {

                amount++;

                if (amount == 37) {
                    break;
                }

                ItemStack item = new ItemStack(Material.MAP);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§e" + warp.getName());
                meta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.warp").replace("%name%", warp.getCreator()) + "\n\n§9" + warp.getName()).getLore());
                item.setItemMeta(meta);

                inv.addItem(item);

            }
        }

        ItemStack back = new ItemStack(Material.MINECART);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.back"));
        back.setItemMeta(backMeta);

        inv.setItem(36, back);

        ItemStack previousPage = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta previousPageMeta = (SkullMeta) previousPage.getItemMeta();
        previousPageMeta.setOwner("MHF_ArrowLeft");
        previousPageMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.previouspage"));
        previousPage.setItemMeta(previousPageMeta);

        inv.setItem(43, previousPage);

        int maxpages = warpAmount / 36;

        if ((warpAmount - maxpages * 36) > 0) {
            maxpages++;
        }

        if (maxpages < 1) maxpages = 1;

        ItemStack pitem = new ItemStack(Material.PAPER);
        ItemMeta pitemMeta = pitem.getItemMeta();
        //TODO: Add Message for prefix page!
        pitemMeta.setDisplayName("§7Page: §e" + page + "§7/§e" + maxpages);
        pitem.setItemMeta(pitemMeta);

        inv.setItem(40, pitem);

        ItemStack nextPage = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta nextPageMeta = (SkullMeta) nextPage.getItemMeta();
        nextPageMeta.setOwner("MHF_ArrowRight");
        nextPageMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.nextpage"));
        nextPage.setItemMeta(nextPageMeta);

        inv.setItem(44, nextPage);

        p.openInventory(inv);

    }

    public static void openWarpGui(Player p, Integer page, String keyword) {

        //TODO: Load warps async

        Inventory inv = Bukkit.createInventory(null, 9 * 5, MessageManager.getMessage("MyWarp.warp.gui.title.menu"));

        if (WarpManager.getFilteredWarps().isEmpty()) {

            ItemStack nowarps = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
            SkullMeta nowarpsMeta = (SkullMeta) nowarps.getItemMeta();
            nowarpsMeta.setOwner("MHF_Question");
            nowarpsMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.nowarps"));
            nowarps.setItemMeta(nowarpsMeta);

            inv.setItem(22, nowarps);

            Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), () -> p.openInventory(inv), 2);

        } else {

            ArrayList<Warp> warps = getSearchedWarps(keyword);

            for (int i = 0; i < 36 * (page - 1) && warps.size() != 0; i++) {
                warps.remove(0);
            }

            int amount = 0;

            for (Warp warp : warps) {

                amount++;

                if (amount == 37) {
                    break;
                }

                ItemStack item = new ItemStack(Material.MAP);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§e" + warp.getName());
                meta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.warp").replace("%name%", warp.getCreator()) + "\n\n§9" + warp.getName()).getLore());
                item.setItemMeta(meta);

                inv.addItem(item);

            }
        }

        ItemStack previousPage = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta previousPageMeta = (SkullMeta) previousPage.getItemMeta();
        previousPageMeta.setOwner("MHF_ArrowLeft");
        previousPageMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.previouspage"));
        previousPage.setItemMeta(previousPageMeta);

        inv.setItem(43, previousPage);

        ItemStack search = new ItemStack(Material.COMPASS);
        if (keyword != null) search.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        ItemMeta searchMeta = search.getItemMeta();
        if (keyword != null) {
            searchMeta.setDisplayName("§eSearch §8| §7Current: §e" + keyword);
            searchMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            searchMeta.setDisplayName("§eSearch");
        }
        searchMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.search")).getLore());
        search.setItemMeta(searchMeta);

        inv.setItem(36, search);

        int size = getSearchedWarps(keyword).size();
        int maxpages = size / 36;

        if ((size - maxpages * 36) > 0) {
            maxpages++;
        }

        if (maxpages < 1) maxpages = 1;

        ItemStack pitem = new ItemStack(Material.PAPER);
        ItemMeta pitemMeta = pitem.getItemMeta();
        //TODO: Add Message for prefix page!
        pitemMeta.setDisplayName("§7Page: §e" + page + "§7/§e" + maxpages);
        pitem.setItemMeta(pitemMeta);

        inv.setItem(40, pitem);

        ItemStack invisibleWarps = new ItemStack(Material.GUNPOWDER);
        ItemMeta invisibleWarpsMeta = invisibleWarps.getItemMeta();
        invisibleWarpsMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.invisiblewarps"));
        invisibleWarps.setItemMeta(invisibleWarpsMeta);

        if (p.hasPermission("MyWarp.warp.showhiddenwarps")) {
            inv.setItem(39, invisibleWarps);
        }

        ItemStack nextPage = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta nextPageMeta = (SkullMeta) nextPage.getItemMeta();
        nextPageMeta.setOwner("MHF_ArrowRight");
        nextPageMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.nextpage"));
        nextPage.setItemMeta(nextPageMeta);

        inv.setItem(44, nextPage);

        Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), () -> p.openInventory(inv), 2);

    }

    private static void openRenameConfirmGUI(Player p, Warp warp, String name) {

        if (checkWarpNullException(p, warp)) return;

        Inventory inv = Bukkit.createInventory(null, 9 * 4, MessageManager.getMessage("MyWarp.warp.gui.title.rename"));

        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.information"));
        infoMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.rename.info").replaceAll("%name%", warp.getName()).replaceAll("%newname%", name) + "\n§9" + warp.getName().toLowerCase()).getLore());
        info.setItemMeta(infoMeta);

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.confirm"));
        confirmMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.rename.confirm").replaceAll("%name%", warp.getName()).replaceAll("%newname%", name)).getLore());
        confirm.setItemMeta(confirmMeta);

        ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.cancel"));
        cancelMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.rename.cancel").replaceAll("%name%", warp.getName()).replaceAll("%newname%", name)).getLore());
        cancel.setItemMeta(cancelMeta);

        inv.setItem(4, info);
        inv.setItem(20, confirm);
        inv.setItem(24, cancel);

        Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(MyWarp.class), () -> p.openInventory(inv), 3);

    }

    private static void openDeleteConfirmGUI(Player p, Warp warp) {

        if (checkWarpNullException(p, warp)) return;

        Inventory inv = Bukkit.createInventory(null, 9 * 4, MessageManager.getMessage("MyWarp.warp.gui.title.delete"));

        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.information"));
        infoMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.delete.info").replaceAll("%name%", warp.getName()) + "\n§9" + warp.getName().toLowerCase()).getLore());
        info.setItemMeta(infoMeta);

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.confirm"));
        confirmMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.delete.confirm").replaceAll("%name%", warp.getName())).getLore());
        confirm.setItemMeta(confirmMeta);

        ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(MessageManager.getMessage("MyWarp.warp.gui.item.cancel"));
        cancelMeta.setLore(new SimpleLore(MessageManager.getMessage("MyWarp.warp.gui.lore.delete.cancel").replaceAll("%name%", warp.getName())).getLore());
        cancel.setItemMeta(cancelMeta);

        inv.setItem(4, info);
        inv.setItem(20, confirm);
        inv.setItem(24, cancel);

        p.openInventory(inv);

    }

    private static void openSearchGUI(Player p) {

        new AnvilGUI(MyWarp.getPlugin(), p, MessageManager.getMessage("MyWarp.warp.gui.search.basic"), (player, reply) -> {

            if (reply == null || reply.equals("")) {
                return MessageManager.getMessage("MyWarp.warp.gui.search.basic");
            }

            playGUISound(p, p.getLocation(), Sound.UI_BUTTON_CLICK, 20, 20);
            openWarpGui(p, 1, reply);

            return null;
        });

    }

    private static void openRenameGUI(Player p, Warp warp) {

        new AnvilGUI(MyWarp.getPlugin(), p, warp.getName(), (player, reply) -> {

            if (reply == null || reply.equals("")) {
                return warp.getName();
            }

            if (reply.contains(" ")) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warp.nospaces"));
                return warp.getName();
            }

            playGUISound(p, p.getLocation(), Sound.UI_BUTTON_CLICK, 20, 20);
            openRenameConfirmGUI(p, warp, reply);

            return null;
        });

    }

    private static boolean checkWarpNullException(Player p, Warp warp) {

        if (warp == null) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.warp"));
            return true;
        } else {
            return false;
        }

    }

    public static void playGUISound(Player p, Location loc, Sound sound, Integer m1, Integer m2) {

        if (MyWarp.isGUISoundEnabled()) {
            p.playSound(loc, sound, m1, m2);
        }

    }

    private static ArrayList<Warp> getSearchedWarps(String keyword) {

        ArrayList<Warp> allwarps = WarpManager.getWarpList();
        ArrayList<Warp> warps = new ArrayList<>();

        for (Warp warp : allwarps) {
            if (!warp.getFlags().contains(WarpFlag.GUIINVISIBLE)) {
                if (keyword == null || warp.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    warps.add(warp);
                }
            }
        }

        return warps;
    }

    private static Warp getSelectedWarp(Inventory inv) {
        return WarpManager.getWarp(getSelectedWarpName(inv));
    }

    private static String getSelectedWarpName(Inventory inv) {
        return getWarpNameFromItem(inv.getItem(4));
    }

    private static Warp getSelectedWarpFromWarpFlagInv(Inventory inv) {

        ItemStack item = inv.getItem(31);

        String name = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1);
        name = name.replace("§9", "");

        return WarpManager.getWarp(name);
    }

    private static WarpFlag getWarpFlagFromItem(ItemStack item) {

        String name = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1);
        name = name.replace("§9", "");

        return WarpFlag.valueOf(name);
    }

    private static String getWarpNameFromItem(ItemStack item) {

        String name = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1);
        name = name.replace("§9", "");

        return name;
    }

    private static Warp getWarpFromItem(ItemStack item) {
        return WarpManager.getWarp(getWarpNameFromItem(item));
    }

    private static String getRenamedName(Inventory inv) {

        ItemStack item = inv.getItem(4);

        String name = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 2);
        name = name.replace("§9", "");

        return name;
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
