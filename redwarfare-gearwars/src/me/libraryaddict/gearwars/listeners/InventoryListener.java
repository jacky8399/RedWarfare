package me.libraryaddict.gearwars.listeners;

import me.libraryaddict.inventory.events.NamedPageClickEvent;
import me.libraryaddict.inventory.events.PagesClickEvent;
import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.managers.AbilityManager;
import me.libraryaddict.gearwars.managers.KitManager;
import me.libraryaddict.gearwars.types.Ability;
import me.libraryaddict.gearwars.types.Gamer;
import me.libraryaddict.gearwars.types.Kit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryListener implements Listener {
    @EventHandler
    public void onClick(NamedPageClickEvent event) {
        if (event.getItemStack() != null) {
            if (event.getItemStack().hasItemMeta() && event.getItemStack().getItemMeta().hasDisplayName()) {
                String name = ChatColor.stripColor(event.getItemStack().getItemMeta().getDisplayName());
                Player p = event.getPlayer();
                Gamer gamer = GearApi.getPlayerManager().getGamer(p);
                if (event.getPage().getPageName().equals("Kits")) {
                    KitManager manager = GearApi.getKitManager();
                    Kit kit = manager.getKit(name);
                    if (kit != null) {
                        if (manager.ownsKit(p, kit)) {
                            if (gamer.getKit() == kit) {
                                p.sendMessage(ChatColor.RED + "Already using kit " + kit.getName());
                            } else {
                                manager.setKit(p, kit);
                                p.sendMessage(ChatColor.RED + "Now using kit " + kit.getName());
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not own the kit " + kit.getName());
                        }
                    } else if (name.equalsIgnoreCase("Random Kit")) {
                        p.sendMessage(ChatColor.RED + "Now using a random kit");
                        manager.setKit(p, null);
                    }
                } else if (event.getPage().getPageName().equals("Abilities")) {
                    AbilityManager manager = GearApi.getAbilityManager();
                    Ability ability = manager.getAbility(name);
                    if (ability != null) {
                        if (manager.ownsAbility(p, ability)) {
                            if (gamer != null) {
                                if (gamer.getAbility() == ability) {
                                    p.sendMessage(ChatColor.RED + "Already using ability " + ability.getName());
                                } else {
                                    manager.setAbility(gamer, ability);
                                    p.sendMessage(ChatColor.RED + "Now using ability " + ability.getName());
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not own the ability " + ability.getName());
                        }
                    } else if (name.equalsIgnoreCase("Random ability")) {
                        p.sendMessage(ChatColor.RED + "Now using a random ability");
                        manager.setAbility(gamer, null);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(PagesClickEvent event) {
        if (event.getItemStack() != null) {
            if (event.getItemStack().hasItemMeta() && event.getItemStack().getItemMeta().hasDisplayName()) {
                String name = ChatColor.stripColor(event.getItemStack().getItemMeta().getDisplayName());
                Player player = Bukkit.getPlayerExact(name);
                if (player != null) {
                    event.getPlayer().teleport(player);
                }
            }
        }
    }
}
