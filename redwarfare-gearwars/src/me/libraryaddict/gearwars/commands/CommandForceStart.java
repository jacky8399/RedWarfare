package me.libraryaddict.gearwars.commands;

import me.libraryaddict.gearwars.GearApi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandForceStart implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("gearwars.forcestart")) {
            if (GearApi.getGameManager().getGTime() < 0) {
                if (args.length == 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.setLevel(0);
                    }
                    GearApi.getGameManager().startGame();
                } else {
                    try {
                        int time = Integer.parseInt(args[0]);
                        GearApi.getGameManager().setTime(-Math.abs(time));
                        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " changed the time to "
                                + GearApi.getGameManager().getGTime(GearApi.getGameManager().getGTime()));
                    } catch (Exception ex) {
                        sender.sendMessage(ChatColor.RED + args[0] + " is not a number!");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "The game has already started!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "No permission!");
        }
        return true;
    }
}
