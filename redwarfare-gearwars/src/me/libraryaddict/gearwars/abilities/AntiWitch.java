package me.libraryaddict.gearwars.abilities;

import java.util.Iterator;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.libraryaddict.gearwars.types.AbilityListener;

public class AntiWitch extends AbilityListener {

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && hasAbility((Player) event.getDamager())) {
            if (event.getEntity() instanceof LivingEntity) {
                Iterator<PotionEffect> itel = ((LivingEntity) event.getEntity()).getActivePotionEffects().iterator();
                while (itel.hasNext()) {
                    PotionEffect effect = itel.next();
                    PotionEffectType type = effect.getType();
                    if (type.equals(PotionEffectType.ABSORPTION) || type.equals(PotionEffectType.DAMAGE_RESISTANCE)
                            || type.equals(PotionEffectType.FAST_DIGGING) || type.equals(PotionEffectType.FIRE_RESISTANCE)
                            || type.equals(PotionEffectType.HEALTH_BOOST) || type.equals(PotionEffectType.INCREASE_DAMAGE)
                            || type.equals(PotionEffectType.INVISIBILITY) || type.equals(PotionEffectType.JUMP)
                            || type.equals(PotionEffectType.NIGHT_VISION) || type.equals(PotionEffectType.REGENERATION)
                            || type.equals(PotionEffectType.REGENERATION) || type.equals(PotionEffectType.SATURATION)
                            || type.equals(PotionEffectType.SPEED) || type.equals(PotionEffectType.WATER_BREATHING)) {
                        ((LivingEntity) event.getEntity()).removePotionEffect(type);
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
