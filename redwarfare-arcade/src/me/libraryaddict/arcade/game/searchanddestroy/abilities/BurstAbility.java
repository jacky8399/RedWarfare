package me.libraryaddict.arcade.game.searchanddestroy.abilities;

import me.libraryaddict.arcade.kits.Ability;
import me.libraryaddict.core.damage.AttackType;
import me.libraryaddict.core.damage.CustomDamageEvent;
import me.libraryaddict.core.damage.DamageManager;
import me.libraryaddict.core.damage.DamageMod;
import me.libraryaddict.core.explosion.CustomExplosion;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

/**
 * Created by noracrouch0220 on 11/11/2017.
 */
public class BurstAbility extends Ability {

    private DamageMod _selfDamage = DamageMod.CUSTOM.getSubMod("Self Burst Arrow");
    private AttackType HURTFINGERS = new AttackType("Burst Arrow Shot", "%Killed% ripped their fingers apart.").setIgnoreArmor();
    private AttackType BURSTARROW = new AttackType("Burst Arrow Explosion", "%Killed% was faced with an explosive arrow from %Killer%.").setExplosion().setIgnoreArmor();
    private AttackType BURSTARROW_SELF = new AttackType("Burst Arrow Self Explosion", "%Killed% lost their sense of aim.").setExplosion().setIgnoreArmor();

    private void explode(Projectile projectile)
    {

        //projectile.remove();

        Player thrower = (Player) projectile.getShooter();

        if (!isLive())
            return;

        if (!isAlive(thrower))
            return;

        new CustomExplosion(projectile.getLocation().subtract(projectile.getLocation().getDirection().normalize().multiply(0.1)),
                4F, BURSTARROW, BURSTARROW_SELF)

                .setDamageBlocks(false)

                .setMaxDamage(6)

                .setDamager(thrower)

                .setIgnoreNonLiving(true)

                .explode();

    }

    @EventHandler
    public void arrowLaunch(ProjectileLaunchEvent event) {

        if (!(event.getEntity() instanceof Arrow))
            return;

        Arrow arrow = (Arrow) event.getEntity();

        if (!(arrow.getShooter() instanceof Player))
            return;

        Player player = (Player) arrow.getShooter();

        if (!hasAbility(player))
            return;

        DamageManager damageManager = getGame().getDamageManager();
        CustomDamageEvent event1 = new CustomDamageEvent(player, HURTFINGERS, 1.5);

        Location playerLocation = player.getLocation();

        double pitch = ((playerLocation.getPitch() + 90) * Math.PI) / 180;
        double yaw  = ((playerLocation.getYaw() + 90)  * Math.PI) / 180;

        double x = Math.sin(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch) * Math.sin(yaw);
        double z = Math.cos(pitch);



        event1.setKnockback(new Vector(-x, 0, -z));

        damageManager.callDamage(event1);
    }

    @EventHandler
    public void arrowHit(ProjectileHitEvent event) {

        if (!(event.getEntity() instanceof Arrow))
            return;

        Arrow arrow = (Arrow) event.getEntity();

        if (!(arrow.getShooter() instanceof Player))
            return;

        Player player = (Player) arrow.getShooter();

        if (!hasAbility(player))
            return;

        explode(arrow);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(CustomDamageEvent event)
    {
        if (event.getAttackType() != BURSTARROW_SELF)
            return;

        Entity damagee = event.getDamagee();

        if (!isAlive(damagee))
            return;

        if (!isLive())
            return;

        event.setDamager(null, null);
        event.addDamage(_selfDamage, 2);
    }
}
