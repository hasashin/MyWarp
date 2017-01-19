package de.ImOlli.commands;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.mywarp.MyWarp;
import de.ImOlli.objects.Warp;
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
            cs.sendMessage("You are not an player!");
            return true;
        }

        Player p = (Player) cs;

        if (MyWarp.isOnlyOp() && !p.isOp()) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
            return true;
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

        p.sendMessage(MyWarp.getPrefix() + "§7List of all Warps (§e" + WarpManager.getWarps().size() + "§7)");
        p.spigot().sendMessage(msg);

        return true;
    }
}
