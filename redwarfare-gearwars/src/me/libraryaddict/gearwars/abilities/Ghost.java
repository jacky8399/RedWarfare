package me.libraryaddict.gearwars.abilities;

import java.util.HashMap;
import java.util.Map;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Ghost extends AbilityListener implements Disableable {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (hasAbility(player)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null
                    && event.getItem().getType() == Material.GLASS) {
                event.setCancelled(true);
                final BlockState state = event.getClickedBlock().getState();
                state.getBlock().setType(Material.AIR);
                if (player.getItemInHand().getAmount() > 1) {
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                } else {
                    player.getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
                }
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GearApi.getMainPlugin(), new Runnable() {
                    public void run() {
                        if (state.getBlock().getType() == Material.AIR) {
                            state.getBlock().setTypeIdAndData(state.getTypeId(), state.getRawData(), false);
                        }
                        if (hasAbility(player)) {
                            HashMap<Integer, ItemStack> drops = player.getInventory().addItem(new ItemStack(20));
                            for (Map.Entry<Integer, ItemStack> entry : drops.entrySet()) {
                                player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
                            }
                        }
                    }
                }, 200);
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.GLASS, 6));
        }
    }

    @Override
    public void unregisterListener() {
    }
}
