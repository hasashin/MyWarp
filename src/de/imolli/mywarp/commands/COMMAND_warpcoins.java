package de.imolli.mywarp.commands;

import de.imolli.mywarp.MyWarp;
import de.imolli.mywarp.managers.MessageManager;
import de.imolli.mywarp.warpcosts.WarpCostsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_warpcoins implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageManager.getMessage("MyWarp.console.notplayer"));
            return true;
        }

        Player p = (Player) cs;

        if (!p.hasPermission("MyWarp.warpcoins.basic")) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
            return true;
        }

        if (MyWarp.isVaultEnabled()) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcoins.onlyavailable"));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcoins.showcoins").replaceAll("%amount%", WarpCostsManager.getBalance(p).toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
            return true;
        }

        if (args.length == 1) {
            p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/warpcoins <add|get|remove> <player> <amount>"));
            return true;
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("get")) {

                if (!p.hasPermission("MyWarp.warpcoins.get")) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
                    return true;
                }

                Player p2 = Bukkit.getPlayer(args[1]);

                if (p2 == null) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.notonline").replaceAll("%name%", args[1]));
                    return true;
                }

                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcoins.get").replaceAll("%name%", p2.getName()).replaceAll("%amount%", WarpCostsManager.getBalance(p2).toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                return true;
            } else {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/warpcoins <add|get|remove> <player> <amount>"));
                return true;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {

                if (!p.hasPermission("MyWarp.warpcoins.add")) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
                    return true;
                }

                Player p2 = Bukkit.getPlayer(args[1]);

                if (p2 == null) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.notonline").replaceAll("%name%", args[1]));
                    return true;
                }

                try {
                    Double value = Double.parseDouble(args[2]);

                    if (value <= 0) {
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.onlypositvenumbers"));
                        return true;
                    }

                    WarpCostsManager.addWarpCoins(p2, value);
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcoins.add").replaceAll("%name%", p2.getName()).replaceAll("%amount%", value.toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                    p2.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcoins.add2").replaceAll("%amount%", value.toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                    return true;

                } catch (Exception e) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.number"));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("remove")) {

                if (!p.hasPermission("MyWarp.warpcoins.remove")) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.noperm.msg"));
                    return true;
                }

                Player p2 = Bukkit.getPlayer(args[1]);

                if (p2 == null) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.notonline").replaceAll("%name%", args[1]));
                    return true;
                }

                try {
                    Double value = Double.parseDouble(args[2]);

                    if (value <= 0) {
                        p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.onlypositvenumbers"));
                        return true;
                    }

                    WarpCostsManager.removeWarpCoins(p2, value);
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcoins.remove").replaceAll("%name%", p2.getName()).replaceAll("%amount%", value.toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                    p2.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.warpcoins.remove2").replaceAll("%amount%", value.toString()).replaceAll("%currency%", WarpCostsManager.getCurrency()));
                    return true;


                } catch (Exception e) {
                    p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.error.number"));
                    return true;
                }
            } else {
                p.sendMessage(MyWarp.getPrefix() + MessageManager.getMessage("MyWarp.cmd.error").replaceAll("%cmd%", "/warpcoins <add|get|remove> <player> <amount>"));
                return true;
            }
        }

        return true;
    }
}
