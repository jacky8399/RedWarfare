package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.events.TimeSecondEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Poseidon extends AbilityListener implements Disableable {
    public int potionMultiplier = 0;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (hasAbility(event.getPlayer()) && event.getPlayer().getRemainingAir() < 200)
            event.getPlayer().setRemainingAir(200);
    }

    @EventHandler
    public void onSecond(TimeSecondEvent event) {
        for (Gamer gamer : getGamers()) {
            Player p = gamer.getPlayer();
            if (p.getLocation().getBlock().isLiquid()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, potionMultiplier), true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, potionMultiplier), true);
            }
        }
    }

    @Override
    public void registerListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterListener() {
        // TODO Auto-generated method stub

    }
}
