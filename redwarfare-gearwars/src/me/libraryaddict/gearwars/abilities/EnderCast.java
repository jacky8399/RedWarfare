package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;

public class EnderCast extends AbilityListener implements Disableable {
    private Map<Villager, Player> npcs = new HashMap<Villager, Player>();

    public EnderCast() {
        // registerScoreboard(DisplaySlot.SIDEBAR, ChatColor.BLUE + "Cooldown");
        registerScoreboard(DisplaySlot.SIDEBAR, ChatColor.BLUE + "Health");
        // registerScoreboard(DisplaySlot.SIDEBAR, ChatColor.BLUE + "Teleport");
    }

    public void castNpc(Player player) {
        if (GearApi.getGameManager().getGTime() < 120) {
            player.sendMessage(ChatColor.GREEN
                    + "Your soul is weary from being created. Try again after invincibilty has worn off");
        } else if (!npcs.containsValue(player.getName())) {
            player.sendMessage(ChatColor.YELLOW + "You gaze at the block and slowly your inner self appears");
            List<Block> blockList = player.getLastTwoTargetBlocks((Set<Material>) null, 500);
            if (!blockList.isEmpty()) {
                Block block = blockList.get(0);
                Location spawnLoc = block.getLocation().clone().add(0.5, 1, 0.5);
                final Villager villager = (Villager) player.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
                npcs.put(villager, player);
                ScoreboardManager.makeScore(npcs.get(villager), DisplaySlot.SIDEBAR, ChatColor.BLUE + "Health",
                        (int) villager.getHealth());
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GearApi.getMainPlugin(), new Runnable() {
                    public void run() {
                        if (npcs.containsKey(villager)) {
                            Player player = npcs.get(villager);
                            if (villager.isDead()) {
                                GearApi.getPlayerManager().killGamer(GearApi.getPlayerManager().getGamer(player),
                                        player.getName() + "'s soul was torn apart.");
                            } else {
                                ScoreboardManager.hideScore(npcs.get(villager), DisplaySlot.SIDEBAR, ChatColor.BLUE + "Health");
                                npcs.remove(villager);
                                player.teleport(villager);
                                player.damage(0);
                                player.setHealth(villager.getHealth());
                                player.sendMessage(ChatColor.YELLOW + "Your soul summoned you to it!");
                                villager.remove();
                            }
                        }
                    }
                }, new Random().nextInt(80) + 80);
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You don't want to tear your soul even more!");
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Villager) {
            Villager entity = (Villager) event.getEntity();
            if (npcs.containsKey(entity)) {
                if (event.getDamage() > 1 || entity.getHealth() % 2 == 0) {
                    npcs.get(entity).sendMessage(ChatColor.YELLOW + "You feel your soul cast being ripped apart!");
                    ScoreboardManager.makeScore(npcs.get(entity), DisplaySlot.SIDEBAR, ChatColor.BLUE + "Health",
                            (int) entity.getHealth());
                }
            }
        }
    }

    @EventHandler
    public void entityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Villager) {
            if (npcs.containsKey(event.getEntity())) {
                npcs.get(event.getEntity()).sendMessage(ChatColor.YELLOW + "You felt your soul torn to shreds. Brace yourself.");
            }
        }
    }

    @EventHandler
    public void entityInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            if (npcs.containsKey((event.getRightClicked()))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(
                        ChatColor.GREEN + "This is " + npcs.get(event.getRightClicked()).getName()
                                + "'s soul cast! Kill it to kill him!");
            }
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (event.getItem() != null && hasAbility(player)) {
                if (event.getItem().getType() == Material.ENDER_PEARL) {
                    event.setCancelled(true);
                    castNpc(player);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        if (npcs.containsValue(event.getKilled().getPlayer())) {
            Iterator<Villager> itel = npcs.keySet().iterator();
            while (itel.hasNext()) {
                Villager id = itel.next();
                if (npcs.get(id) == event.getKilled().getPlayer()) {
                    itel.remove();
                    break;
                }
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
        }
    }

    @Override
    public void unregisterListener() {
    }

}
