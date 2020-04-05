package me.libraryaddict.arcade.commands;

import me.libraryaddict.arcade.managers.ArcadeManager;
import me.libraryaddict.core.C;
import me.libraryaddict.core.command.SimpleCommand;
import me.libraryaddict.core.rank.Rank;
import me.libraryaddict.core.ranks.PlayerRank;
import me.libraryaddict.core.utils.UtilInv;
import me.libraryaddict.core.utils.UtilItem;
import me.libraryaddict.core.utils.UtilNumber;
import me.libraryaddict.core.utils.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CommandGiveItem extends SimpleCommand
{
    private ArcadeManager _arcadeManager;

    public CommandGiveItem(ArcadeManager arcadeManager)
    {
        super(new String[]
            {
                    "give", "item", "i"
            }, Rank.OWNER);

        _arcadeManager = arcadeManager;
    }

    @Override
    public void onTab(Player player, PlayerRank rank, String[] args, String token, Collection<String> completions)
    {
        if (rank.hasRank(Rank.OWNER) && args.length == 0)
        {
            completions.addAll(getPlayers(token));

            if ("all".startsWith(token.toLowerCase()))
            {
                completions.add("All");
            }
        }

        if (args.length > 1)
            return;

        ArrayList<String> items = UtilItem.getCompletions(token, false);

        completions.addAll(items);
    }

    @Override
    public void runCommand(Player player, PlayerRank rank, String alias, String[] args) {
        if (args.length == 0) {
            player.sendMessage(C.Red + "/" + alias + " <ItemStack>");

            if (rank.hasRank(Rank.OWNER)) {
                player.sendMessage(C.Red + "/" + alias + " <Player> <ItemStack>");
            }

            return;
        }

        boolean all = args[0].equalsIgnoreCase("All");

        Player toReceive = args.length > 1 ? Bukkit.getPlayer(args[0]) : null;

        if (toReceive == null && !all)
            toReceive = player;
        else
            args = Arrays.copyOfRange(args, 1, args.length);

        if (toReceive != player && !rank.hasRank(Rank.OWNER)) {
            player.sendMessage(C.Red + "You are not allowed to do that");
            return;
        }

        if (args.length > 2) {
            player.sendMessage(C.Red + "Too many arguments were given!");
            return;
        }

        Material item = Material.getMaterial(args[0].toUpperCase());

        int amount;

        if (item == null) {
            player.sendMessage(C.Red + "Unable to find the item " + args[0]);
            return;
        }

        if (args.length > 1) {
            if (!UtilNumber.isParsableInt(args[1])) {
                player.sendMessage(C.Red + "Cannot parse '" + args[1] + "' to a number!");
                return;
            } else {
                amount = Integer.parseInt(args[1]);
            }
        } else {
            amount = item.getMaxStackSize();
        }

        amount = Math.min(5000, amount);


        ItemStack itemstack = new ItemStack(item, amount);

        if (all) {
            for (Player p : UtilPlayer.getPlayers()) {
                UtilInv.addItem(p, itemstack);

                if (p == player)
                    continue;

                p.sendMessage(C.Blue + "Given " + item.toString() + " x " + amount);
            }

            player.sendMessage(C.Blue + "Given everyone " + item.toString() + " x " + amount);
        } else {
            UtilInv.addItem(toReceive, itemstack);

            toReceive.sendMessage(C.Blue + "Given " + item.toString() + " x " + amount
                    + (player != toReceive ? " by " + player.getName() : ""));

            if (player != toReceive) {
                player.sendMessage(C.Blue + "Given " + toReceive.getName() + " " + item.toString() + " x " + amount);
            }
        }
    }
}
