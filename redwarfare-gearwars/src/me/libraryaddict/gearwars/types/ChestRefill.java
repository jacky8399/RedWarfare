package me.libraryaddict.gearwars.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ChestRefill {

    private ArrayList<Block> opened = new ArrayList<Block>();
    private ArrayList<RandomItem> randomItems;
    private boolean tier2;

    public ChestRefill(ArrayList<RandomItem> rItems, boolean isGodTier) {
        randomItems = new ArrayList<RandomItem>();
        for (RandomItem item : rItems) {
            randomItems.add(item);
        }
        tier2 = isGodTier;
    }

    private int countItems(Inventory inv) {
        int i = 0;
        for (ItemStack item : inv.getContents())
            if (item != null && item.getType() != Material.AIR)
                i++;
        return i;
    }

    private void fillChest(Inventory inv, boolean doubleChest) {
        for (Block b : getBlock(inv.getHolder())) {
            if (!opened.contains(b))
                opened.add(b);
        }
        inv.clear();
        while (countItems(inv) < (doubleChest ? 8 : 4)) {
            Collections.shuffle(randomItems, new Random());
            for (RandomItem item : randomItems) {
                if (item.hasChance()) {
                    inv.setItem(new Random().nextInt(inv.getSize()), item.getItemStack());
                }
            }
        }
    }

    private Block[] getBlock(InventoryHolder holder) {
        if (holder == null)
            return null;
        if (holder instanceof DoubleChest) {
            return new Block[] { ((BlockState) ((DoubleChest) holder).getLeftSide()).getBlock(),
                    ((BlockState) ((DoubleChest) holder).getRightSide()).getBlock() };
        } else if (holder instanceof Chest) {
            return new Block[] { ((BlockState) (Chest) holder).getBlock() };
        }
        return null;
    }

    public boolean isOpened(InventoryHolder inv) {
        for (Block b : getBlock(inv)) {
            if (opened.contains(b))
                return true;
        }
        return false;
    }

    public void refillChests() {
        ArrayList<Block> oldOpened = new ArrayList<Block>(opened);
        Iterator<Block> itel = oldOpened.iterator();
        opened.clear();
        /*    for (Player p : Bukkit.getOnlinePlayers()) {
                InventoryView inv = p.getOpenInventory();
                if (inv.getTopInventory() != null) {
                    Block b = getBlock(inv.getTopInventory().getHolder());
                    if (b != null && !opened.contains(b)) {
                        if (!b.getChunk().isLoaded())
                            b.getChunk().load();
                        fillChest(inv.getTopInventory(), inv.getTopInventory().getHolder() instanceof DoubleChest);
                        opened.add(b);
                    }
                }
            }*/
        while (itel.hasNext()) {
            Block b = itel.next();
            if (opened.contains(b))
                continue;
            if (!b.getChunk().isLoaded())
                b.getChunk().load();
            Inventory inv = ((InventoryHolder) b.getState()).getInventory();
            inv.clear();
            if (!inv.getViewers().isEmpty()) {
                fillChest(inv, inv.getHolder() instanceof DoubleChest);
                opened.add(b);
            } else if (tier2) {
                inv.setItem(0, new ItemStack(Material.STONE));
            }
            if (inv.getHolder() instanceof Chest)
                ((Chest) inv.getHolder()).update();
            if (inv.getHolder() instanceof DoubleChest) {
                ((BlockState) ((DoubleChest) inv.getHolder()).getLeftSide()).update();
            }
        }
    }

    public void refillDoubleChest(Inventory inv) {
        fillChest(inv, true);
    }

    public void refillSingleChest(Inventory inv) {
        fillChest(inv, false);
    }

    public void addOpened(Block block) {
        opened.add(block);
    }

    public void removeBlock(Block block) {
        opened.remove(block);
    }
}
