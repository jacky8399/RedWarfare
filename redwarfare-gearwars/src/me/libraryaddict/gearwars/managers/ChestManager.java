package me.libraryaddict.gearwars.managers;

import java.util.ArrayList;
import java.util.HashMap;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.ChestLootEvent;
import me.libraryaddict.gearwars.types.ChestRefill;
import me.libraryaddict.gearwars.types.RandomItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestManager {
    public enum ChestTier {
        NORMAL, SPECIAL;
    }

    private HashMap<ChestTier, ChestRefill> chests = new HashMap<ChestTier, ChestRefill>();

    public ChestManager() {
        ArrayList<RandomItem> randomItems = new ArrayList<RandomItem>();
        randomItems.add(new RandomItem(20, Material.IRON_BLOCK, 0, 1, 1));
        randomItems.add(new RandomItem(20, Material.TNT, 0, 1, 20));
        randomItems.add(new RandomItem(20, Material.REDSTONE_TORCH_ON, 0, 3, 8));
        randomItems.add(new RandomItem(60, Material.ENCHANTMENT_TABLE, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.IRON_PICKAXE, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.IRON_SWORD, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.IRON_HELMET, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.IRON_CHESTPLATE, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.IRON_LEGGINGS, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.IRON_BOOTS, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.GOLDEN_APPLE, 0, 1, 5));
        randomItems.add(new RandomItem(20, Material.COOKED_BEEF, 0, 1, 5));
        randomItems.add(new RandomItem(15, Material.WATER_BUCKET, 0, 1, 1));
        randomItems.add(new RandomItem(15, Material.LAVA_BUCKET, 0, 1, 1));
        randomItems.add(new RandomItem(15, Material.IRON_INGOT, 0, 1, 6));
        randomItems.add(new RandomItem(30, Material.BOW, 0, 1, 1));
        randomItems.add(new RandomItem(25, Material.ARROW, 0, 5, 20));
        randomItems.add(new RandomItem(10, Material.STONE_SWORD, 0, 1, 1));
        randomItems.add(new RandomItem(80, Material.POTION, 8193, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 8194, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 8195, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16389, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 8201, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16388, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16396, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16392, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16394, 1, 2));
        randomItems.add(new RandomItem(20, Material.EXP_BOTTLE, 0, 1, 3));
        randomItems.add(new RandomItem(40, Material.BREWING_STAND_ITEM, 0, 1, 1));
        randomItems.add(new RandomItem(10, Material.WOOD, 0, 1, 5));
        randomItems.add(new RandomItem(60, Material.WEB, 0, 1, 1));
        randomItems.add(new RandomItem(60, Material.ENDER_PEARL, 0, 1, 1));
        randomItems.add(new RandomItem(15, Material.STONE_SPADE, 0, 1, 1));
        chests.put(ChestTier.NORMAL, new ChestRefill(randomItems, false));
        randomItems.add(new RandomItem(20, Material.TNT, 0, 1, 20));
        randomItems.add(new RandomItem(20, Material.EXP_BOTTLE, 0, 1, 3));
        randomItems.add(new RandomItem(20, Material.REDSTONE_TORCH_ON, 0, 1, 4));
        randomItems.add(new RandomItem(40, Material.DIAMOND_SWORD, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.DIAMOND_CHESTPLATE, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.DIAMOND_LEGGINGS, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.DIAMOND_BOOTS, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.DIAMOND_HELMET, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.CHAINMAIL_HELMET, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.CHAINMAIL_CHESTPLATE, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.CHAINMAIL_LEGGINGS, 0, 1, 1));
        randomItems.add(new RandomItem(40, Material.CHAINMAIL_BOOTS, 0, 1, 1));
        randomItems.add(new RandomItem(20, Material.GOLDEN_APPLE, 0, 1, 10));
        randomItems.add(new RandomItem(60, Material.GOLDEN_APPLE, 0, 1, 3));
        randomItems.add(new RandomItem(15, Material.WATER_BUCKET, 0, 1, 1));
        randomItems.add(new RandomItem(15, Material.LAVA_BUCKET, 0, 1, 1));
        randomItems.add(new RandomItem(30, Material.DIAMOND_BLOCK, 0, 1, 1));
        randomItems.add(new RandomItem(20, Material.COOKED_BEEF, 0, 1, 20));
        randomItems.add(new RandomItem(30, Material.ENDER_PEARL, 0, 1, 10));
        randomItems.add(new RandomItem(40, Material.ENCHANTMENT_TABLE, 0, 1, 1));
        randomItems.add(new RandomItem(30, Material.BOW, 0, 1, 1));
        randomItems.add(new RandomItem(25, Material.ARROW, 0, 5, 20));
        randomItems.add(new RandomItem(20, Material.DIAMOND_PICKAXE, 0, 1, 1));
        randomItems.add(new RandomItem(80, Material.POTION, 8225, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 8258, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16421, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16420, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16428, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 8259, 1, 2));
        randomItems.add(new RandomItem(80, Material.POTION, 16457, 1, 2));
        randomItems.add(new RandomItem(40, Material.BREWING_STAND_ITEM, 0, 1, 1));
        randomItems.add(new RandomItem(30, Material.WEB, 0, 1, 1));
        randomItems.add(new RandomItem(30, Material.ENDER_PEARL, 0, 1, 1));
        chests.put(ChestTier.SPECIAL, new ChestRefill(randomItems, true));
    }

    public boolean fillChest(Player player, Inventory inv) {
        ChestTier tier = getTier(inv);
        if (tier != null) {
            ChestLootEvent event = new ChestLootEvent(GearApi.getPlayerManager().getGamer(player), tier);
            if (player != null) {
                Bukkit.getPluginManager().callEvent(event);
            }
            tier = event.getChestTier();
            if (tier != null) {
                if (inv.getHolder() instanceof DoubleChest)
                    chests.get(tier).refillDoubleChest(inv);
                else
                    chests.get(tier).refillSingleChest(inv);
                return true;
            }
        }
        return false;
    }

    private ChestTier getTier(Inventory inv) {
        for (ChestRefill refill : chests.values())
            if (refill.isOpened(inv.getHolder()))
                return null;
        ItemStack item = inv.getItem(0);
        if (item != null && item.getType() != Material.AIR)
            return ChestTier.SPECIAL;
        return ChestTier.NORMAL;
    }

    public void refillChests() {
        for (ChestRefill refill : chests.values()) {
            refill.refillChests();
        }
    }

    public void addChest(Player player, Block block) {
        chests.get(ChestTier.NORMAL).addOpened(block);
    }

    public void removeChest(Block b) {
        for (ChestTier tier : ChestTier.values()) {
            chests.get(tier).removeBlock(b);
        }
    }
}
