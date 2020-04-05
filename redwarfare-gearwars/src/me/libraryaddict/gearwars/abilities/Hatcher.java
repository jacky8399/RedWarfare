package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class Hatcher extends AbilityListener implements Disableable {
    private HashMap<Block, ArrayList<Entity>> spawnedMobs = new HashMap<Block, ArrayList<Entity>>();
    private HashMap<Player, ArrayList<Block>> spawners = new HashMap<Player, ArrayList<Block>>();

    public void giveSpawner(Player player) {
        if (player.isOnline() && player.getAllowFlight() == false) {
            HashMap<Integer, ItemStack> drops = player.getInventory().addItem(new ItemStack(52));
            for (Map.Entry<Integer, ItemStack> entry : drops.entrySet()) {
                player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
            }
        }
    }

    private boolean isSpawner(Block block) {
        for (ArrayList<Block> blocks : spawners.values()) {
            if (blocks.contains(block)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        List<Block> blockListCopy = new ArrayList<Block>();
        for (Block block : blockListCopy) {
            if (isSpawner(block)) {
                removeSpawner(block, true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item != null && item.getType() == Material.MONSTER_EGG && hasAbility(event.getPlayer())
                && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.MOB_SPAWNER) {
                event.setCancelled(true);
                Player p = event.getPlayer();
                if (spawners.containsKey(p) && spawners.get(p).contains(event.getClickedBlock())) {
                    CreatureSpawner spawner = (CreatureSpawner) event.getClickedBlock().getState();
                    EntityType entity = EntityType.fromId(item.getData().getData());
                    spawner.setSpawnedType(entity);
                    spawner.update();
                    p.sendMessage(ChatColor.RED + "You are now spawning " + entity.name());
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        p.getInventory().setItemInHand(new ItemStack(Material.AIR));
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "This is not your mob spawner!");
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block b = event.getBlock();
        if (b.getType() == Material.MOB_SPAWNER) {
            if (hasAbility(event.getPlayer())) {
                if (!spawners.containsKey(event.getPlayer())) {
                    spawners.put(event.getPlayer(), new ArrayList<Block>());
                }
                spawners.get(event.getPlayer()).add(b);
                MobSpawnerAbstract spawner = ((TileEntityMobSpawner) ((CraftWorld) b.getWorld()).getHandle().getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()))).getSpawner();
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("CustomName", event.getPlayer().getName());
                MobSpawnerData data = new MobSpawnerData(EntityType.PIG.getTypeId(), nbt);
                spawner.a(data);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerKilledEvent event) {
        if (event.getKillerGamer() != null) {
            if (hasAbility(event.getKillerGamer())) {
                EntityType[] mobs = new EntityType[] { EntityType.SKELETON, EntityType.CREEPER, EntityType.ZOMBIE,
                        EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.PIG_ZOMBIE, EntityType.SILVERFISH };
                event.getKilled()
                        .getPlayer()
                        .getWorld()
                        .dropItemNaturally(event.getKilled().getPlayer().getLocation(),
                                new ItemStack(Material.MONSTER_EGG, 1, mobs[new Random().nextInt(mobs.length)].getTypeId()));
            }
        } else if (hasAbility(event.getKilled()) && spawners.containsKey(event.getKilled().getPlayer())) {
            List<Block> blocks = spawners.get(event.getKilled().getPlayer());
            while (!blocks.isEmpty()) {
                removeSpawner(blocks.get(0), false);
            }
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == SpawnReason.SPAWNER) {
            if (event.getEntity().getCustomName() != null) {
                event.getEntity().setMetadata("DontTarget",
                        new FixedMetadataValue(GearApi.getMainPlugin(), event.getEntity().getCustomName()));
                event.getEntity().setCustomName(null);
            }
        }
    }

    @EventHandler
    public void onSpawnerSmash(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.MOB_SPAWNER) {
            for (Player p : spawners.keySet()) {
                if (spawners.get(p).contains(event.getBlock())) {
                    removeSpawner(event.getBlock(), false);
                    event.getPlayer().sendMessage(ChatColor.BLUE + "You destroyed " + p.getName() + "'s spawner!");
                    p.sendMessage(ChatColor.BLUE + event.getPlayer().getName() + " destroyed your monster spawner!");
                }
            }
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (event.getEntity().hasMetadata("DontTarget") && event.getTarget() instanceof Player) {
            if (event.getEntity().getMetadata("DontTarget").get(0).asString().equals(((Player) event.getTarget()).getName())) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.MOB_SPAWNER));
        }
    }

    private void removeSpawner(Block block, boolean message) {
        Iterator<Player> itel = spawners.keySet().iterator();
        while (itel.hasNext()) {
            Player p = itel.next();
            if (spawners.get(p).contains(block)) {
                if (message) {
                    p.sendMessage(ChatColor.RED + "Spawner has been broken!");
                }
                spawners.get(p).remove(block);
                if (spawners.get(p).isEmpty()) {
                    itel.remove();
                }
                if (spawnedMobs.containsKey(block)) {
                    for (Entity entity : spawnedMobs.get(block)) {
                        entity.remove();
                    }
                    spawnedMobs.remove(block);
                }
                block.setType(Material.AIR);
                break;
            }
        }
    }

    @Override
    public void unregisterListener() {
    }

}
