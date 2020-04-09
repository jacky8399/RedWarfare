package me.libraryaddict.arcade.commands;

import me.libraryaddict.arcade.managers.GameManager;
import me.libraryaddict.core.C;
import me.libraryaddict.core.command.SimpleCommand;
import me.libraryaddict.core.rank.Rank;
import me.libraryaddict.core.ranks.PlayerRank;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandCredits extends SimpleCommand {

    private GameManager _gameManager;

    public CommandCredits(GameManager gameManager) {
        super("credits", Rank.ADMIN);
        _gameManager = gameManager;
    }

    @Override
    public void onTab(Player player, PlayerRank rank, String[] args, String token, Collection<String> completions) {

        if (args.length == 1) {
            List<String> strings = new ArrayList<>(Arrays.asList("setwin", "setloss", "setkill"));

            for (String string : strings) {
                if (string.startsWith(token.toLowerCase()))
                    completions.add(string);
            }

        }
    }

    @Override
    public void runCommand(Player player, PlayerRank rank, String alias, String[] args) {

        if (args.length < 2) {
            player.sendMessage(C.Red + "/credits <setwin/setloss/setkill> #");
            return;
        }

        try {
            if (args[0].equalsIgnoreCase("setwin")) {
                _gameManager.getGame().setCreditsWin(Integer.parseInt(args[1]));
                _gameManager.getGame().Announce(C.DGreen + "Winners of this match will receive " + args[1] + " credits!");
            }

            if (args[0].equalsIgnoreCase("setloss")) {
                _gameManager.getGame().setCreditsLoss(Integer.parseInt(args[1]));
                _gameManager.getGame().Announce(C.DGreen + "Losers of this match will receive " + args[1] + " credits!");
            }

            if (args[0].equalsIgnoreCase("setkill")) {
                _gameManager.getGame().setCreditsKill(Integer.parseInt(args[1]));
                _gameManager.getGame().Announce(C.DGreen + "Each kill of this match will grant " + args[1] + " credits!");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(C.Red + "Second arg must be numeric!");
        }

    }
}
