package me.libraryaddict.gearwars.abilities;

import java.util.Random;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Stripper extends AbilityListener implements Disableable {

    @EventHandler
    public void playerInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player player = (Player) event.getRightClicked();
            Player healer = event.getPlayer();
            if (hasAbility(healer)) {
                if (healer.getItemInHand() != null && healer.getItemInHand().getType() == Material.RED_ROSE) {
                    if (GearApi.getGameManager().getGTime() >= 120) {
                        ItemStack[] pItems = player.getInventory().getArmorContents();
                        boolean itemFound = false;
                        for (ItemStack item : pItems) {
                            if (item != null && item.getType() != Material.AIR) {
                                itemFound = true;
                                break;
                            }
                        }
                        if (itemFound) {
                            if (healer.getItemInHand().getAmount() > 1) {
                                healer.getItemInHand().setAmount(healer.getItemInHand().getAmount() - 1);
                            } else {
                                healer.getInventory().setItemInHand(new ItemStack(Material.AIR));
                            }
                            ItemStack[] hItems = healer.getInventory().getArmorContents();
                            for (ItemStack item : hItems) {
                                if (item != null && item.getType() != Material.AIR) {
                                    healer.getWorld().dropItemNaturally(healer.getLocation(), item);
                                }
                            }
                            healer.getInventory().setArmorContents(new ItemStack[4]);
                            healer.updateInventory();
                            if (new Random().nextInt(3) == 0) {
                                for (ItemStack item : pItems) {
                                    if (item != null && item.getType() != Material.AIR) {
                                        healer.getWorld().dropItemNaturally(player.getLocation(), item);
                                    }
                                }
                                player.getInventory().setArmorContents(new ItemStack[4]);
                                player.updateInventory();
                                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " was seduced by " + healer.getName()
                                        + "! They both removed their armor..");
                            } else {
                                healer.sendMessage(ChatColor.AQUA
                                        + "They resisted your sexy dance! But you had already stripped down for them..");
                            }
                        } else {
                            healer.sendMessage(ChatColor.RED + "They are not wearing any armor!");
                        }
                    } else {
                        healer.sendMessage(ChatColor.RED + "Do this after invincibility runs out!");
                    }
                }
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(new ItemStack(Material.RED_ROSE, 5));
        }
    }

    @Override
    public void unregisterListener() {
    }

}
