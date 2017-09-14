package de.ImOlli.commands;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.mywarp.MyWarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_mywarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if (cs instanceof Player) {
            Player p = (Player) cs;

            if (MyWarp.isOnlyOp() && !p.isOp()) {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
                return true;
            }

            if (!p.hasPermission("MyWarp.mywarp")) {
                return false;
            }
        }

        if (args.length == 0) {

            cs.sendMessage(" ");
            cs.sendMessage("    §aMyWarp §ev" + MyWarp.getPlugin().getDescription().getVersion() + " §7by §e" + MyWarp.getPlugin().getDescription().getAuthors().get(0));
            cs.sendMessage("    §7Description: §e" + MyWarp.getPlugin().getDescription().getDescription());
            cs.sendMessage(" ");

            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                MyWarp.reload();
                cs.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.reload"));
                return true;
            } else {
                cs.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/Mywarp [reload]"));
                return true;
            }
        } else {
            cs.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/Mywarp [reload]"));
            return true;
        }
    }
}
