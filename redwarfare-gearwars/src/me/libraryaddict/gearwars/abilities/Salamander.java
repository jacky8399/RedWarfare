package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Salamander extends AbilityListener implements Disableable {

    @EventHandler
    public void onFireOrLavaDmg(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (hasAbility(player)) {
                if (player.getRemainingAir() > 0
                        && (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.LAVA)) {
                    player.setFireTicks(0);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 10), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 0), true);
                    event.setCancelled(true);
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
