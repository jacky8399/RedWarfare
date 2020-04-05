package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Leech extends AbilityListener implements Disableable {
    @EventHandler
    public void onEntityDmg(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (hasAbility(player)) {
                double rawDmg = event.getDamage();
                rawDmg /= 3;
                double hp = player.getHealth() + rawDmg;
                if (hp > player.getMaxHealth())
                    hp = player.getMaxHealth();
                player.setHealth(hp);
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
