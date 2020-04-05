package me.libraryaddict.gearwars.abilities;

import java.util.ArrayList;

import me.libraryaddict.inventory.InventoryApi;
import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class Blazer extends AbilityListener implements Disableable {
    private ArrayList<Player> toggledOn = new ArrayList<Player>();
    private ItemStack toggler;

    public Blazer() {
        toggler = InventoryApi.setNameAndLore(new ItemStack(Material.MAGMA_CREAM), ChatColor.GOLD + "Toggle Blaze",
                ChatColor.WHITE + "Right click with this", ChatColor.WHITE + "to toggle your blazing!");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && hasAbility((Player) event.getEntity())) {
            if (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
                event.setCancelled(true);
                event.getEntity().setFireTicks(0);
            }
        }
    }

    @EventHandler
    public void onKilled(PlayerKilledEvent event) {
        this.toggledOn.remove(event.getKilled().getPlayer());
    }

    @EventHandler
    public void onSecond(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (this.toggledOn.contains(p)) {
            Block b = event.getFrom().getBlock();
            Material type = b.getType();
            if ((type == Material.AIR || type == Material.SNOW || type == Material.LONG_GRASS)
                    && GearApi.getPlayerManager().isSolid(b.getRelative(BlockFace.DOWN))) {
                b.setType(Material.FIRE);
            }
        }
    }

    @EventHandler
    public void onToggle(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (hasAbility(p)) {
            if (event.getItem() != null && event.getItem().isSimilar(toggler)) {
                if (toggledOn.contains(p)) {
                    toggledOn.remove(p);
                    p.sendMessage(ChatColor.GOLD + "You are no longer blazing!");
                } else {
                    toggledOn.add(p);
                    p.sendMessage(ChatColor.GOLD + "You are blazing hotter than the sun!");
                }
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(toggler);
        }
    }

    @Override
    public void unregisterListener() {
    }
}
