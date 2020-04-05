package me.libraryaddict.gearwars.abilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Transporter extends AbilityListener implements Disableable {

    private Map<Player, Block> portals = new HashMap<Player, Block>();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (portals.containsValue(event.getBlock())) {
            Iterator<Player> itel = portals.keySet().iterator();
            while (itel.hasNext()) {
                Player player = itel.next();
                if (portals.get(player) == event.getBlock()) {
                    if (player == event.getPlayer()) {
                        player.sendMessage(ChatColor.RED + "You broke your transportation link!");
                    } else {
                        player.sendMessage(ChatColor.RED + event.getPlayer().getName() + " broke your transportation link!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (portals.containsValue(block)) {
                Iterator<Player> itel = portals.keySet().iterator();
                while (itel.hasNext()) {
                    Player player = itel.next();
                    if (portals.get(player) == block) {
                        player.sendMessage(ChatColor.RED + "Your transportation link exploded!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (hasAbility(event.getPlayer()) && event.getPlayer().getItemInHand() != null
                && event.getPlayer().getItemInHand().getType() == Material.SPONGE) {
            if (portals.containsKey(event.getPlayer())) {
                event.setCancelled(transport(event.getPlayer()));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (hasAbility(event.getPlayer()) && event.getItem() != null && event.getItem().getType() == Material.SPONGE) {
            if (portals.containsKey(event.getPlayer())) {
                event.setCancelled(transport(event.getPlayer()));
            }
        }
    }

    @EventHandler
    public void onPiston(BlockPistonExtendEvent event) {
        for (Block b : event.getBlocks()) {
            if (portals.containsValue(b)) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.SPONGE && hasAbility(event.getPlayer())) {
            if (portals.containsKey(event.getPlayer())) {
                if (transport(event.getPlayer())) {
                    event.setCancelled(true);
                }
            } else {
                portals.put(event.getPlayer(), event.getBlock());
                event.getPlayer().sendMessage(ChatColor.BLUE + "Transport location set");
            }
        }
    }

    @EventHandler
    public void onRetract(BlockPistonRetractEvent event) {
        if (event.isSticky()
                && portals.containsValue(event.getBlock().getRelative(event.getDirection()).getRelative(event.getDirection()))) {
            event.setCancelled(true);
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.SPONGE, 8));
        }
    }

    private boolean transport(Player p) {
        if (portals.containsKey(p)) {
            ItemStack item = p.getItemInHand();
            if (item != null && item.getType() == Material.SPONGE) {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    p.setItemInHand(new ItemStack(Material.AIR));
                }
                Block block = portals.remove(p);
                block.setType(Material.AIR);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
                p.teleport(block.getLocation().clone().add(0.5, 0.5, 0.5));
                p.sendMessage(ChatColor.BLUE + "Transported!");
                return true;
            }
        }
        return false;
    }

    @Override
    public void unregisterListener() {
    }

}
