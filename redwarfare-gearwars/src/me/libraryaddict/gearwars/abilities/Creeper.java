package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Creeper extends AbilityListener implements Disableable {

    @EventHandler
    public void onPlayerKilled(PlayerKilledEvent event) {
        if (hasAbility(event.getKilled())) {
            Player p = event.getKilled().getPlayer();
            p.getWorld().createExplosion(p.getLocation(), 8);
        }
    }

    @Override
    public void registerListener() {
    }

    @Override
    public void unregisterListener() {
    }

}
