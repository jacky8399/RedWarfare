package me.libraryaddict.arcade.commands;

import me.libraryaddict.arcade.managers.ArcadeManager;
import me.libraryaddict.core.C;
import me.libraryaddict.core.command.SimpleCommand;
import me.libraryaddict.core.inventory.utils.ItemBuilder;
import me.libraryaddict.core.rank.Rank;
import me.libraryaddict.core.ranks.PlayerRank;
import me.libraryaddict.core.utils.UtilInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class CommandClearInventory extends SimpleCommand
{
    private ArcadeManager _arcadeManager;

    public CommandClearInventory(ArcadeManager arcadeManager)
    {
        super(new String[]
            {
                    "clearinventory", "clear", "clearinv", "ci"
            }, Rank.OWNER);

        _arcadeManager = arcadeManager;
    }

    @Override
    public void onTab(Player player, PlayerRank rank, String[] args, String token, Collection<String> completions)
    {
    }

    @Override
    public void runCommand(Player player, PlayerRank rank, String alias, String[] args)
    {
        Player target = player;
        Material toRemove = null;

        if (rank.hasRank(Rank.OWNER) && args.length > 0)
        {
            target = Bukkit.getPlayer(args[0]);

            if (target == null)
            {
                player.sendMessage(C.Red + "Unknown player '" + args[0] + "'");
                return;
            }

            args = Arrays.copyOfRange(args, 1, args.length);
        }

        if (args.length > 0)
        {
            if (Material.getMaterial(args[0].toUpperCase()) != null)
                toRemove = Material.getMaterial(args[0].toUpperCase());
            else
                player.sendMessage(C.Red + "Unknown item '" + args[0] + "'");
        }

        if (toRemove == null)
        {
            target.sendMessage(C.Blue + "Cleared inventory!");

            UtilInv.clearInventory(target);

            ItemStack openMaps = new ItemBuilder(Material.NETHER_STAR).setTitle(C.Gold + "Open Maps").build();

            target.getInventory().setItem(8, openMaps);
        }
        else
        {
            UtilInv.remove(target, toRemove);
        }
    }

}
