package me.libraryaddict.gearwars.commands;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGoto implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Gamer gamer = GearApi.getPlayerManager().getGamer((Player) sender);
        if (gamer != null && !gamer.isAlive()) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "You need to define a playername!");
            } else {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + "Cannot find player " + args[0]);
                } else {
                    gamer.getPlayer().teleport(p.getPlayer());
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only spectators may teleport!");
        }
        return true;
    }
}
