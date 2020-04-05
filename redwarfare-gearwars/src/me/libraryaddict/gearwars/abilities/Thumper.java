package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Thumper extends AbilityListener implements Disableable {

    @Override
    public void registerListener() {
    }

    @EventHandler
    public void thump(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (hasAbility(player)) {
                event.setCancelled(true);
                int current = 0;
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                        current += effect.getDuration();
                        if (effect.getAmplifier() > 0 && current > 60) {
                            return;
                        }
                    }
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
                        (int) (current + (event.getDamage() * 20D)), 0), true);
            }
        }
    }

    @Override
    public void unregisterListener() {
    }

}
