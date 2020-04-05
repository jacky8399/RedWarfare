package me.libraryaddict.gearwars.abilities;

import java.util.Random;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Demoman extends AbilityListener implements Disableable {
    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            if (hasAbility(player)) {
                event.getDrops().add(new ItemStack(Material.TNT, new Random().nextInt(4) + 1));
            }
        }
    }

    @EventHandler
    public void onSmash(BlockDamageEvent event) {
        if (event.getBlock().getType() == Material.TNT) {
            if (event.getPlayer() != null) {
                if (hasAbility(event.getPlayer())) {
                    event.getPlayer().getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class);
                    event.getBlock().setType(Material.AIR);
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
