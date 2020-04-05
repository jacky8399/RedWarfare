package me.libraryaddict.gearwars.listeners;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.managers.GameManager;
import me.libraryaddict.gearwars.managers.InventoryManager;
import me.libraryaddict.gearwars.managers.PlayerManager;
import me.libraryaddict.gearwars.misc.DamageApi;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import me.libraryaddict.gearwars.types.Damage;
import me.libraryaddict.gearwars.types.Gamer;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Iterator;

public class PlayerListener implements Listener {
    private GameManager gm = GearApi.getGameManager();
    private InventoryManager inv = GearApi.getInventoryManager();
    private PlayerManager pm = GearApi.getPlayerManager();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        switch (event.getBlock().getType()) {
            case CHEST:
            case TRAPPED_CHEST:
                GearApi.getChestManager().addChest(event.getPlayer(), event.getBlock());
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Gamer gamer = pm.getGamer(event.getPlayer());
        if (gamer == null || !gamer.isAlive()) {
            event.setCancelled(true);
            return;
        }
        if (event.getBlock().getType() == Material.CHEST || event.getBlock().getType() == Material.TRAPPED_CHEST) {
            GearApi.getChestManager().removeChest(event.getBlock());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!pm.getGamer(event.getWhoClicked()).isAlive()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(BlockDamageEvent event) {
        Gamer gamer = pm.getGamer(event.getPlayer());
        if (gamer == null || !gamer.isAlive()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        Gamer gamer = pm.getGamer(event.getEntity());
        if (event.getEntity() instanceof Player) {
            if (gm.isGameOver() || gm.getGTime() < 120 || gamer == null || !gamer.isAlive()) {
                event.setCancelled(true);
            }
        }
        if (event.isCancelled()) {
            if (event.getCause() == DamageCause.VOID) {
                if (gamer != null) {
                    pm.sendToSpawn(gamer.getPlayer(), 5);
                }
            }
            if (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
                event.getEntity().setFireTicks(0);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Gamer gamer = pm.getGamer(event.getDamager());
            if (gamer == null || !gamer.isAlive()) {
                event.setCancelled(true);
            }
        }
        if (!event.isCancelled() && event.getEntity() instanceof Player) {
            Gamer g = pm.getGamer(event.getEntity());
            if (g != null && g.isAlive()) {
                event.getEntity().getWorld().spigot()
                        .playEffect(((LivingEntity) event.getEntity()).getEyeLocation().clone().subtract(0, 0.3, 0),
                                Effect.TILE_BREAK, Material.WOOL.getId(), 14, 0, 0, 0, 1, 6, 64);
                Gamer gamer = pm.getGamer(event.getDamager());
                if (gamer == null && event.getDamager() instanceof Projectile && ((Projectile) event.getDamager())
                        .getShooter() instanceof Player) {
                    gamer = pm.getGamer((Player) ((Projectile) event.getDamager()).getShooter());
                }
                if (gamer == null && event.getDamager() instanceof Tameable && ((Tameable) event.getDamager())
                        .isTamed()) {
                    gamer = pm.getGamer(((Tameable) event.getDamager()).getOwner().getName());
                }
                if (gamer != null) {
                    pm.setDamager(g, new Damage(System.currentTimeMillis() + 60000, gamer));
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        pm.killGamer(pm.getGamer(event.getEntity()), event.getDeathMessage());
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Gamer gamer = pm.getGamer(event.getPlayer());
        if (gamer == null || !gamer.isAlive()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        if (!pm.getGamer(event.getPlayer()).isAlive()) {
            event.setAmount(0);
        }
    }

    @EventHandler
    public void onHungry(FoodLevelChangeEvent event) {
        if (!pm.getGamer((Player) event.getEntity()).isAlive()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Gamer gamer = pm.getGamer(event.getPlayer());
        if (gamer == null || !gamer.isAlive()) {
            event.setCancelled(true);
        }
        if (event.getItem() != null && event.getAction().name().contains("RIGHT")) {
            if (event.getItem().equals(inv.getAbilityMenu())) {
                inv.openAbilityMenu(event.getPlayer());
            } else if (event.getItem().equals(inv.getKitMenu())) {
                inv.openKitMenu(event.getPlayer());
            } else if (event.getItem().equals(inv.getSpectatorIcon())) {
                inv.openSpectatorMenu(event.getPlayer());
            } else if (event.getItem().getType() == Material.COMPASS) {
                Gamer toTrack = null;
                double dist = 0;
                for (Gamer g : pm.getAliveGamers()) {
                    if (g != gamer) {
                        double newDist = g.getPlayer().getLocation().distance(gamer.getPlayer().getLocation());
                        if (newDist >= 15 && (toTrack == null || newDist < dist)) {
                            toTrack = g;
                            dist = newDist;
                        }
                    }
                }
                gamer.getPlayer().playSound(gamer.getPlayer().getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 10);
                if (toTrack != null) {
                    gamer.getPlayer().sendMessage(ChatColor.RED + "Compass pointing at " + toTrack.getName());
                    gamer.getPlayer().setCompassTarget(toTrack.getPlayer().getLocation());
                } else {
                    gamer.getPlayer().sendMessage(ChatColor.RED + "Can't find anyone to track! Pointing to spawn!");
                    gamer.getPlayer().setCompassTarget(gamer.getPlayer().getWorld().getSpawnLocation());
                }
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Gamer gamer = pm.getGamer(p);
        if (gamer == null || !gamer.isAlive()) {
            event.setCancelled(true);
            if (gm.getGTime() >= 0) {
                if (event.getRightClicked() instanceof Player) {
                    gamer = pm.getGamer(event.getRightClicked());
                    if (gamer.isAlive()) {
                        Player player = (Player) event.getRightClicked();
                        p.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.RESET + gamer.getName());
                        if (gamer.getAbility() != null) {
                            p.sendMessage(
                                    ChatColor.GOLD + "Ability: " + ChatColor.GREEN + gamer.getAbility().getName());
                        }
                        p.sendMessage(ChatColor.GOLD + "Health: " + ChatColor.RED + (int) Math
                                .ceil(player.getHealth()) + "/" + (int) player
                                .getMaxHealth() + ChatColor.DARK_RED + " ❤");
                        p.sendMessage(ChatColor.GOLD + "Hunger: " + ChatColor.YELLOW + player
                                .getFoodLevel() + "/20 " + ChatColor.GOLD + ChatColor.ITALIC + "❦");
                        p.sendMessage(ChatColor.GOLD + "Armor: " + ChatColor.AQUA + DamageApi.getArmorRating(player));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        ScoreboardManager.registerScoreboard(event.getPlayer());
        Gamer gamer = pm.registerGamer(event.getPlayer());
        gamer.clearInventory();
        gamer.getPlayer().setLevel(-Math.min(0, gm.getGTime()));
        pm.sendToSpawn(gamer.getPlayer(), 5);
        gamer.getPlayer().setAllowFlight(true);
        if (gm.getGTime() >= 0) {
            pm.setSpectator(gamer);
            gamer.updateOthersToSelf();
        } else {
            PlayerInventory in = gamer.getPlayer().getInventory();
            in.addItem(inv.getKitMenu());
            in.addItem(inv.getAbilityMenu());
            ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.BLUE + "Players", pm.getGamers().size());
            if (gm.getGTime() < -30 && Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers()) {
                Bukkit.broadcastMessage(ChatColor.RED + "Game is full! Time set to 30 seconds!");
                gm.setTime(-30);
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);
        System.out.print(event.getPlayer().getName() + " has been kicked with message " + event.getReason());
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() == null)
            return;
        if (inv.getHolder() instanceof Chest || inv.getHolder() instanceof DoubleChest) {
            GearApi.getChestManager().fillChest((Player) event.getPlayer(), event.getInventory());
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Gamer gamer = pm.getGamer(event.getPlayer());
        if (gamer == null || !gamer.isAlive()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        Iterator<LivingEntity> itel = event.getAffectedEntities().iterator();
        while (itel.hasNext()) {
            Entity entity = itel.next();
            if (entity instanceof Player) {
                Gamer gamer = pm.getGamer(entity);
                if (gamer == null || !gamer.isAlive()) {
                    itel.remove();
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Gamer gamer = pm.getGamer(event.getPlayer());
        if (gamer.isAlive()) {
            pm.killGamer(gamer, gamer.getName() + " was killed for leaving the game");
        }
        pm.unregisterGamer(gamer);
        if (gm.getGTime() < 0) {
            ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.BLUE + "Players", pm.getGamers().size());
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (gm.getGTime() < 0) {
            event.setCancelled(true);
        } else if (event.getTarget() != null && event.getTarget() instanceof Player) {
            Gamer gamer = pm.getGamer(event.getTarget());
            if (gamer == null || !gamer.isAlive()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVechileEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) {
            Gamer gamer = pm.getGamer(event.getEntered());
            if (gamer == null || !gamer.isAlive()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVechileEnter(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            Gamer gamer = pm.getGamer(event.getExited());
            if (gamer == null || !gamer.isAlive()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVechileEvent(VehicleDestroyEvent event) {
        if (event.getAttacker() instanceof Player) {
            Gamer gamer = pm.getGamer(event.getAttacker());
            if (gamer == null || !gamer.isAlive()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVechileMove(VehicleEntityCollisionEvent event) {
        if (event.getEntity() instanceof Player) {
            Gamer gamer = pm.getGamer(event.getEntity());
            if (gamer == null || !gamer.isAlive()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void paintingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Gamer gamer = pm.getGamer(event.getRemover());
            if (gamer == null || !gamer.isAlive()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void paintingBreak(HangingBreakEvent event) {
        if (gm.getGTime() < 0) {
            event.setCancelled(true);
        }
    }
}
