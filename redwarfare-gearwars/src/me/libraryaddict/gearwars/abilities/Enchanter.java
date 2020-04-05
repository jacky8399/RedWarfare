package me.libraryaddict.gearwars.abilities;

import java.util.Random;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class Enchanter extends AbilityListener implements Disableable {

    private boolean isEnchantable(ItemStack itemstack) {
        Enchantment[] enchants = Enchantment.values();
        for (int i = 0; i < enchants.length; i++) {
            if (enchants[i].canEnchantItem(itemstack)) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Player crafter = (Player) event.getWhoClicked();
        if (hasAbility(crafter) && (event.getCursor() == null || event.getCursor().getType() == Material.AIR)) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
                crafter.sendMessage(ChatColor.RED + "You can not use shift click!");
            } else if (new Random().nextBoolean()) {
                ItemStack enchant = event.getCurrentItem();
                if (isEnchantable(enchant)) {
                    Enchantment[] enchants = Enchantment.values();
                    Enchantment enchanted = null;
                    while (enchanted == null || !enchanted.canEnchantItem(enchant)) {
                        enchanted = enchants[new Random().nextInt(enchants.length)];
                    }
                    enchant.addUnsafeEnchantment(enchanted,
                            new Random().nextInt(enchanted.getMaxLevel() > 3 ? 3 : enchanted.getMaxLevel()) + 1);
                    while (new Random().nextBoolean()) {
                        enchanted = null;
                        while (enchanted == null || !enchanted.canEnchantItem(enchant)) {
                            enchanted = enchants[new Random().nextInt(enchants.length)];
                        }
                        enchant.addUnsafeEnchantment(enchanted,
                                new Random().nextInt(enchanted.getMaxLevel() > 3 ? 3 : enchanted.getMaxLevel()) + 1);
                    }
                }
            }
        }
    }

    @Override
    public void registerListener() {
    }

    @Override
    public void unregisterListener() {
    }

}
