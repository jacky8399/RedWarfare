package me.libraryaddict.gearwars.managers;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.DamageApi;
import me.libraryaddict.gearwars.types.Ability;
import me.libraryaddict.gearwars.types.Gamer;
import me.libraryaddict.gearwars.types.Kit;
import me.libraryaddict.inventory.InventoryApi;
import me.libraryaddict.inventory.NamedInventory;
import me.libraryaddict.inventory.Page;
import me.libraryaddict.inventory.PageInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InventoryManager {
    private ItemStack abilityMenu;
    private ItemStack kitMenu;
    private ItemStack[] playerMenu = new ItemStack[0];
    private ItemStack spectatorMenu;
    private int itemCounter;

    public InventoryManager() {
        kitMenu = InventoryApi.setNameAndLore(new ItemStack(Material.FEATHER), ChatColor.BLUE + "Kits",
                ChatColor.GREEN + "Right click this to", ChatColor.GREEN + "open the kit selector!");
        abilityMenu = InventoryApi.setNameAndLore(new ItemStack(Material.NETHER_STAR), ChatColor.GOLD + "Abilities",
                ChatColor.YELLOW + "Right click this to", ChatColor.YELLOW + "open the ability selector!");
        spectatorMenu = InventoryApi.setNameAndLore(new ItemStack(Material.FEATHER), ChatColor.DARK_AQUA + "Gamers",
                ChatColor.AQUA + "Right click this to", ChatColor.AQUA + "open the spectators menu!");
    }

    public void addCounter() {
        itemCounter++;
    }

    public ArrayList<ItemStack> getAbilities(Player player) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        List<Ability> abilities = GearApi.getAbilityManager().getAbilities();
        for (Ability ability : abilities) {
            ItemStack item = ability.getIcon().clone();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + ability.getName());
            meta.setLore(Arrays.asList(ability.getDescription()));
            item.setItemMeta(meta);
            items.add(item);
        }
        items.add(InventoryApi.setNameAndLore(abilities.get(itemCounter % abilities.size()).getIcon(),
                ChatColor.translateAlternateColorCodes('&', "&1R&2a&3n&4d&5o&6m&7 &8A&9b&ai&bl&ci&dt&ey"),
                ChatColor.GOLD + "Use a random ability"));
        return items;
    }

    public ItemStack getAbilityMenu() {
        return abilityMenu;
    }

    private NamedInventory getInventory(Player player) {
        NamedInventory inv = new NamedInventory(player);
        ArrayList<ItemStack> aItems = getAbilities(player);
        inv.setPage(new Page("Abilities"), aItems);
        ArrayList<ItemStack> kItems = getKits(player);
        inv.setPage(new Page("Kits"), kItems);
        return inv;
    }

    public ItemStack getKitMenu() {
        return kitMenu;
    }

    public ArrayList<ItemStack> getKits(Player player) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        List<Kit> kits = GearApi.getKitManager().getKits();
        for (Kit kit : kits) {
            ItemStack item = kit.getIcon().clone();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + kit.getName());
            meta.setLore(kit.getDescription());
            item.setItemMeta(meta);
            items.add(item);
        }
        items.add(InventoryApi.setNameAndLore(kits.get(itemCounter % kits.size()).getIcon(),
                ChatColor.translateAlternateColorCodes('&', "&aR&ba&cn&dd&eo&1m&2 &3K&4i&5t"),
                ChatColor.GOLD + "Use a random kit"));
        return items;
    }

    private Material getCurrentMat() {
        switch (itemCounter % 5) {
            case 0:
                return Material.WOOD_SWORD;
            case 1:
                return Material.STONE_SWORD;
            case 2:
                return Material.GOLD_SWORD;
            case 3:
                return Material.IRON_SWORD;
            default:
                return Material.DIAMOND_SWORD;
        }
    }

    public ItemStack getSpectatorIcon() {
        return spectatorMenu;
    }

    public void openAbilityMenu(Player player) {
        NamedInventory inv = getInventory(player);
        inv.setPage("Abilities");
        inv.openInventory();
    }

    public void openKitMenu(Player player) {
        NamedInventory inv = getInventory(player);
        inv.setPage("Kits");
        inv.openInventory();
    }

    public void openSpectatorMenu(Player player) {
        PageInventory inv = new PageInventory(player);
        inv.setPages(playerMenu);
        inv.setTitle("Gamers");
        inv.openInventory();
    }

    public void updatePlayers() {
        ArrayList<Gamer> gamers = GearApi.getPlayerManager().getAliveGamers();
        playerMenu = new ItemStack[gamers.size()];
        ArrayList<String> names = new ArrayList<String>();
        for (Gamer gamer : gamers) {
            names.add(gamer.getName());
        }
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        int i = 0;
        for (String name : names) {
            for (Gamer gamer : gamers) {
                if (gamer.getName().equals(name)) {
                    ItemStack item = new ItemStack(gamer.getKit().getIcon());
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(
                            ChatColor.WHITE + ChatColor.getLastColors(gamer.getPlayer().getDisplayName()) + name);
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.BLUE + "Ability: " + ChatColor.RESET + (gamer.getAbility() != null ?
                            gamer.getAbility().getName() : "None"));
                    lore.add(ChatColor.GOLD + "Health: " + ChatColor.RESET + (int) Math
                            .ceil(gamer.getPlayer().getHealth()) + "/" + (int) gamer.getPlayer()
                            .getMaxHealth() + ChatColor.DARK_RED + " ❤");
                    lore.add(ChatColor.GOLD + "Hunger: " + ChatColor.RESET + gamer.getPlayer()
                            .getFoodLevel() + "/20 " + ChatColor.GOLD + ChatColor.ITALIC + "❦");
                    lore.add(
                            ChatColor.GOLD + "Armor: " + ChatColor.RESET + DamageApi.getArmorRating(gamer.getPlayer()));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    playerMenu[i] = item;
                    break;
                }
            }
            i++;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PageInventory inv = InventoryApi.getPageInventory(player);
            if (inv != null && inv.getTitle().equals("Gamers")) {
                inv.setPages(playerMenu);
            }
        }
    }
}
