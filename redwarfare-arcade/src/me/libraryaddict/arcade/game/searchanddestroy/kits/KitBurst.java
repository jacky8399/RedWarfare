package me.libraryaddict.arcade.game.searchanddestroy.kits;

import me.libraryaddict.arcade.game.searchanddestroy.abilities.BurstAbility;
import me.libraryaddict.arcade.kits.KitAvailibility;
import me.libraryaddict.core.inventory.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/**
 * Created by noracrouch0220 on 11/11/2017.
 */
public class KitBurst extends SnDKit{

    public KitBurst() {
        super("Burst", KitAvailibility.Purchase, new String[] {
                "Find yourself shooting arrows, only to see no excitement? Well with burst, you finally get to see that happen! Shoot an arrow, it explodes on impact! Be careful, as your fingers are fragile and can't handle the extreme danger these arrows hold."
        }, new BurstAbility());

        setPrice(150);

        setItems(new ItemBuilder(Material.STONE_SWORD).build(),
                new ItemBuilder(Material.BOW).addEnchantment(Enchantment.ARROW_INFINITE, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build(),
                new ItemBuilder(Material.ARROW).build());
    }

    @Override
    public Material[] getArmorMats() {
        return new Material[] {
                Material.DIAMOND_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.DIAMOND_HELMET
        };
    }

    @Override
    public Material getMaterial() {
        return Material.FIREWORK_ROCKET;
    }
}
