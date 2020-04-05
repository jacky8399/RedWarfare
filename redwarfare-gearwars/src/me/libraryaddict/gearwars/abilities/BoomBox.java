package me.libraryaddict.gearwars.abilities;

import java.util.HashMap;
import java.util.HashSet;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class BoomBox extends AbilityListener implements Disableable {
    private HashMap<Player, Long> cooldown = new HashMap<Player, Long>();

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (hasAbility(player)) {
            if (event.getAction().name().contains("RIGHT")) {
                Block block = event.getClickedBlock();
                if (block == null) {
                    block = player.getTargetBlock((HashSet<Material>) new HashSet(), 2);
                }
                if (block != null && block.getType() == Material.WOOL && block.getData() == (byte) 15) {
                    event.setCancelled(true);
                    if (!cooldown.containsKey(player) || cooldown.get(player) < System.currentTimeMillis()) {
                        cooldown.put(player, System.currentTimeMillis() + 60000);
                        Location wool = block.getLocation();
                        int distModif = -6;
                        event.getPlayer().setMetadata("BoomBox",
                                new FixedMetadataValue(GearApi.getMainPlugin(), System.currentTimeMillis() + 500));
                        for (int b = 0; b < 3; b++) {
                            player.getWorld().createExplosion(wool.getX() + distModif, wool.getY(), wool.getZ(), 2);
                            player.getWorld().createExplosion(wool.getX(), wool.getY(), wool.getZ() + distModif, 2);
                            int G = -3;
                            for (int c = 0; c < 3; c++) {
                                player.getWorld().createExplosion(wool.getX() + G, wool.getY(), wool.getZ() + distModif, 2);
                                player.getWorld().createExplosion(wool.getX() + distModif, wool.getY(), wool.getZ() + G, 2);
                                G = G + 3;
                            }
                            distModif += 6;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You cannot use boombox at this time");
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerDmg(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() == DamageCause.ENTITY_EXPLOSION) {
                Player player = (Player) event.getEntity();
                if (player.hasMetadata("BoomBox") && player.getMetadata("BoomBox").get(0).asLong() > System.currentTimeMillis()) {
                    event.setDamage(0);
                }
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.WOOL, 4, (short) 15));
        }
    }

    @Override
    public void unregisterListener() {
    }

}
