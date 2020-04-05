package me.libraryaddict.arcade.game.searchanddestroy.abilities;

import me.libraryaddict.arcade.events.DeathEvent;
import me.libraryaddict.arcade.events.GameStateEvent;
import me.libraryaddict.arcade.game.GameTeam;
import me.libraryaddict.arcade.kits.Ability;
import me.libraryaddict.arcade.managers.GameState;
import me.libraryaddict.core.damage.AttackType;
import me.libraryaddict.core.damage.CustomDamageEvent;
import me.libraryaddict.core.damage.DamageManager;
import me.libraryaddict.core.data.ParticleColor;
import me.libraryaddict.core.inventory.utils.ItemBuilder;
import me.libraryaddict.core.time.TimeEvent;
import me.libraryaddict.core.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by noracrouch0220 on 11/8/2017.
 */
public class BarricaderAbility extends Ability {

    public static HashMap<UUID, ArrayList<Block>> currentBarricades = new HashMap<>();
    private ArrayList<Barricade> _regen = new ArrayList<>();
    private static AttackType BARRICADE = new AttackType("Barricade Damage", "%Killed% couldn't escape from %Killer%'s barricades.");
    private int _radius = 5;

    public class Barricade {
        private Material block;
        private UUID owner;
        private long startTime;
        private long regenTime;
        private byte data;

        public Barricade(Material block, UUID uuid, long regenTime, byte data) {
            this.block = block;
            owner = uuid;
            startTime = System.currentTimeMillis();
            this.data = data;
            this.regenTime = regenTime;
        }

        public boolean isOwner(UUID uuid) {
            return this.owner == uuid;
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();

        if (!hasAbility(player))
            return;

        event.setCancelled(false);

        ArrayList<Block> blocks = new ArrayList<>();

        if (currentBarricades.get(player.getUniqueId()) != null)
            blocks.addAll(currentBarricades.get(player.getUniqueId()));

        blocks.add(event.getBlockPlaced());

        currentBarricades.put(player.getUniqueId(), blocks);

    }

    @EventHandler
    public void onDeath(DeathEvent event) {

        Player player = event.getPlayer();

        if (!hasAbility(player))
            return;

        if (currentBarricades.get(player.getUniqueId()) == null)
            return;

        Iterator<Block> itr = currentBarricades.get(player.getUniqueId()).iterator();

        while (itr.hasNext()) {
            Block barricade = itr.next();
            barricade.setType(Material.AIR);

            itr.remove();
        }

        _regen.removeIf(barricade -> !barricade.isOwner(player.getUniqueId()));

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        for (Map.Entry<UUID, ArrayList<Block>> entry : currentBarricades.entrySet()) {

            Player owner = Bukkit.getPlayer(entry.getKey());

            if (entry.getValue() == null)
                continue;

            if (!entry.getValue().contains(event.getBlock()))
                continue;

            Player breaker = event.getPlayer();

            if (getGame().sameTeam(breaker, owner) && breaker != owner)
                continue;

            event.setCancelled(false);

            entry.getValue().remove(event.getBlock());

            event.setDropItems(false);

            //owner.getInventory().addItem(new ItemBuilder(event.getBlock().getType()).build());

            int delay = 1000;
            short data = 0;

            switch (event.getBlock().getType()) {
                case LONG_GRASS:
                    delay *= 20;
                    data = 1;
                    break;
                case WOOD:
                    delay *= 40;
                    break;
                case STONE:
                    delay *= 60;
                    break;
                case WEB:
                    delay *= 60;
                    break;
                default:
                    delay *= 30;
            }

            _regen.add(new Barricade(event.getBlock().getType(), owner.getUniqueId(), delay, event.getBlock().getData()));

            if (breaker == owner)
                continue;

            breaker.setFireTicks(40);
        }

    }

    @EventHandler
    public void onRegen(TimeEvent event) {

        Iterator<Barricade> iterator = _regen.iterator();

        while (iterator.hasNext()) {

            Barricade barricade = iterator.next();

            Player player = Bukkit.getPlayer(barricade.owner);
            Material block = barricade.block;
            byte data = barricade.data;
            long startTime = barricade.startTime;
            long regenTime = barricade.regenTime;

            if (!getGame().isAlive(player))
                continue;

            if (UtilTime.elasped(startTime, regenTime)) {
                UtilInv.addItem(player, new ItemBuilder(block, 1, data).build());
                iterator.remove();
            }
        }

    }

    @EventHandler
    public void gameStateEvent(GameStateEvent event) {
        if (event.getState() != GameState.End)
            return;

        currentBarricades.clear();
        _regen.clear();
    }

    @EventHandler
    public void onTick(TimeEvent event) {

        for (Map.Entry<UUID, ArrayList<Block>> entry : currentBarricades.entrySet()) {

            Player owner = Bukkit.getPlayer(entry.getKey());

            for (Player effected : Bukkit.getOnlinePlayers()) {

                for (Block block : entry.getValue()) {

                    if (UtilTime.currentTick % 10 == 0) {

                        UtilParticle.playParticle(
                                block.getLocation().add(
                                        UtilMath.rr(-.3, 1.3), UtilMath.rr(-.3, 1.3), UtilMath.rr(-.3, 1.3)),
                                getGame().getTeam(owner).getSettings().getParticleColor(),
                                UtilParticle.ViewDist.LONG,
                                Bukkit.getOnlinePlayers().toArray(new Player[0]));
                    }

                    if (owner == effected)
                        continue;

                    if (getGame().sameTeam(owner, effected))
                        continue;

                    if (!(effected.getLocation().distanceSquared(block.getLocation()) <= _radius * _radius))
                        continue;

                    GameTeam team = getGame().getTeam(effected);

                    if (team == null)
                        continue;

                    ParticleColor color = team.getSettings().getParticleColor();
                    ArrayList<Player> enemies = UtilPlayer.getPlayers();

                    enemies.remove(effected);

                    if (getGame().getKit(effected).getName().equals("Ghost") || getGame().getKit(effected).getName().equals("Wraith")) {
                        Player[] array = enemies.toArray(new Player[0]);

                        //if (UtilTime.currentTick % 10 != 0) continue;

                        for (int i = 0; i < 5; i++) {
                            UtilParticle.playParticle(
                                    effected.getLocation()/*.add(UtilMath.rr(-0.1, .1), UtilMath.rr(0, .1), UtilMath.rr(-.3, .1))*/,
                                    color, UtilParticle.ViewDist.LONG, array);

                            UtilParticle.playParticle(
                                    effected.getLocation()/*.add(UtilMath.rr(-0.1, .1), UtilMath.rr(0, .1), UtilMath.rr(-.3, .1))*/,
                                    color, UtilParticle.ViewDist.LONG, effected);
                        }

                        continue;
                    }

                    if (UtilTime.currentTick % 20 == 0) {

                        DamageManager damageManager = getGame().getDamageManager();
                        CustomDamageEvent event1 = new CustomDamageEvent(effected, BARRICADE, 2);

                        event1.setKnockback(new Vector(0, 0, 0));
                        event1.setFinalDamage(2);

                        damageManager.callDamage(event1);

                    }

                }

            }

        }

    }

    public static AttackType getAttackType() {
        return BARRICADE;
    }

}
