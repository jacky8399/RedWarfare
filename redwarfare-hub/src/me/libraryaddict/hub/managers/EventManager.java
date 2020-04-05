package me.libraryaddict.hub.managers;

import me.libraryaddict.core.damage.AttackType;
import me.libraryaddict.core.damage.CustomDamageEvent;
import me.libraryaddict.core.plugin.MiniPlugin;
import me.libraryaddict.core.utils.UtilPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EventManager extends MiniPlugin
{
    private boolean _joinable;

    public EventManager(JavaPlugin plugin)
    {
        super(plugin, "Event Manager");

        new BukkitRunnable()
        {
            public void run()
            {
                _joinable = true;
            }
        }.runTaskLater(getPlugin(), 2);
    }

    @EventHandler
    public void breakDoor(EntityBreakDoorEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFlow(BlockFromToEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBurn(BlockBurnEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCropsBreak(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.PHYSICAL)
            return;

        if (event.getClickedBlock() == null)
            return;

        if (event.getClickedBlock().getType() != Material.FARMLAND)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(CustomDamageEvent event)
    {
        event.setCancelled(true);

        if (event.getAttackType().isFire())
        {
            event.getDamagee().setFireTicks(0);
        }

        if (!event.isPlayerDamagee() || event.getAttackType() != AttackType.VOID)
            return;

        new BukkitRunnable()
        {
            public void run()
            {
                UtilPlayer.tele(event.getPlayerDamagee(), event.getDamagee().getWorld().getSpawnLocation());
            }
        }.runTask(getPlugin());

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event)
    {
        event.setAmount(0);
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event)
    {
        event.blockList().clear();
    }

    @EventHandler
    public void onFade(BlockFadeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onGrow(StructureGrowEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event)
    {
        if (_joinable)
            return;

        event.disallow(Result.KICK_OTHER, "The server is still starting up!");
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() == SpawnReason.CUSTOM)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onSpread(BlockSpreadEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onVechileEvent(VehicleDestroyEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event)
    {
        event.setCancelled(true);
    }

}
