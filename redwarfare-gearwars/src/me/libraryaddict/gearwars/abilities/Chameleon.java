package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class Chameleon extends AbilityListener implements Disableable {
    private String chameleonBreakDisguise = ChatColor.GREEN + "You broke out of your disguise!";
    private String chameleonDisguiseBroken = ChatColor.GREEN + "Your disguise was broken!";
    private String chameleonNowDisguised = ChatColor.GREEN + "Now disguised as a %s!";

    @EventHandler
    public void arrowFire(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player p = (Player) event.getEntity().getShooter();
            if (DisguiseAPI.isDisguised(p) && hasAbility(p)) {
                DisguiseAPI.undisguiseToAll(p);
                p.sendMessage(ChatColor.BLUE + "You launched a " + event.getEntity().getType().name().toLowerCase()
                        + " and broke the disguise!");
            }
        }
    }

    private void disguise(Entity entity, Player p) {
        if (entity instanceof Creature) {
            if (hasAbility(p)) {
                if (!DisguiseAPI.isDisguised(p))
                    DisguiseAPI.disguiseToAll(p, new MobDisguise(DisguiseType.getType(entity.getType()), true));
                else {
                    Disguise disguise = DisguiseAPI.getDisguise(p);
                    if (disguise.getType() == DisguiseType.valueOf(entity.getType().name()))
                        return;
                    DisguiseAPI.disguiseToAll(p, new MobDisguise(DisguiseType.valueOf(entity.getType().name()), true));
                }
                p.sendMessage(String.format(chameleonNowDisguised, entity.getType().name().replace("_", " ").toLowerCase()));
            }
        }
    }

    private boolean isChameleon(Entity entity) {
        return (entity instanceof Player && hasAbility((Player) entity));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;
        Entity damager = event.getDamager();
        if (event.getDamager() instanceof Projectile && ((Projectile) damager).getShooter() != null
                && ((Projectile) damager).getShooter() instanceof Entity)
            damager = (Entity) ((Projectile) damager).getShooter();
        if (isChameleon(event.getEntity())) {
            Player p = (Player) event.getEntity();
            if (DisguiseAPI.isDisguised(p)) {
                DisguiseAPI.undisguiseToAll(p);
                p.sendMessage(chameleonDisguiseBroken);
            }
        }
        if (isChameleon(damager)) {
            Player p = (Player) damager;
            if (event.getEntity() instanceof Player && DisguiseAPI.isDisguised(p)) {
                DisguiseAPI.undisguiseToAll(p);
                p.sendMessage(chameleonBreakDisguise);
            } else
                disguise(event.getEntity(), p);
        }
    }

    @Override
    public void registerListener() {
    }

    @Override
    public void unregisterListener() {
    }

}
