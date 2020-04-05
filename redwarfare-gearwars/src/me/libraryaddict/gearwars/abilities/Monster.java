package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class Monster extends AbilityListener implements Disableable {
    @EventHandler
    public void onEntityDmg(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (hasAbility(player)) {
                event.setDamage(99999);
            }
        } else if (event.getEntity() instanceof Player && event.getDamager() instanceof Monster) {
            Player player = (Player) event.getEntity();
            if (hasAbility(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTargetEvent(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player && (event.getEntity() instanceof Monster || event.getEntity() instanceof Wolf)) {
            Player player = (Player) event.getTarget();
            if (hasAbility(player)) {
                event.setCancelled(true);
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
