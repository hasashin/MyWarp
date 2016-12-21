package de.ImOlli.commands;

import de.ImOlli.managers.MessageManager;
import de.ImOlli.managers.WarpManager;
import de.ImOlli.mywarp.MyWarp;
import de.ImOlli.objects.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_warps implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("Du bist kein Spieler");
            return true;
        }

        Player p = (Player) cs;

        if (MyWarp.isOnlyOp() && !p.isOp()) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
            return true;
        }

        String warps = null;
        for (Warp warp : WarpManager.getWarps().values()) {
            if (warps == null) {
                warps = warp.getName();
            } else {
                warps = warps + ", " + warp.getName();
            }
        }
        if (warps == null) {
            warps = "-";
        }
        p.sendMessage(MyWarp.getPrefix() + "§7" + warps);

        return true;
    }
}
