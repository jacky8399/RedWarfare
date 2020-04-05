package me.libraryaddict.gearwars.types;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbilityListener implements Listener {
    private ArrayList<Gamer> gamers = new ArrayList<Gamer>();

    public boolean isDisableable() {
        return this instanceof Disableable;
    }

    public ArrayList<Gamer> getGamers() {
        return new ArrayList<Gamer>(gamers);
    }

    public boolean hasAbility(Player player) {
        for (Gamer gamer : getGamers()) {
            if (gamer.getPlayer() == player)
                return true;
        }
        return false;
    }

    public void addGamer(Gamer gamer) {
        gamers.add(gamer);
    }

    public void removeGamer(Gamer gamer) {
        gamers.remove(gamer);
        for (DisplaySlot slot : this.scoreboard.keySet()) {
            for (String s : this.scoreboard.get(slot)) {
                ScoreboardManager.hideScore(gamer.getPlayer(), slot, s);
            }
        }
    }

    public boolean hasAbility(Gamer gamer) {
        return getGamers().contains(gamer);
    }

    private HashMap<DisplaySlot, ArrayList<String>> scoreboard = new HashMap<DisplaySlot, ArrayList<String>>();

    public void registerScoreboard(DisplaySlot slot, String string) {
        ArrayList<String> strings = new ArrayList<String>();
        if (scoreboard.containsKey(slot)) {
            strings = scoreboard.get(slot);
        } else {
            scoreboard.put(slot, strings);
        }
        strings.add(string);
    }

    public abstract void registerListener();

    public abstract void unregisterListener();
}
