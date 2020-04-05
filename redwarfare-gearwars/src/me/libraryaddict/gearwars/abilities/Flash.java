package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Flash extends AbilityListener implements Disableable {

    /* @EventHandler
    public void onSecond(TimeSecondEvent event) {
    for (String st : sprinting) {
      Player p = Bukkit.getPlayerExact(st);
      if (p != null && !plugin.isSpec(p)) {
        Collection<PotionEffect> effects = p.getActivePotionEffects();
        for (PotionEffect effect : effects) {
          if (effect.getType().equals(PotionEffectType.SPEED)
              && effect.getAmplifier() < 3) {
            if (cool.contains(p.getName())) {
              p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30000,
                  effect.getAmplifier() + 1), true);
              cool.remove(p.getName());
            } else
              cool.add(p.getName());
          }
        }
      } else {
        rem.add(st);
      }
    }
    sprinting.removeAll(rem);
    rem.clear();
    }*/

    @EventHandler
    public void onSplash(PotionSplashEvent event) {
        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (effect.getType().equals(PotionEffectType.SLOW)) {
                for (LivingEntity entity : event.getAffectedEntities()) {
                    if (entity instanceof Player && hasAbility((Player) entity)) {
                        entity.removePotionEffect(PotionEffectType.SPEED);
                    }
                }
                break;
            }
        }
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent event) {
        Player p = event.getPlayer();
        if (hasAbility(p)) {
            if (event.isSprinting() && !p.hasPotionEffect(PotionEffectType.SLOW)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30000, 1), true);
            } else {
                p.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            if (gamer.getPlayer().isSprinting()) {
                gamer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30000, 1), true);
            }
        }
    }

    @Override
    public void unregisterListener() {
    }
}
