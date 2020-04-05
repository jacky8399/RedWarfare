package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.events.TimeSecondEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.HashMap;

public class Rewind extends AbilityListener implements Disableable {
    private class Rewinder {
        private ArrayList<Float> fallDist = new ArrayList<Float>();
        private ArrayList<Location> locs = new ArrayList<Location>();
    }

    public Rewind() {
        registerScoreboard(DisplaySlot.SIDEBAR, ChatColor.BLUE + "Cooldown");
    }

    private HashMap<Player, Rewinder> rewinders = new HashMap<Player, Rewinder>();

    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        rewinders.remove(event.getKilled().getPlayer());
    }

    @EventHandler
    public void onEntity(PlayerInteractEntityEvent event) {
        playerInteract(new PlayerInteractEvent(event.getPlayer(), Action.RIGHT_CLICK_AIR, null, null, null));
    }

    @EventHandler
    public void onSecond(TimeSecondEvent event) {
        for (Gamer gamer : getGamers()) {
            Player player = gamer.getPlayer();
            if (!rewinders.containsKey(player)) {
                rewinders.put(player, new Rewinder());
            }
            Rewinder rewinder = rewinders.get(player);
            rewinder.fallDist.add(player.getFallDistance());
            rewinder.locs.add(player.getLocation().clone());
            if (rewinder.locs.size() == 30) {
                ScoreboardManager.hideScore(player, DisplaySlot.SIDEBAR, ChatColor.BLUE + "Cooldown");
            } else if (rewinder.locs.size() < 30) {
                ScoreboardManager.makeScore(player, DisplaySlot.SIDEBAR, ChatColor.BLUE + "Cooldown", 30 - rewinder.locs.size());
            }
            if (rewinder.fallDist.size() > 30) {
                rewinder.fallDist.remove(0);
            }
            if (rewinder.locs.size() > 30) {
                rewinder.locs.remove(0);
            }
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && hasAbility(p)
                && p.getPlayer().getItemInHand().getType() == Material.WATCH) {
            if (rewinders.containsKey(p)) {
                Rewinder rewinder = rewinders.get(p);
                if (rewinder.locs.size() >= 30 && rewinder.fallDist.size() >= 30) {
                    p.eject();
                    p.leaveVehicle();
                    p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 9);
                    p.teleport(rewinder.locs.get(0));
                    p.setFallDistance(rewinder.fallDist.get(0));
                    p.sendMessage(ChatColor.BLUE + "You step sidewards into your timestream and appear 30 seconds ago!");
                    rewinder.fallDist.clear();
                    rewinder.locs.clear();
                    ScoreboardManager.makeScore(p, DisplaySlot.SIDEBAR, ChatColor.BLUE + "Cooldown", 30 - rewinder.locs.size());
                } else {
                    p.sendMessage(ChatColor.BLUE + "Your timestream is still recovering! Timestream will be stable in "
                            + (30 - rewinder.locs.size()) + " seconds!");
                }
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.WATCH));
        }
    }

    public void unregisterListener() {
        rewinders.clear();
    }
}
