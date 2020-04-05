package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Thor extends AbilityListener implements Disableable {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (hasAbility(player)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getItemInHand().getType() == Material.WOOD_AXE) {
                if (!player.hasMetadata("LightningCooldown")
                        || player.getMetadata("LightningCooldown").get(0).asLong() <= System.currentTimeMillis()) {
                    player.setMetadata("LightningCooldown",
                            new FixedMetadataValue(GearApi.getMainPlugin(), System.currentTimeMillis() + 7000));
                    LightningStrike lightning = player.getWorld().strikeLightning(
                            player.getWorld().getHighestBlockAt(event.getClickedBlock().getLocation()).getLocation());
                    lightning.setMetadata("DontHurt", new FixedMetadataValue(GearApi.getMainPlugin(), player.getName()));
                } else {
                    player.sendMessage(ChatColor.RED
                            + "You cannot use thor for "
                            + (int) Math.ceil((player.getMetadata("LightningCooldown").get(0).asLong() - System
                                    .currentTimeMillis()) / 1000) + " seconds.");
                }
            }
        }
    }

    @EventHandler
    public void playerDmg(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof LightningStrike) {
            if (event.getDamager().hasMetadata("DontHurt")
                    && event.getDamager().getMetadata("DontHurt").get(0).asString()
                            .equals(((Player) event.getEntity()).getName())) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.WOOD_AXE));
        }
    }

    @Override
    public void unregisterListener() {
    }
}
