package me.libraryaddict.gearwars.listeners;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.managers.GameManager;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class GeneralListener implements Listener {
    private GameManager gm = GearApi.getGameManager();

    @EventHandler
    public void breakDoor(EntityBreakDoorEvent event) {
        if (gm.getGTime() < 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void burn(BlockBurnEvent event) {
        if (gm.getGTime() < 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void ignite(BlockIgniteEvent event) {
        if (gm.getGTime() < 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (gm.getGTime() < 0) {
            if (event.getEntity() instanceof Animals) {
                GearApi.getGameManager().storeMob(event.getLocation(), event.getEntityType());
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        for (Block b : event.blockList()) {
            BlockState state = b.getState();
            if (b.getState() instanceof Chest) {
                GearApi.getChestManager().fillChest(null, ((Chest) state).getInventory());
                GearApi.getChestManager().removeChest(b);
                state.update();
            } else if (b.getState() instanceof DoubleChest) {
                GearApi.getChestManager().fillChest(null, ((DoubleChest) state).getLeftSide().getInventory());
                GearApi.getChestManager().fillChest(null, ((DoubleChest) state).getRightSide().getInventory());
                GearApi.getChestManager().removeChest(b);
                state.update();
            }
        }
    }

    @EventHandler
    public void onSpread(BlockSpreadEvent event) {
        if (gm.getGTime() < 0) {
            event.setCancelled(true);
        }
    }
}
