package me.libraryaddict.gearwars.misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.libraryaddict.gearwars.types.Kit;

public class Kits {

    public static List<Kit> getKits() {
        List<Kit> kits = new ArrayList<Kit>();
        {
            Kit archer = new Kit("Archer", new ItemStack(Material.BOW), 0);
            archer.setDescription("Bow and arrow with gold armor");
            archer.setArmor(new ItemStack(Material.GOLD_BOOTS), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(
                    Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_HELMET));
            archer.addItem(new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 64));
            kits.add(archer);
        }
        {
            Kit basic = new Kit("Basic", new ItemStack(Material.WOOD_SWORD), 0);
            basic.setDescription("Leather armor and a enchanted stone sword");
            basic.setArmor(new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(
                    Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET));
            ItemStack item1 = new ItemStack(Material.WOOD_SWORD);
            item1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
            basic.addItem(item1);
            kits.add(basic);
        }
        {
            Kit elemental = new Kit("Elemental", new ItemStack(Material.LAVA_BUCKET), 0);
            elemental.setDescription("Lava and water with iron armor");
            elemental.setArmor(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(
                    Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET));
            elemental.addItem(new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.LAVA_BUCKET), new ItemStack(
                    Material.LAVA_BUCKET), new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.WATER_BUCKET),
                    new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.BUCKET), new ItemStack(Material.BUCKET),
                    new ItemStack(Material.OBSIDIAN, 10));
            kits.add(elemental);
        }
        {
            Kit enchanter = new Kit("Enchanter", new ItemStack(Material.EXP_BOTTLE), 0);
            enchanter.setDescription("Chain armor with all", "the stuff a enchanter wants");
            enchanter.setArmor(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET));
            enchanter.addItem(new ItemStack(Material.IRON_SWORD), new ItemStack(Material.BOW), new ItemStack(Material.EXP_BOTTLE,
                    32), new ItemStack(Material.BOOKSHELF, 10), new ItemStack(Material.ENCHANTMENT_TABLE));
            kits.add(enchanter);
        }
        {
            Kit feast = new Kit("Feast", new ItemStack(Material.CAKE), 0);
            feast.setDescription("Tons of food and chain armor");
            feast.setArmor(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(
                    Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET));
            feast.addItem(new ItemStack(Material.GRILLED_PORK, 10), new ItemStack(Material.GOLDEN_APPLE, 5), new ItemStack(
                    Material.APPLE, 5), new ItemStack(Material.CAKE, 2));
            kits.add(feast);
        }
        {
            Kit healer = new Kit("Healer", new ItemStack(Material.POTION, 1, (short) 8197), 0);
            healer.setDescription("Regeneration and health potions", "with a leather helmet");
            healer.setArmor(new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(
                    Material.LEATHER_HELMET));
            healer.addItem(new ItemStack(Material.POTION, 10, (short) 8193), new ItemStack(Material.POTION, 10, (short) 8197));
            kits.add(healer);
        }
        {
            Kit magician = new Kit("Magician", new ItemStack(Material.ENDER_PEARL), 0);
            magician.setDescription("Enderpearls and golden apples", "with a diamond chestplate");
            magician.setArmor(new ItemStack(Material.AIR), new ItemStack(Material.AIR),
                    new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.AIR));
            magician.addItem(new ItemStack(Material.ENDER_PEARL, 10), new ItemStack(Material.GOLDEN_APPLE, 2));
            kits.add(magician);
        }
        {
            Kit miner = new Kit("Miner", new ItemStack(Material.IRON_PICKAXE), 0);
            miner.setDescription("Mining pick with iron armor");
            miner.setArmor(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(
                    Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET));
            ItemStack item1 = new ItemStack(Material.IRON_PICKAXE);
            item1.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
            item1.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
            miner.addItem(item1, new ItemStack(Material.COOKED_BEEF, 2));
            kits.add(miner);
        }
        {
            Kit overlord = new Kit("Overlord", new ItemStack(Material.IRON_CHESTPLATE), 0);
            overlord.setDescription("Mixture of iron and chain with", "leather with a iron sword and bow and arrow");
            overlord.setArmor(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(
                    Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET));
            overlord.addItem(new ItemStack(Material.IRON_SWORD), new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 20),
                    new ItemStack(Material.COOKED_BEEF, 5));
            kits.add(overlord);
        }
        {
            Kit suicider = new Kit("Suicider", new ItemStack(Material.TNT), 0);
            suicider.setDescription("Tnt with leather and diamond armor");
            suicider.setArmor(new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(
                    Material.LEATHER_CHESTPLATE), new ItemStack(Material.DIAMOND_HELMET));
            suicider.addItem(new ItemStack(Material.TNT, 10), new ItemStack(Material.FLINT_AND_STEEL, 2));
            kits.add(suicider);
        }
        {
            Kit swordsman = new Kit("Swordsman", new ItemStack(Material.STONE_SWORD), 0);
            swordsman.setDescription("Gold armor with leather chestplate", "and enchanted stone sword");
            swordsman.setArmor(new ItemStack(Material.GOLD_BOOTS), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(
                    Material.LEATHER_CHESTPLATE), new ItemStack(Material.GOLD_HELMET));
            ItemStack item1 = new ItemStack(Material.STONE_SWORD);
            item1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
            swordsman.addItem(item1);
            kits.add(swordsman);
        }
        {
            Kit tank = new Kit("Tank", new ItemStack(Material.IRON_CHESTPLATE), 0);
            tank.setDescription("Enchanted iron and leather armor");
            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
            boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
            leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
            chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
            helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            tank.setArmor(boots, leggings, chestplate, helmet);
            kits.add(tank);
        }
        {
            Kit trader = new Kit("Trader", new ItemStack(Material.EMERALD), 0);
            trader.setDescription("Chain armor with the", "items to trade with NPC's");
            trader.setArmor(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(
                    Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET));
            trader.addItem(new ItemStack(Material.MONSTER_EGG, 5, (short) 120), new ItemStack(Material.EMERALD, 32));
            kits.add(trader);
        }
        {
            Kit vegetarian = new Kit("Vegetarian", new ItemStack(Material.CARROT_ITEM), 0);
            vegetarian.setDescription("Chain armor and plenty", "of vegetarian food");
            vegetarian.setArmor(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET));
            vegetarian.addItem(new ItemStack(Material.MELON, 5), new ItemStack(Material.BREAD, 10), new ItemStack(
                    Material.COOKIE, 5), new ItemStack(Material.MUSHROOM_SOUP, 5), new ItemStack(Material.CAKE, 2));
            kits.add(vegetarian);
        }
        {
            Kit warrior = new Kit("Warrior", new ItemStack(Material.WOOD_SWORD), 0);
            warrior.setDescription("Iron armor with a wooden sword.");
            warrior.setArmor(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(
                    Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET));
            warrior.addItem(new ItemStack(Material.WOOD_SWORD), new ItemStack(Material.GOLDEN_APPLE, 2));
            kits.add(warrior);
        }
        {
            Kit wizard = new Kit("Wizard", new ItemStack(Material.POTION), 0);
            wizard.setDescription("Potions with leather and diamond armor");
            wizard.setArmor(new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(
                    Material.LEATHER_CHESTPLATE), new ItemStack(Material.DIAMOND_HELMET));
            wizard.addItem(new ItemStack(Material.POTION, 2, (short) 8201), new ItemStack(Material.POTION, 2, (short) 8226),
                    new ItemStack(Material.POTION, 2, (short) 8197), new ItemStack(Material.POTION, 5, (short) 16396));
            kits.add(wizard);
        }
        {
            Kit zoo = new Kit("Zoo", new ItemStack(Material.MONSTER_EGG, 1, (short) 52), 0);
            zoo.setDescription("Tons of mob eggs and leather armor");
            zoo.setArmor(new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(
                    Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET));
            zoo.addItem(new ItemStack(Material.MONSTER_EGG, 5, (short) 50), new ItemStack(Material.MONSTER_EGG, 5, (short) 51),
                    new ItemStack(Material.MONSTER_EGG, 5, (short) 52), new ItemStack(Material.MONSTER_EGG, 5, (short) 54),
                    new ItemStack(Material.MONSTER_EGG, 2, (short) 55), new ItemStack(Material.MONSTER_EGG, 5, (short) 57),
                    new ItemStack(Material.MONSTER_EGG, 5, (short) 59), new ItemStack(Material.MONSTER_EGG, 5, (short) 50),
                    new ItemStack(Material.MONSTER_EGG, 2, (short) 61), new ItemStack(Material.MONSTER_EGG, 3, (short) 62),
                    new ItemStack(Material.MONSTER_EGG, 5, (short) 90), new ItemStack(Material.MONSTER_EGG, 3, (short) 91),
                    new ItemStack(Material.MONSTER_EGG, 5, (short) 92), new ItemStack(Material.MONSTER_EGG, 5, (short) 93),
                    new ItemStack(Material.MONSTER_EGG, 2, (short) 95), new ItemStack(Material.MONSTER_EGG, 5, (short) 58));
            kits.add(zoo);
        }

        return kits;
    }
}
