package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.TimeSecondEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Allergies extends AbilityListener implements Disableable {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (GearApi.getGameManager().getGTime() >= 120) {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                Player damager = (Player) event.getDamager();
                Player p = (Player) event.getEntity();
                if (hasAbility(p)) {
                    if (damager.getItemInHand().getType() == Material.YELLOW_FLOWER
                            || damager.getItemInHand().getType() == Material.RED_ROSE) {
                        event.setCancelled(true);
                        if (p.getHealth() <= 8) {
                            GearApi.getPlayerManager().killGamer(
                                    GearApi.getPlayerManager().getGamer(p),
                                    p.getName() + " took one whiff of " + damager.getName()
                                            + "'s flower and broke out into a rash!");
                        } else {
                            p.setHealth(p.getHealth() - 8);
                            p.damage(0);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSecond(TimeSecondEvent event) {
        if (GearApi.getGameManager().getGTime() >= 120) {
            for (Gamer gamer : getGamers()) {
                Player p = gamer.getPlayer();
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0), true);
                Location loc = p.getLocation().clone();
                if (p.isOnGround()) {
                    loc.subtract(0, 0.1, 0);
                } else {
                    for (double i = 0; i < 10; i++) {
                        if (loc.getBlock().getType() == Material.AIR) {
                            loc.subtract(0, 0.1, 0);
                        }
                    }
                }
                if (loc.getBlock().getType() == Material.GRASS || p.getLocation().getBlock().getType() == Material.LONG_GRASS) {
                    if (p.getHealth() <= 6) {
                        GearApi.getPlayerManager().killGamer(GearApi.getPlayerManager().getGamer(p),
                                p.getName() + " broke out into a rash and sneezed to death!");
                    } else {
                        p.damage(0);
                        p.setHealth(p.getHealth() - 6);
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
