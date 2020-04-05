package me.libraryaddict.gearwars.abilities;

import java.util.List;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Zombie extends AbilityListener implements Disableable {
    @EventHandler
    public void onPlayerHurt(EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (hasAbility(player)) {
                List<Entity> entity = player.getNearbyEntities(5, 3, 5);
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0), true);
                for (Entity ent : entity) {
                    if (ent instanceof Player) {
                        ((Player) ent).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0), true);
                    }
                }
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
