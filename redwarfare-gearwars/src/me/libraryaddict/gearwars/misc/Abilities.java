package me.libraryaddict.gearwars.misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.libraryaddict.gearwars.abilities.*;
import me.libraryaddict.gearwars.types.Ability;

public class Abilities {

    public static List<Ability> getAbilities() {
        List<Ability> abilities = new ArrayList<Ability>();
        abilities.add(new Ability(Allergies.class, "Allergies", new ItemStack(Material.SEEDS), 0, "Once invincibility runs out",
                "you gain a strength boost!", "But walking on grass or", "being hit with a flower will", "severely damage you!"));
        abilities.add(new Ability(AntiWitch.class, "AntiWitch", new ItemStack(Material.POTION, 1, (short) 16384), 0,
                "Hit someone to remove all", "the positive potion effects on them!"));
        abilities.add(new Ability(Blazer.class, "Blazer", new ItemStack(Material.BLAZE_POWDER), 0,
                "Trail a blaze for others to follow!", "Fire spreads whereever you walk", "you are also immune to fire!"));
        abilities.add(new Ability(BoomBox.class, "BoomBox", new ItemStack(Material.WOOL, 1, (short) 15), 0,
                "Place your boom box..", "Hit it...", "EXPLODE!!!", "Did I mention that you live?"));
        abilities.add(new Ability(Camper.class, "Camper", new ItemStack(Material.POTION, 1, (short) 8193), 0,
                "Standing still will give you", "a regeneration effect!"));
        abilities.add(new Ability(Cannibal.class, "Cannibal", new ItemStack(Material.ROTTEN_FLESH), 0, "Hit other players to",
                "gain a 'food source' and to grant", "them the knowledge of hunger"));
        abilities.add(new Ability(Chameleon.class, "Chameleon", new ItemStack(Material.MONSTER_EGG, 1, (short) 92), 0,
                "Hit a mob to be disguised", "as the mob. When you take or give a attack",
                "or shoot a projectile. The disguise is blown!"));
        abilities.add(new Ability(Chef.class, "Chef", new ItemStack(Material.COOKED_BEEF), 0, "The animals you kill will",
                "drop edible meat! The trees", "you plant are instantly grown!"));
        abilities.add(new Ability(Counter.class, "Counter", new ItemStack(Material.DEAD_BUSH), 0, "When attacked, if you are",
                "blocking. The attacker will", "receive the same damage as you!"));
        abilities.add(new Ability(Creeper.class, "Creeper", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 0,
                "Explode on death"));
        abilities.add(new Ability(Demoman.class, "Demoman", new ItemStack(Material.TNT), 0, "Killed mobs drop tnt",
                "Hitting the tnt will explode it!"));
        abilities.add(new Ability(Enchanter.class, "Enchanter", new ItemStack(Material.EXP_BOTTLE), 0, "Crafting a item will",
                "grant it a random enchant!"));
        abilities.add(new Ability(EnderCast.class, "Endercast", new ItemStack(Material.ENDER_PEARL), 0,
                "Right click with your enderpearl to", "summon a villager at the place you look",
                "If the villager is hurt.. You are hurt!", "After approx 5 seconds.", "You teleport to the villager!"));
        abilities.add(new Ability(Explosive.class, "Explosive", new ItemStack(Material.SULPHUR), 0, "Shoot exploding arrows!"));
        abilities.add(new Ability(Flash.class, "Flash", new ItemStack(Material.POTION, 1, (short) 8194), 0,
                "When sprinting, you gain a speed boost!"));
        abilities.add(new Ability(Fraction.class, "Fraction", new ItemStack(Material.INK_SACK, 1, (short) 1), 0,
                "Right click someone with your", "redstone to share damage with them!"));
        abilities.add(new Ability(Ghost.class, "Ghost", new ItemStack(Material.GHAST_TEAR), 0,
                "Right click on blocks with your portals", "to make them disappear for 10 seconds!"));
        abilities.add(new Ability(GraveDigger.class, "GraveDigger", new ItemStack(Material.IRON_HOE), 0,
                "Use your book on someone then", "use it again once you have 3 kills", "to revive them back from the dead!"));
        abilities.add(new Ability(Hammerhead.class, "Hammerhead", new ItemStack(Material.DIAMOND_SWORD), 0,
                "Your weapons deal 2x damage!", "But you drop them after each use.."));
        abilities.add(new Ability(Hatcher.class, "Hatcher", new ItemStack(Material.MOB_SPAWNER), 0,
                "Spawn mobs from your spawners!", "They will not attack you!", "Change the mob type by using",
                "A monster egg on them!", "Obtain monster eggs by slaughtering other players!"));
        abilities.add(new Ability(Jesus.class, "Jesus", new ItemStack(Material.END_CRYSTAL), 0, "Walk on water!"));
        // abilities.add(new Ability(Leech.class, "Leech", new ItemStack(Material.REDSTONE), 0,
        // "Each hit on a enemy player heals you a little!"));
        abilities.add(new Ability(Looter.class, "Looter", new ItemStack(Material.CHEST), 0, "Chests have better loot!"));
        abilities.add(new Ability(Medic.class, "Medic", new ItemStack(Material.PAPER), 0,
                "Given 20 bandages to heal other players!"));
        abilities.add(new Ability(Monster.class, "Monster", new ItemStack(Material.SKULL_ITEM, 1, (short) 2), 0,
                "KO monsters and not be targeted!"));
        abilities.add(new Ability(Poseidon.class, "Poseidon", new ItemStack(Material.WATER_BUCKET), 0,
                "Strength boost in the water!"));
        abilities.add(new Ability(Rewind.class, "Rewind", new ItemStack(Material.WATCH), 0, "Teleport backwards in time",
                "every 30 seconds to your location", "30 seconds ago!"));
        abilities.add(new Ability(Salamander.class, "Salamander", new ItemStack(Material.SAND), 0, "Do not fear the lava!"));
        abilities.add(new Ability(Shadows.class, "Shadows", new ItemStack(Material.POTION, 1, (short) 8206), 0,
                "Stand still for 20 seconds to become", "completely invisible and unattackable!"));
        abilities.add(new Ability(Skinner.class, "Skinner", new ItemStack(Material.SKULL_ITEM, 1, (short) 3), 0,
                "Disguise as the player you kill!"));
        abilities.add(new Ability(Stripper.class, "Stripper", new ItemStack(Material.RED_ROSE), 0,
                "Right click someone with your flower in a", "attempt to seduce them to remove their armor!",
                "You will always remove your armor however.."));
        abilities.add(new Ability(Summoner.class, "Summoner", new ItemStack(Material.MONSTER_EGG, 1, (short) 96), 0,
                "Kill mobs and store them", "in your summoning book! Summon", "them again using your book!",
                "Left click air to toggle mobs", "Left click a block to summon!"));
        abilities.add(new Ability(Teamer.class, "Teamer", new ItemStack(Material.COMMAND), 0,
                "Form a team! Use the command /team!", "A team cannot hurt each other", "and will be granted bonuses",
                "if their team gets large enough!"));
        abilities.add(new Ability(Thor.class, "Thor", new ItemStack(Material.WOOD_AXE), 0, "Right click the ground with your",
                "wooden axe to summon lightning!"));
        abilities.add(new Ability(Thumper.class, "Thumper", new ItemStack(Material.DIAMOND_BOOTS), 0,
                "Hit the ground like a sack of ", "and convert that damage into strength!"));
        abilities.add(new Ability(Transporter.class, "Transporter", new ItemStack(Material.SPONGE), 0,
                "Place one sponge, use the other to", "instantly teleport to the first sponge!"));
        abilities.add(new Ability(Zombie.class, "Zombie", new ItemStack(Material.POTION, 1, (short) 8196), 0,
                "Each time you are attacked, you and", "everyone around you is poisoned!"));
        return abilities;
    }
}
