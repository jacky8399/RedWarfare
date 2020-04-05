package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.DamageApi;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Cannibal extends AbilityListener implements Disableable {

    @EventHandler
    public void playerDmg(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player
                && GearApi.getGameManager().getGTime() >= 120) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            if (hasAbility(damager)) {
                if (event.getDamage() > 1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 240, 3));
                    if (DamageApi.calculateDamageAddArmor(player, event.getCause(), event.getDamage()) >= 2) {
                        HashMap<Integer, ItemStack> drops = damager.getInventory().addItem(new ItemStack(Material.ROTTEN_FLESH));
                        for (Map.Entry<Integer, ItemStack> entry : drops.entrySet()) {
                            damager.getWorld().dropItemNaturally(damager.getLocation(), entry.getValue());
                        }
                    }
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
