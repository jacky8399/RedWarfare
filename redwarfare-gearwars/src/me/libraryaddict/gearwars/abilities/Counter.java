package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.DamageApi;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Counter extends AbilityListener implements Disableable {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && !event.isCancelled()) {
            Player p = (Player) event.getEntity();
            if (hasAbility(p) && p.isBlocking()) {
                // COUNTER THAT BITCH
                LivingEntity ent = null;
                if (event.getDamager() instanceof Projectile
                        && ((Projectile) event.getDamager()).getShooter() instanceof LivingEntity) {
                    ent = (LivingEntity) ((Projectile) event.getDamager()).getShooter();
                } else if (event.getDamager() instanceof LivingEntity)
                    ent = (LivingEntity) event.getDamager();
                else
                    return;
                double armDmg = DamageApi.calculateDamageAddArmor(p, event.getCause(), event.getDamage() * 0.3);
                if (event.getDamager() instanceof Player) {
                    if (ent.getHealth() <= armDmg) {
                        GearApi.getPlayerManager().killGamer(GearApi.getPlayerManager().getGamer(ent),
                                ((Player) ent).getName() + " was killed by " + p.getName() + "'s countered damage");
                    } else {
                        ent.setHealth(ent.getHealth() - armDmg);
                        ent.damage(0);
                    }
                } else {
                    ent.damage(armDmg);
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
