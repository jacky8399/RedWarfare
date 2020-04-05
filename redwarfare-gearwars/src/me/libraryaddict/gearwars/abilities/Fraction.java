package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.misc.DamageApi;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Fraction extends AbilityListener implements Disableable {
    Map<Player, Player> players = new HashMap<Player, Player>();

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player player = event.getPlayer();
            if (hasAbility(player)) {
                if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.REDSTONE) {
                    if (GearApi.getGameManager().getGTime() >= 120) {
                        Player victim = (Player) event.getRightClicked();
                        if (players.containsKey(player) || players.containsValue(player)) {
                            player.sendMessage(ChatColor.RED + "You are already bonded with a player!");
                        } else if (players.containsKey(victim) || players.containsValue(victim)) {
                            player.sendMessage(ChatColor.RED + "He has already bonded with a player!");
                        } else {
                            players.put(victim, player);
                            if (player.getItemInHand().getAmount() > 1) {
                                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                            } else {
                                player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                            }
                            player.sendMessage(ChatColor.RED + "You've just entered into a blood bond with " + victim.getName()
                                    + "!");
                            victim.sendMessage(ChatColor.RED + "You've just entered into a blood bond with " + player.getName()
                                    + "!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Wait until invincibility runs out!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof Player) {
            Player first = (Player) event.getEntity();
            Player second = null;
            if (players.containsKey(first) || players.containsValue(first)) {
                if (players.containsKey(first)) {
                    second = players.get(first);
                } else {
                    for (Player p : players.keySet()) {
                        if (players.get(p) == first) {
                            second = p;
                            break;
                        }
                    }
                }
            }
            if (second != null) {
                double realDmg = event.getDamage();
                event.setDamage(0);
                realDmg /= 2;
                double fDmg = DamageApi.calculateDamageAddArmor(first, event.getCause(), realDmg);
                if (fDmg >= first.getHealth()) {
                    onDeath(first);
                    GearApi.getPlayerManager().killGamer(GearApi.getPlayerManager().getGamer(first),
                            second.getName() + "'s sacrifice was not enough, " + first.getName() + " was slain!");
                } else {
                    first.damage(0);
                    first.setHealth(first.getHealth() - fDmg);
                }
                double sDmg = DamageApi.calculateDamageAddArmor(second, event.getCause(), realDmg);
                if (sDmg >= second.getHealth()) {
                    onDeath(second);
                    GearApi.getPlayerManager().killGamer(GearApi.getPlayerManager().getGamer(second),
                            first.getName() + "'s sacrifice was not enough, " + second.getName() + " was slain!");
                } else {
                    second.damage(0);
                    second.setHealth(second.getHealth() - sDmg);
                }
            }
        }
    }

    private void onDeath(Player killed) {
        if (players.containsKey(killed) || players.containsValue(killed)) {
            Player second = null;
            if (players.containsKey(killed)) {
                second = players.get(killed);
                players.remove(killed);
            } else {
                for (Player p : players.keySet()) {
                    if (players.get(p) == killed) {
                        second = p;
                        players.remove(p);
                        break;
                    }
                }
            }
            second.sendMessage(ChatColor.RED + "You are no longer in a blood bond with " + killed.getName());
        }
    }

    @EventHandler
    public void playerDeath(PlayerKilledEvent event) {
        Player killed = event.getKilled().getPlayer();
        onDeath(killed);
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.REDSTONE, 5));
        }
    }

    @Override
    public void unregisterListener() {
    }

}
