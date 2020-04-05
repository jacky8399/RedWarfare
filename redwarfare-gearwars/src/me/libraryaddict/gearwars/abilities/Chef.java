package me.libraryaddict.gearwars.abilities;

import java.util.List;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Chef extends AbilityListener implements Disableable {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (hasAbility(event.getPlayer())) {
            if (event.getBlock().getType() == Material.SAPLING) {
                int data = event.getBlock().getData() & 7;
                TreeType treeType = TreeType.TREE;
                switch (data) {
                case 0:
                    break;
                case 1:
                    treeType = TreeType.REDWOOD;
                    break;
                case 2:
                    treeType = TreeType.BIRCH;
                    break;
                case 3:
                    treeType = TreeType.SMALL_JUNGLE;
                    break;
                case 4:
                    treeType = TreeType.ACACIA;
                    break;
                case 5:
                    treeType = TreeType.DARK_OAK;
                    break;
                default:
                    treeType = TreeType.TREE;
                }
                event.getBlock().getWorld().generateTree(event.getBlock().getLocation(), treeType);
            } else if (event.getBlock().getType() == Material.CROPS || event.getBlock().getType() == Material.CARROT
                    || event.getBlock().getType() == Material.POTATO) {
                event.getBlock().setData((byte) 7);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            if (hasAbility(event.getEntity().getKiller())) {
                List<ItemStack> items = event.getDrops();
                for (ItemStack item : items) {
                    switch (item.getType()) {
                    case RAW_BEEF:
                        item.setType(Material.COOKED_BEEF);
                        break;
                    case RAW_CHICKEN:
                        item.setType(Material.COOKED_CHICKEN);
                        break;
                    case PORK:
                        item.setType(Material.GRILLED_PORK);
                        break;
                    case ROTTEN_FLESH:
                        item.setType(Material.GOLDEN_APPLE);
                        break;
                    default:
                        break;
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
