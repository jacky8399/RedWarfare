package me.libraryaddict.arcade.game.searchanddestroy.abilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.UUID;
import me.libraryaddict.arcade.events.DeathEvent;
import me.libraryaddict.arcade.events.GameStateEvent;
import me.libraryaddict.arcade.game.GameTeam;
import me.libraryaddict.arcade.kits.Ability;
import me.libraryaddict.arcade.managers.GameState;
import me.libraryaddict.core.damage.AttackType;
import me.libraryaddict.core.damage.CustomDamageEvent;
import me.libraryaddict.core.damage.DamageManager;
import me.libraryaddict.core.data.ParticleColor;
import me.libraryaddict.core.time.TimeEvent;
import me.libraryaddict.core.time.TimeType;
import me.libraryaddict.core.utils.UtilMath;
import me.libraryaddict.core.utils.UtilParticle;
import me.libraryaddict.core.utils.UtilPlayer;
import me.libraryaddict.core.utils.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class BarricaderAbility extends Ability {
    public static ArrayList<Barricade> allBarricades = new ArrayList<>();

    private static AttackType BARRICADE = new AttackType("Barricade Damage", "%Killed% couldn't escape from %Killer%'s barricades.", new String[0]);

    private int _radius = 5;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!hasAbility(player))
            return;
        event.setCancelled(false);
        Barricade barricade = new Barricade(event.getBlockPlaced(), player, getGame().getTeam((Entity)player));
        allBarricades.add(barricade);
    }

    @EventHandler
    public void onDeath(DeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!hasAbility(player))
            return;
        for (Barricade barricade : allBarricades) {
            if (barricade.getOwner().getUniqueId() == uuid)
                barricade.destroy();
        }
        allBarricades.stream().filter(barricade -> barricade.isOwner(uuid)).forEach(Barricade::destroy);
        allBarricades.removeIf(barricade -> barricade.isOwner(uuid));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Barricade barricade = getBarricade(block);
        if (barricade == null)
            return;
        event.setCancelled(false);
        int delay = 20;
        switch (block.getType()) {
            case TALL_GRASS:
                delay *= 20;
                break;
            case OAK_WOOD:
                delay *= 40;
                break;
            case STONE:
            case COBWEB:
                delay *= 60;
                break;
            default:
                delay *= 30;
                break;
        }
        barricade.setActive(false);
        barricade.setRegenTime(getGame().getGameTime() + delay);
        Player breaker = event.getPlayer();
        Player owner = barricade.getOwner();
        if (breaker.getUniqueId() == owner.getUniqueId())
            return;
        breaker.setFireTicks(40);
    }

    @EventHandler
    public void onRegen(TimeEvent event) {
        if (event.getType() != TimeType.TICK)
            return;
        Iterator<Barricade> itr = allBarricades.iterator();
        while (itr.hasNext()) {
            Barricade barricade = itr.next();
            if (barricade.isActive())
                continue;
            if (barricade.getRegenTime() >= UtilTime.currentTick)
                barricade.regenerate();
            itr.remove();
        }
    }

    @EventHandler
    public void gameStateEvent(GameStateEvent event) {
        if (event.getState() != GameState.End)
            return;
        allBarricades.clear();
    }

    @EventHandler
    public void activateBarricades(TimeEvent event) {
        ArrayList<Barricade> activeBarricades = new ArrayList<>();
        for (Barricade barricade : allBarricades) {
            if (barricade.isActive())
                activeBarricades.add(barricade);
        }
        if (event.getType() != TimeType.SEC)
            return;
        LinkedHashSet<Player> affectedPlayers = new LinkedHashSet<>();
        for (Barricade barricade : activeBarricades) {
            Player owner = barricade.getOwner();
            Block block = barricade.getBlock();
            UtilParticle.playParticle(block
                            .getLocation().add(
                            UtilMath.rr(-0.3D, 1.3D), UtilMath.rr(-0.3D, 1.3D), UtilMath.rr(-0.3D, 1.3D)),
                    getGame().getTeam((Entity)barricade.getOwner()).getSettings().getParticleColor(), UtilParticle.ViewDist.LONG,

                    (Player[])Bukkit.getOnlinePlayers().toArray((Object[])new Player[0]));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (getGame().sameTeam((Entity)player, (Entity)owner))
                    continue;
                if (player.getUniqueId() == owner.getUniqueId())
                    continue;
                if (player.getLocation().distanceSquared(block.getLocation()) <= (this._radius * this._radius))
                    affectedPlayers.add(player);
            }
        }
        for (Player player : affectedPlayers) {
            GameTeam team = getGame().getTeam((Entity)player);
            if (team == null)
                continue;
            ParticleColor color = team.getSettings().getParticleColor();
            if (getGame().getKit(player).getName().equals("Ghost") || getGame().getKit(player).getName().equals("Wraith")) {
                for (int i = 0; i < 5; i++) {
                    UtilParticle.playParticle(player
                                    .getLocation(), color, UtilParticle.ViewDist.LONG,
                            (Player[])UtilPlayer.getPlayers().toArray((Object[])new Player[0]));
                    UtilParticle.playParticle(player
                                    .getLocation(), color, UtilParticle.ViewDist.LONG,
                            (Player[])UtilPlayer.getPlayers().toArray((Object[])new Player[0]));
                }
                continue;
            }
            DamageManager damageManager = getGame().getDamageManager();
            CustomDamageEvent event1 = new CustomDamageEvent((Entity)player, BARRICADE, 2.0D);
            event1.setKnockback(new Vector(0, 0, 0));
            event1.setFinalDamage(2.0D);
            damageManager.callDamage(event1);
        }
    }

    public Barricade getBarricade(Block block) {
        return allBarricades.stream().filter(barricade -> (barricade.getBlock() == block)).findFirst().orElse(null);
    }

    public static AttackType getAttackType() {
        return BARRICADE;
    }
}
