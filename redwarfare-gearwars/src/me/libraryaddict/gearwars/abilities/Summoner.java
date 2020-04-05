package me.libraryaddict.gearwars.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.metadata.FixedMetadataValue;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

public class Summoner extends AbilityListener implements Disableable {
    private class SummonInfo {
        private ItemStack book;
        private HashMap<EntityType, Integer> mobs = new HashMap<EntityType, Integer>();
        private ArrayList<EntityType> mobsOrdered = new ArrayList<EntityType>();

        public SummonInfo(ItemStack book) {
            this.book = book;
            reload();
        }

        public void addMob(EntityType type) {
            if (mobs.containsKey(type)) {
                mobs.put(type, mobs.get(type) + 1);
            } else {
                mobs.put(type, 1);
                mobsOrdered.add(type);
            }
            reset();
        }

        public void reload() {
            BookMeta meta = (BookMeta) book.getItemMeta();
            mobs.clear();
            for (String string : meta.getPages()) {
                String[] s = string.split("\n");
                for (String a : s) {
                    if (a.length() == 0)
                        continue;
                    String[] split = a.split(" ");
                    int number = Integer.parseInt(split[0].substring(0, split[0].length() - 1));
                    EntityType type = EntityType.valueOf(a.substring(split[0].length() + 1).replace(" ", "_").toUpperCase());
                    mobs.put(type, number);
                    mobsOrdered.add(type);
                }
            }
        }

        public void removeMob(EntityType type) {
            mobs.put(type, mobs.get(type) - 1);
            if (mobs.get(type) <= 0) {
                mobs.remove(type);
                mobsOrdered.remove(type);
            }
            reset();
        }

        public void reset() {
            BookMeta meta = (BookMeta) book.getItemMeta();
            String s = "";
            for (EntityType entityType : mobsOrdered) {
                s += mobs.get(entityType) + "x " + entityType.name().toLowerCase().replace("_", " ") + "\n";
            }
            meta.setPages(s);
            book.setItemMeta(meta);
        }
    }

    private HashMap<Player, Integer> selectedMob = new HashMap<Player, Integer>();

    private ItemStack buildBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("Notch");
        meta.setDisplayName("Summoning Book");
        book.setItemMeta(meta);
        return book;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER && event.getEntity().getKiller() != null
                && hasAbility(event.getEntity().getKiller())) {
            for (ItemStack item : event.getEntity().getKiller().getInventory().getContents()) {
                if (item != null && item.getType() == Material.WRITTEN_BOOK) {
                    if (item.hasItemMeta() && ((BookMeta) item.getItemMeta()).getAuthor().equals("Notch")) {
                        SummonInfo info = new SummonInfo(item);
                        info.addMob(event.getEntity().getType());
                    }
                }
            }
        }
        if (event.getEntity().hasMetadata("Summoned")) {
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (hasAbility(event.getPlayer()) && event.getItem() != null) {
            if (event.getItem().getType() == Material.WRITTEN_BOOK && event.getItem().hasItemMeta()
                    && ((BookMeta) event.getItem().getItemMeta()).getAuthor().equals("Notch")) {
                Player p = event.getPlayer();
                int toggle = selectedMob.containsKey(p) ? selectedMob.get(p) : 0;
                SummonInfo info = new SummonInfo(event.getItem());
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    event.setCancelled(true);
                    // Spawn
                    if (info.mobs.size() > 0) {
                        Location spawnLoc = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation()
                                .add(0.5, 0, 0.5);
                        if (toggle >= info.mobs.size()) {
                            toggle = 0;
                        }
                        EntityType type = info.mobsOrdered.get(toggle);
                        Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, type);
                        entity.setMetadata("Summoned", new FixedMetadataValue(GearApi.getMainPlugin(), p.getName()));
                        info.removeMob(type);
                        String name = type.name().toLowerCase().replace("_", " ");
                        p.sendMessage(ChatColor.RED + "Spawned a " + name + "!");
                        if (info.mobs.containsKey(type)) {
                            p.sendMessage(ChatColor.RED + "You have " + info.mobs.get(type) + " left!");
                        } else {
                            p.sendMessage(ChatColor.RED + "You ran out of " + name + "!");
                            if (info.mobs.size() >= toggle) {
                                toggle = 0;
                            }
                            if (info.mobs.size() == 0) {
                                p.sendMessage(ChatColor.RED + "You ran out of mobs to spawn!");
                            } else {
                                type = info.mobsOrdered.get(toggle);
                                p.sendMessage(ChatColor.RED + "Selected mob " + type.name().toLowerCase().replace("_", " ")
                                        + " of which you have " + info.mobs.get(type) + "!");
                            }
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have any mobs captured!");
                    }
                } else if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    event.setCancelled(true);
                    // Toggle
                    toggle++;
                    if (toggle >= info.mobs.size()) {
                        toggle = 0;
                    }
                    if (info.mobs.size() == 0) {
                        p.sendMessage(ChatColor.RED + "Unable to toggle, You do not have any mobs captured!");
                    } else {
                        EntityType selected = info.mobsOrdered.get(toggle);
                        p.sendMessage(ChatColor.RED + "Selected mob " + selected.name().replace("_", " ").toLowerCase()
                                + ", you have " + info.mobs.get(selected) + " left");
                    }
                }
                selectedMob.put(p, toggle);
            }
        }
    }

    @EventHandler
    public void onKilled(PlayerKilledEvent event) {
        selectedMob.remove(event.getKilled().getPlayer());
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (event.getEntity().hasMetadata("Summoned")) {
            if (event.getTarget() != null && event.getTarget() instanceof Player) {
                if (event.getEntity().getMetadata("Summoned").get(0).asString().equals(((Player) event.getTarget()).getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            gamer.getPlayer().getInventory().addItem(buildBook());
        }
    }

    @Override
    public void unregisterListener() {
    }

}
