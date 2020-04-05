package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Explosive extends AbilityListener implements Disableable {

    @EventHandler
    public void onArrowShot(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            if (event.getEntityType() == EntityType.ARROW) {
                if (hasAbility(shooter)) {
                    shooter.getWorld().createExplosion(event.getEntity().getLocation(), 1.5F);
                    event.getEntity().remove();
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
