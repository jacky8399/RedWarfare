package me.libraryaddict.gearwars.types;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RandomItem {
    int chance;
    ItemStack item;
    int min, max;

    public RandomItem(int newChance, ItemStack item, int newMin, int newMax) {
        chance = newChance;
        this.item = item;
        min = newMin;
        max = newMax;
    }

    public RandomItem(int newChance, Material mat, int newData, int newMin, int newMax) {
        chance = newChance;
        item = new ItemStack(mat, 1, (short) newData);
        min = newMin;
        max = newMax;
    }

    public ItemStack getItemStack() {
        item.setAmount((new Random().nextInt(Math.max(1, (max - min) + 1)) + min));
        return item;
    }

    public boolean hasChance() {
        return new Random().nextInt(chance) == 0;
    }
}