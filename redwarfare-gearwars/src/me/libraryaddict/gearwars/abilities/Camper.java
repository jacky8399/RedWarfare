package me.libraryaddict.gearwars.abilities;

import java.util.HashMap;

import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.events.TimeSecondEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Camper extends AbilityListener implements Disableable {
    private class PlayerInfo {
        private Location last;
        private int secondsSince;
    }

    private HashMap<Player, PlayerInfo> playerInfo = new HashMap<Player, PlayerInfo>();

    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        playerInfo.remove(event.getKilled().getPlayer());
    }

    @EventHandler
    public void onSecond(TimeSecondEvent event) {
        for (Gamer gamer : getGamers()) {
            if (!playerInfo.containsKey(gamer.getPlayer())) {
                playerInfo.put(gamer.getPlayer(), new PlayerInfo());
                playerInfo.get(gamer.getPlayer()).last = gamer.getPlayer().getLocation();
            } else {
                PlayerInfo info = playerInfo.get(gamer.getPlayer());
                if (info.last.distance(gamer.getPlayer().getLocation()) < 1) {
                    info.secondsSince++;
                    if (info.secondsSince > 5) {
                        gamer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), true);
                    }
                } else {
                    info.secondsSince = 0;
                }
                info.last = gamer.getPlayer().getLocation();
            }
        }
    }

    @Override
    public void registerListener() {
    }

    @Override
    public void unregisterListener() {
    }

}
