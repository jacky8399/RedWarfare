package me.libraryaddict.gearwars.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Kit {
    private ItemStack[] armor = new ItemStack[0];
    private List<String> description = new ArrayList<String>();
    private ItemStack icon;
    private ItemStack[] items = new ItemStack[0];
    private String name;
    private int price;

    public Kit(String name, ItemStack icon, int price) {
        this.name = name;
        this.icon = icon;
        this.price = price;
    }

    public void addItem(ItemStack... items) {
        ItemStack[] newItems = new ItemStack[items.length + this.items.length];
        for (int i = 0; i < newItems.length; i++) {
            if (i < this.items.length) {
                newItems[i] = this.items[i];
            } else {
                newItems[i] = items[i - this.items.length];
            }
        }
        this.items = newItems;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public List<String> getDescription() {
        return description;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void giveKit(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.addItem(new ItemStack(Material.COMPASS));
        inv.setArmorContents(getArmor());
        for (ItemStack item : items) {
            if (item != null) {
                inv.addItem(item.clone());
            }
        }
    }

    public boolean isFree() {
        return price <= 0;
    }

    public void setArmor(ItemStack... armor) {
        this.armor = armor;
    }

    public void setDescription(String... description) {
        for (int i = 0; i < description.length; i++) {
            if (ChatColor.getLastColors(description[i]).equals("")) {
                description[i] = ChatColor.LIGHT_PURPLE + description[i];
            }
        }
        this.description = Arrays.asList(description);
    }
}
