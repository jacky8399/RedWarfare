package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Medic extends AbilityListener implements Disableable {

    @EventHandler
    public void playerInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player player = (Player) event.getRightClicked();
            Player healer = event.getPlayer();
            if (hasAbility(healer)) {
                if (healer.getItemInHand().getType() == Material.PAPER) {
                    double hp = player.getHealth();
                    if (hp <= player.getMaxHealth() - 1) {
                        hp = Math.min(player.getMaxHealth(), hp + 8);
                        player.setHealth(hp);
                        if (healer.getItemInHand().getAmount() > 1) {
                            healer.getItemInHand().setAmount(healer.getItemInHand().getAmount() - 1);
                        } else {
                            healer.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.PAPER, 20));
        }
    }

    @Override
    public void unregisterListener() {
    }
}
