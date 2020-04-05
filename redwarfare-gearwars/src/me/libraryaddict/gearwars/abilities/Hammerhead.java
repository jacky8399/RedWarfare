package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Hammerhead extends AbilityListener implements Disableable {

    @EventHandler
    public void onHitEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (hasAbility(player)) {
                event.setDamage(event.getDamage() * 2);
                if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
                    Item item = player.getWorld().dropItemNaturally(player.getLocation(), player.getItemInHand().clone());
                    if (player.getItemInHand().getItemMeta() != null) {
                        item.getItemStack().setItemMeta(player.getItemInHand().getItemMeta());
                    }
                    item.setPickupDelay(60);
                    player.setItemInHand(new ItemStack(Material.AIR));
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
