package de.imolli.mywarp.commands;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.warp.Warp;
import de.imolli.mywarp.warp.WarpManager;
import de.imolli.mywarp.warpcosts.WarpCosts;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_warps implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageManager.getMessage("MyWarp.console.notplayer"));
            return true;
        }

        Player p = (Player) cs;

        if (MyWarp.isOnlyOp() && !p.isOp()) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
            return true;
        }

        if (!p.hasPermission("MyWarp.warps")) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("Warp.noperm.msg"));
            return false;
        }

        if (MyWarp.isWarpcostsEnabled() && WarpCosts.LISTWARPS.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore")) {
            if (WarpCostsManager.hasEnoughFor(p, WarpCosts.LISTWARPS)) {
                WarpCostsManager.removeWarpCoins(p, WarpCosts.LISTWARPS.getCosts());
            } else {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcosts.notenough").replaceAll("%amount%", WarpCosts.LISTWARPS.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                return true;
            }
        }

        TextComponent warps = new TextComponent("");
        for (Warp warp : WarpManager.getWarps().values()) {

            TextComponent warpComponent = new TextComponent("§7" + warp.getName());
            warpComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warp.getName()));
            warpComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Warp to §e" + warp.getName()).create()));

            if (warps.toPlainText().equals("")) {
                warps.addExtra(warpComponent);
            } else {
                TextComponent seperator = new TextComponent("§7, ");

                warps.addExtra(seperator);
                warps.addExtra(warpComponent);
            }
        }

        if (warps.toPlainText().equals("")) {
            warps = new TextComponent("§7-");
        }

        TextComponent msg = new TextComponent(MyWarp.getPrefix() + "§7");
        msg.addExtra(warps);

        if (MyWarp.isWarpcostsEnabled() && WarpCosts.LISTWARPS.isActive() && !p.hasPermission("MyWarp.warpcosts.ignore")) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warps.msgwithwarpcosts").replaceAll("%amount%", WarpCosts.LISTWARPS.getCosts().toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
        }

        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warps.msg").replace("%warps%", "" + WarpManager.getWarps().size()));
        p.spigot().sendMessage(msg);

        return true;
    }
}
