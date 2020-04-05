package me.libraryaddict.gearwars.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import me.libraryaddict.gearwars.types.Damage;
import me.libraryaddict.gearwars.types.Gamer;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PlayerManager {
    private static HashSet<Byte> nonSolid = new HashSet<Byte>();
    static {
        nonSolid.add((byte) 0);
        for (int b = 8; b < 12; b++)
            nonSolid.add((byte) b);
        nonSolid.add((byte) Material.SNOW.getId());
        nonSolid.add((byte) Material.LONG_GRASS.getId());
        nonSolid.add((byte) Material.RED_MUSHROOM.getId());
        nonSolid.add((byte) Material.RED_ROSE.getId());
        nonSolid.add((byte) Material.YELLOW_FLOWER.getId());
        nonSolid.add((byte) Material.BROWN_MUSHROOM.getId());
        nonSolid.add((byte) Material.SIGN_POST.getId());
        nonSolid.add((byte) Material.WALL_SIGN.getId());
        nonSolid.add((byte) Material.FIRE.getId());
        nonSolid.add((byte) Material.TORCH.getId());
        nonSolid.add((byte) Material.REDSTONE_WIRE.getId());
        nonSolid.add((byte) Material.REDSTONE_TORCH_OFF.getId());
        nonSolid.add((byte) Material.REDSTONE_TORCH_ON.getId());
        nonSolid.add((byte) Material.VINE.getId());
        nonSolid.add((byte) Material.LADDER.getId());
    }

    public static HashSet<Byte> getNonSolids() {
        return nonSolid;
    }

    public void checkPlayers() {
        try {
            for (Field field : EntityLiving.class.getFields()) {
                if (field.getType().getSimpleName().equals("CombatTracker")) {
                    for (Field entries : CombatTracker.class.getDeclaredFields()) {
                        if (entries.getType() == ArrayList.class) {
                            entries.setAccessible(true);
                            Iterator<Gamer> itel = lastDamager.keySet().iterator();
                            while (itel.hasNext()) {
                                Gamer gamer = itel.next();
                                if (!gamer.isAlive() || lastDamager.get(gamer).getTime() < System.currentTimeMillis()
                                        || !lastDamager.get(gamer).getDamager().isAlive()) {
                                    EntityPlayer eP = ((CraftPlayer) gamer.getPlayer()).getHandle();
                                    CombatTracker tracker = (CombatTracker) field.get(eP);
                                    if (tracker != null) {
                                        List<CombatEntry> entry = (List<CombatEntry>) entries.get(tracker);
                                        Iterator<CombatEntry> combatItel = entry.iterator();
                                        while (combatItel.hasNext()) {
                                            CombatEntry comb = combatItel.next();
                                            if (comb != null && comb.a() != null && comb.a().getEntity() instanceof EntityHuman) {
                                                combatItel.remove();
                                            }
                                        }
                                    }
                                    itel.remove();
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<Gamer> gamers = new ArrayList<Gamer>();

    private HashMap<Gamer, Damage> lastDamager = new HashMap<Gamer, Damage>();

    private String formatDeathMessage(String deathMessage, String toReplace, String playerName) {
        if (deathMessage.contains(toReplace + "'s")) {
            deathMessage = deathMessage.replaceFirst(toReplace + "'s", playerName + "'s" + ChatColor.YELLOW);
        } else {
            deathMessage = deathMessage.replaceFirst(toReplace, playerName + ChatColor.YELLOW);
        }
        return deathMessage;
    }

    public ArrayList<Gamer> getAliveGamers() {
        ArrayList<Gamer> list = new ArrayList<Gamer>();
        for (Gamer gamer : getGamers()) {
            if (gamer.isAlive()) {
                list.add(gamer);
            }
        }
        return list;
    }

    public Gamer getGamer(Entity entity) {
        for (Gamer gamer : gamers) {
            if (gamer.getPlayer() == entity) {
                return gamer;
            }
        }
        return null;
    }

    public Gamer getGamer(String name) {
        for (Gamer gamer : gamers) {
            if (gamer.getName().equals(name)) {
                return gamer;
            }
        }
        return null;
    }

    public ArrayList<Gamer> getGamers() {
        return gamers;
    }

    public Gamer getKiller(Gamer victim) {
        Damage dmg = lastDamager.get(victim);
        Gamer backup = null;
        if (dmg != null)
            if (dmg.getTime() >= System.currentTimeMillis())
                backup = dmg.getDamager();
        return backup;
    }

    public boolean isSolid(Block block) {
        return !nonSolid.contains((byte) block.getTypeId());
    }

    public void killGamer(Gamer gamer, String deathMessage) {
        killGamer(gamer, deathMessage, getKiller(gamer));
    }

    public void killGamer(Gamer gamer, String deathMessage, Gamer killer) {
        if (gamer == null || !gamer.isAlive() || GearApi.getGameManager().isGameOver() || GearApi.getGameManager().getGTime() < 0) {
            return;
        }
        if (killer == null) {
            killer = getKiller(gamer);
        }
        PlayerKilledEvent event = new PlayerKilledEvent(gamer, killer, deathMessage);
        Bukkit.getPluginManager().callEvent(event);
        GearApi.getAbilityManager().removeGamer(gamer);
        World world = gamer.getPlayer().getWorld();
        for (ItemStack item : gamer.getInventory()) {
            if (item != null && item.getType() != Material.AIR) {
                if (item.hasItemMeta()) {
                    world.dropItemNaturally(gamer.getPlayer().getLocation(), item.clone()).getItemStack()
                            .setItemMeta(item.getItemMeta());
                } else {
                    world.dropItemNaturally(gamer.getPlayer().getLocation(), item);
                }
            }
        }
        String msg = ChatColor.YELLOW + event.getDeathMessage();

        Bukkit.broadcastMessage(msg);
        sendToSpawn(gamer.getPlayer(), 5);
        setSpectator(gamer);
        ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.BLUE + "Players", getAliveGamers().size());
        GearApi.getGameManager().checkWinner();
        GearApi.getInventoryManager().updatePlayers();
        GearApi.getGameManager().playLightning();
        this.checkPlayers();
    }

    public Gamer registerGamer(Player player) {
        Gamer gamer = new Gamer(player);
        gamers.add(gamer);
        return gamer;
    }

    public void removeKilled(Gamer gamer) {
        lastDamager.remove(gamer);
        Iterator<Gamer> itel = lastDamager.keySet().iterator();
        while (itel.hasNext()) {
            Gamer g = itel.next();
            if (lastDamager.get(g).getDamager() == gamer)
                itel.remove();
        }
    }

    public void sendToSpawn(Player p, int radius) {
        Location center = GearApi.getGameManager().getWorld().getSpawnLocation();
        p.setFallDistance(0);
        Location playerLoc = null;
        p.leaveVehicle();
        p.eject();
        int chances = 0;
        while (playerLoc == null && chances < 500) {
            chances++;
            // While he has failed to find a location
            Location checkingSpawn = new Location(center.getWorld(), center.getX() + (new Random().nextInt(radius * 2) - radius),
                    center.getY() + new Random().nextInt(radius / 2), center.getZ() + (new Random().nextInt(radius * 2) - radius));
            // Get a random location
            if (!isSolid(checkingSpawn.getBlock()) && !isSolid(checkingSpawn.getBlock().getRelative(BlockFace.UP))) {
                // He just spawned in a place he can live.. If he could fly
                while (!isSolid(checkingSpawn.getBlock().getRelative(BlockFace.DOWN)) && checkingSpawn.getY() > 0) {
                    checkingSpawn.add(0, -1, 0);
                }
                if (checkingSpawn.getBlock().getRelative(BlockFace.DOWN).getType().name().contains("FENCE")
                        || checkingSpawn.getY() <= 1 || checkingSpawn.getBlock().isLiquid()) {
                    continue;
                }
                playerLoc = checkingSpawn.clone();
                break;
            }
        }
        if (playerLoc == null)
            playerLoc = center.clone();
        playerLoc.add(0.5, 0.4, 0.5);
        if (playerLoc.getBlock().getType() == Material.SNOW) {
            playerLoc.add(0, playerLoc.getBlock().getData() * 0.1, 0);
        }
        playerLoc.setYaw((float) -Math.abs(((Math.atan2(playerLoc.getBlockX() - center.getBlockX(), playerLoc.getBlockZ()
                - center.getBlockZ()) * 180) / Math.PI) - 180));
        p.teleport(playerLoc);
    }

    public void setDamager(Gamer hit, Damage dmg) {
        if (dmg.getDamager() != hit) {
            lastDamager.put(hit, dmg);
        }
    }

    public void setSpectator(Gamer gamer) {
        gamer.setSpectator(true);
        gamer.updateSelfToOthers();
        gamer.clearInventory();
        gamer.setGhost();
        removeKilled(gamer);
        final Player p = gamer.getPlayer();
        ((CraftPlayer) p).getHandle().b(0);
        p.setAllowFlight(true);
        p.setFoodLevel(20);
        p.setMaxHealth(20);
        p.setHealth(20);
        ScoreboardManager.addToTeam(p, "Spectators", ChatColor.DARK_GRAY + "", true);
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(GearApi.getMainPlugin(), new Runnable() {
            public void run() {
                p.getInventory().addItem(GearApi.getInventoryManager().getSpectatorIcon());
            }
        }, 18);
        for (Entity entity : p.getWorld().getEntities()) {
            if (entity instanceof Creature && ((Creature) entity).getTarget() == p) {
                ((Creature) entity).setTarget(null);
            }
        }
        refreshMeta(p);
    }

    public void refreshMeta(Player player) {
        PacketContainer packet = ProtocolLibrary
                .getProtocolManager()
                .createPacketConstructor(PacketType.Play.Server.ENTITY_METADATA, player.getEntityId(),
                        WrappedDataWatcher.getEntityWatcher(player), true)
                .createPacket(player.getEntityId(), WrappedDataWatcher.getEntityWatcher(player), true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.canSee(player))
                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
        }
    }

    public void unregisterGamer(Gamer gamer) {
        gamers.remove(gamer);
        removeKilled(gamer);
    }
}
