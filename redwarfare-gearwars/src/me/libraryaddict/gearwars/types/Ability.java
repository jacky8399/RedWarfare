package me.libraryaddict.gearwars.types;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class Ability {
    private String[] abilityDescription;
    private ItemStack abilityIcon;
    private String abilityName;
    private AbilityListener listener;
    private int price;

    public Ability(Class abilityClass, String abilityName, ItemStack abilityIcon, int price, String... description) {
        try {
            listener = (AbilityListener) abilityClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.abilityIcon = abilityIcon;
        this.abilityName = abilityName;
        for (int i = 0; i < description.length; i++) {
            description[i] = ChatColor.WHITE + description[i];
        }
        this.abilityDescription = description;
        this.price = price;
    }

    public String[] getDescription() {
        return this.abilityDescription;
    }

    public ItemStack getIcon() {
        return abilityIcon;
    }

    public AbilityListener getListener() {
        return listener;
    }

    public String getName() {
        return abilityName;
    }

    public int getPrice() {
        return price;
    }

    public boolean isFree() {
        return price <= 0;
    }
}
