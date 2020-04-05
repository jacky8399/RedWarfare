package me.libraryaddict.gearwars.managers;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BorderManager {

    private int borderSize = Integer.MAX_VALUE;
    private int getTimesShrunk = -1;

    /**
     * Returns true if the spectator approached the border
     */
    public boolean doBorder(Gamer gamer, Location walkTo) {
        walkTo = walkTo.getBlock().getLocation().add(0.5, 0.5, 0.5);
        Location borderCenter = walkTo.getWorld().getSpawnLocation().clone();
        borderCenter.setY(walkTo.getY());
        double distFromSpawn = borderCenter.distance(walkTo);
        if (distFromSpawn + 10 > getBorderSize()) {
            if (distFromSpawn < getBorderSize()) {
                if (gamer.isAlive()) {
                    gamer.getPlayer().sendMessage(ChatColor.RED + "You are approaching the border!");
                }
            } else {
                if (gamer.isAlive()) {
                    if (gamer.getPlayer().getHealth() <= 2) {
                        GearApi.getPlayerManager().killGamer(gamer, gamer.getName() + " foolishly ran outside the border");
                        return false;
                    } else {
                        gamer.getPlayer().damage(0);
                        gamer.getPlayer().setHealth(gamer.getPlayer().getHealth() - 2);
                    }
                    gamer.getPlayer().sendMessage(ChatColor.RED + "You are beyond the border! Return or die!");
                } else {
                    return true;
                }
            }
            int checkRadius = gamer.isAlive() ? 10 : 1;
            for (int x = -checkRadius; x <= checkRadius; x++) {
                for (int y = -checkRadius; y <= checkRadius; y++) {
                    for (int z = -checkRadius; z <= checkRadius; z++) {
                        Location borderBlockLoc = walkTo.clone().add(x, y, z).getBlock().getLocation();
                        double blockY = borderBlockLoc.getY();
                        borderBlockLoc.setY(borderCenter.getY());
                        double blockDistanceFromSpawn = borderBlockLoc.distance(borderCenter);
                        borderBlockLoc.setY(blockY);
                        // If the block is a border block
                        if (blockDistanceFromSpawn >= getBorderSize() && blockDistanceFromSpawn < getBorderSize() + 1) {
                            // If the block is a valid block. And the dist is 5 blocks or so of the player.
                            if (borderBlockLoc.getY() >= 0 && borderBlockLoc.getY() <= 256
                                    && walkTo.distance(borderBlockLoc) <= checkRadius) {
                                Block block = borderBlockLoc.getBlock();
                                switch (block.getType()) {
                                case ACTIVATOR_RAIL:
                                case AIR:
                                case BROWN_MUSHROOM:
                                case CARPET:
                                case CARROT:
                                case CROPS:
                                case DEAD_BUSH:
                                case DETECTOR_RAIL:
                                case FENCE_GATE:
                                case GOLD_PLATE:
                                case IRON_DOOR:
                                case IRON_PLATE:
                                case LADDER:
                                case LAVA:
                                case LEVER:
                                case LONG_GRASS:
                                case MELON_STEM:
                                case PORTAL:
                                case POTATO:
                                case POWERED_RAIL:
                                case PUMPKIN_STEM:
                                case RAILS:
                                case RED_MUSHROOM:
                                case RED_ROSE:
                                case REDSTONE_COMPARATOR_OFF:
                                case REDSTONE_COMPARATOR_ON:
                                case REDSTONE_TORCH_OFF:
                                case REDSTONE_TORCH_ON:
                                case REDSTONE_WIRE:
                                case SAPLING:
                                case SIGN_POST:
                                case SKULL:
                                case SNOW:
                                case STATIONARY_LAVA:
                                case STATIONARY_WATER:
                                case STONE_BUTTON:
                                case STONE_PLATE:
                                case SUGAR_CANE_BLOCK:
                                case TORCH:
                                case TRAP_DOOR:
                                case TRIPWIRE:
                                case TRIPWIRE_HOOK:
                                case VINE:
                                case WALL_SIGN:
                                case WATER:
                                case WEB:
                                case WOOD_BUTTON:
                                case WOOD_DOOR:
                                case WOOD_PLATE:
                                case YELLOW_FLOWER:
                                    if (!gamer.isAlive())
                                        return true;
                                    if ((x + z + y) % 2 == 0) {
                                        gamer.getPlayer().playEffect(borderBlockLoc, Effect.STEP_SOUND, Material.BEDROCK.getId());
                                    }
                                    break;
                                default:
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public int getTimesShrunk() {
        return this.getTimesShrunk;
    }

    public void setBorderSize(int newBorderSize) {
        getTimesShrunk++;
        this.borderSize = newBorderSize;
    }
}
