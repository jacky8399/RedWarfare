package me.libraryaddict.arcade.game.searchanddestroy.kits;

import me.libraryaddict.arcade.game.searchanddestroy.abilities.BarricaderAbility;
import me.libraryaddict.arcade.kits.KitAvailibility;
import me.libraryaddict.core.inventory.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/**
 * Created by noracrouch0220 on 11/8/2017.
 */
public class KitBarricader extends SnDKit {

    public KitBarricader() {
        super("Barricader", KitAvailibility.Locked, new String[]
                {
                        "A defensive kit equipped with blocks that will gradually hurt nearby enemies. Those who break the blocks will be set ablaze!"
                }, new BarricaderAbility());

        setPrice(500);

        setItems(new ItemBuilder(Material.WOODEN_SWORD).addEnchantment(Enchantment.FIRE_ASPECT, 1).build(),
                new ItemBuilder(Material.STONE_PICKAXE).build(),
                new ItemBuilder(Material.TALL_GRASS).setAmount(3).build(),
                new ItemBuilder(Material.OAK_WOOD).setAmount(5).build(),
                new ItemBuilder(Material.STONE).setAmount(2).build(),
                new ItemBuilder(Material.COBWEB).setAmount(2).build());

    }

    @Override
    public Material[] getArmorMats() {
        return new Material[]
                {
                        Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET
                };
    }

    @Override
    public Material getMaterial() {
        return Material.OAK_WOOD;
    }
}
