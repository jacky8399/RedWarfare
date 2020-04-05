package me.libraryaddict.gearwars.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import me.libraryaddict.core.CentralManager;
import me.libraryaddict.core.ServerType;
import me.libraryaddict.core.command.CommandManager;
import me.libraryaddict.core.server.ServerManager;
import me.libraryaddict.core.utils.UtilError;
import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.GearWars;
import me.libraryaddict.gearwars.events.TimeSecondEvent;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import me.libraryaddict.gearwars.types.Gamer;
import me.libraryaddict.inventory.InventoryApi;
import me.libraryaddict.inventory.NamedInventory;
import me.libraryaddict.inventory.Page;
import me.libraryaddict.network.ServerInfo;
import me.libraryaddict.redis.operations.RedisPublishServerInfo;
import org.bukkit.*;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GameManager extends CentralManager{
    private int currentTime = -180;
    private boolean gameOver;
    private HashMap<Location, EntityType> toSpawn = new HashMap<Location, EntityType>();
    private World world;
    private ServerManager _serverManager;
    private CommandManager _commandManager;
    String _ip;

    public GameManager() {
        super(GearWars.getInstance());

        _commandManager = new CommandManager(GearWars.getInstance());
        _serverManager = new ServerManager(GearWars.getInstance(), _commandManager);

        ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.GREEN + "Game starts", -getGTime());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(GearApi.getMainPlugin(), new Runnable() {
            private long time = System.currentTimeMillis();
            int i = 0;

            public void run() {
                if (getGTime() < 0 && i++ % 4 == 0) {
                    GearApi.getInventoryManager().addCounter();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        NamedInventory inv = InventoryApi.getNamedInventory(p);
                        if (inv != null) {
                            if (inv.getTitle().equalsIgnoreCase("Kits")) {
                                inv.setPage(new Page("Kits"), GearApi.getInventoryManager().getKits(p));
                            } else if (inv.getTitle().equalsIgnoreCase("Abilities")) {
                                inv.setPage(new Page("Abilities"), GearApi.getInventoryManager().getAbilities(p));
                            }
                        }
                    }
                }
                if (System.currentTimeMillis() >= time) {
                    time = time + 1000;
                    onSecond();
                    if (!isGameOver()) {
                        Bukkit.getPluginManager().callEvent(new TimeSecondEvent());
                        sendInfo();
                    }
                }
            }
        }, 0, 1L);
    }

    public void checkWinner() {
        if (isGameOver() || getGTime() < 0) {
            return;
        }
        ArrayList<Gamer> gamers = GearApi.getPlayerManager().getAliveGamers();
        if (gamers.size() == 1) {
            Gamer winner = gamers.get(0);
            final String winnerName = winner.getName();
            gameOver = true;
            new BukkitRunnable() {
                int times = 0;

                public void run() {
                    Bukkit.broadcastMessage(ChatColor.GOLD + winnerName + " won!");
                    times++;
                    if (times > 4) {
                        new BukkitRunnable() {
                            public void run() {
                                int i = 0;
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.kickPlayer(ChatColor.GOLD + winnerName + " won!");
                                    if (i++ >= 2) {
                                        return;
                                    }
                                }
                                Bukkit.shutdown();
                            }
                        }.runTaskTimer(GearApi.getMainPlugin(), 0, 0);
                        cancel();
                    }
                }
            }.runTaskTimer(GearApi.getMainPlugin(), 0, 60);
        }
        sendInfo();
    }

    public int getGTime() {
        return currentTime;
    }

    public String getGTime(int i) {
        i = Math.abs(i);
        int remainder = i % 3600, minutes = remainder / 60, seconds = remainder % 60;
        String time = "";
        if (minutes > 0) {
            time += minutes + " minute";
            if (minutes > 1)
                time += "s";
        }
        if (seconds > 0) {
            if (minutes > 0)
                time += ", ";
            time += seconds + " second";
            if (seconds > 1)
                time += "s";
        }
        if (time.equals(""))
            time = "no time at all";
        return time;
    }

    public World getWorld() {
        return world;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void onSecond() {
        if (isGameOver()) {
            return;
        }
        setTime(getGTime() + 1);
        if (getGTime() <= 0) {
            if (Bukkit.getOnlinePlayers().size() == 0) {
                setTime(-180);
            } else if (getGTime() > -30 && Bukkit.getOnlinePlayers().size() <= 1) {
                setTime(-30);
            } else if (getGTime() < 0
                    && (getGTime() % 60 == 0 || (getGTime() >= -30 && getGTime() % 15 == 0) || getGTime() == -10 || getGTime() >= -5)) {
                Bukkit.broadcastMessage(ChatColor.RED + "Game starting in " + getGTime(getGTime()));
                if (getGTime() >= -5) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getEyeLocation(), Sound.BLOCK_LAVA_POP, 1, 2);
                }
            }
            ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.GREEN + "Game starts", -getGTime());
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setLevel(-getGTime());
            }
            if (getGTime() == 0) {
                startGame();
            }
        }
        if (getGTime() >= 0 && getGTime() <= 120) {
            switch (getGTime()) {
            case 0:
            case 60:
            case 90:
            case 105:
            case 110:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
                Bukkit.broadcastMessage(ChatColor.RED + "Invincibility wears off in " + getGTime(120 - getGTime()) + "!");
                ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.GOLD + "Invincibility", 120 - getGTime());
                break;
            case 120:
                Bukkit.broadcastMessage(ChatColor.RED + "Invincibility has worn off!");
                ScoreboardManager.hideScore(DisplaySlot.SIDEBAR, ChatColor.GOLD + "Invincibility");
                break;
            default:
                ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.GOLD + "Invincibility", 120 - getGTime());
                break;
            }
        }
        if (getGTime() >= 0) {
            GearApi.getInventoryManager().updatePlayers();
            GearApi.getPlayerManager().checkPlayers();
        }
        if (getGTime() > 120) {
            if (getGTime() >= 60 * 60) {
                for (Gamer gamer : GearApi.getPlayerManager().getAliveGamers()) {
                    gamer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 4), true);
                }
            } else {
                int time = (60 * 15) - (getGTime() % (60 * 15));
                if (time == 60 * 15) {
                    ScoreboardManager.hideScore(DisplaySlot.SIDEBAR, ChatColor.GOLD + "Teleport");
                } else if (time <= 300) {
                    ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.GOLD + "Teleport", time);
                }
                switch (time) {
                case (60 * 15):
                    playLightning();
                    Bukkit.broadcastMessage(ChatColor.RED + "Everyone has been teleported to spawn!");
                    for (Entity entity : getWorld().getEntities()) {
                        if (entity instanceof EnderPearl) {
                            entity.remove();
                        }
                    }
                    for (Gamer gamer : GearApi.getPlayerManager().getGamers()) {
                        GearApi.getPlayerManager().sendToSpawn(gamer.getPlayer(), 15);
                    }
                    int borderSize = GearApi.getBorderManager().getBorderSize();
                    int shrunk = GearApi.getBorderManager().getTimesShrunk();
                    switch (shrunk) {
                    case 0:
                        borderSize /= 1.6;
                    case 1:
                        borderSize /= 2;
                    default:
                        borderSize = 30;
                        break;
                    }
                    GearApi.getBorderManager().setBorderSize(borderSize);
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 10:
                case 30:
                case 60:
                case 120:
                case 180:
                case 300:
                    Bukkit.broadcastMessage(ChatColor.RED + "Teleport to spawn in " + getGTime(time) + "!");
                    break;
                default:
                    break;
                }
            }
        }
        for (Gamer gamer : GearApi.getPlayerManager().getGamers()) {
            Location loc = gamer.getPlayer().getLocation();
            boolean returns = GearApi.getBorderManager().doBorder(gamer, loc);
            if (returns) {
                Vector vec = loc.toVector().subtract(getWorld().getSpawnLocation().toVector());
                vec.normalize();
                vec.multiply((loc.distance(getWorld().getSpawnLocation()) - GearApi.getBorderManager().getBorderSize()) + 2);
                Location loc1 = loc.clone().subtract(vec);
                gamer.getPlayer().teleport(loc1);
                gamer.getPlayer().sendMessage(ChatColor.RED + "You cannot approach the border!");
            }
        }
    }

    public void playLightning() {
        getWorld().playSound(getWorld().getSpawnLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 50000, 1.2F);
    }

    public void setTime(int newTime) {
        currentTime = newTime;
    }

    public void setWorld(World world2) {
        this.world = world2;
        Bukkit.setSpawnRadius(0);
    }

    public void startGame() {
        ScoreboardManager.hideScore(DisplaySlot.SIDEBAR, ChatColor.GREEN + "Game starts");
        setTime(0);
        playLightning();
        for (Gamer gamer : GearApi.getPlayerManager().getGamers()) {
            gamer.getPlayer().setAllowFlight(false);
            GearApi.getPlayerManager().sendToSpawn(gamer.getPlayer(), 10);
            gamer.clearInventory();
            if (gamer.isRandomAbility()) {
                gamer.getPlayer().sendMessage(
                        ChatColor.DARK_GREEN + "You are using the ability: " + ChatColor.GREEN + gamer.getAbility().getName());
            }
        }
        getWorld().setGameRuleValue("doMobSpawning", "true");
        ScoreboardManager.makeScore(DisplaySlot.SIDEBAR, ChatColor.GOLD + "Invincibility", 120 - getGTime());
        GearApi.getKitManager().giveoutKits();
        GearApi.getAbilityManager().registerAbilities();
        Bukkit.broadcastMessage(ChatColor.RED + "The game has started!");
        for (Location loc : this.toSpawn.keySet()) {
            loc.getWorld().spawnEntity(loc, toSpawn.get(loc));
        }
        toSpawn.clear();
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(GearApi.getMainPlugin(), ListenerPriority.HIGH, PacketType.Play.Server.ENTITY_METADATA) {
                    public void onPacketSending(PacketEvent event) {
                        Player observer = event.getPlayer();
                        StructureModifier<Entity> entityModifer = event.getPacket().getEntityModifier(observer.getWorld());
                        org.bukkit.entity.Entity entity = entityModifer.read(0);
                        if (entity instanceof Player) {
                            Gamer gamer = GearApi.getPlayerManager().getGamer(entity);
                            if (gamer == null || gamer.isSpectator()) {
                                Iterator<WrappedWatchableObject> itel = event.getPacket().getWatchableCollectionModifier()
                                        .read(0).iterator();
                                while (itel.hasNext()) {
                                    WrappedWatchableObject watch = itel.next();
                                    if (watch.getIndex() == 0) {
                                        byte b = (Byte) watch.getValue();
                                        watch.setValue((byte) (b | 1 << 5));
                                    }
                                }
                            }
                        }
                    }
                });
        sendInfo();
    }

    public void storeMob(Location loc, EntityType type) {
        while (toSpawn.containsKey(loc)) {
            loc.add(0, 0.000001F, 0);
        }
        toSpawn.put(loc, type);
    }



    private void sendInfo() {
        sendInfo(0);
    }

    private void sendInfo(int playersMod) {

        ServerInfo.ServerState state;

        if (currentTime > 0) {
            state = ServerInfo.ServerState.IN_PROGRESS;
        }
        else if (isGameOver()) {
            state = ServerInfo.ServerState.ENDED;
        }
        else {
            state = ServerInfo.ServerState.JOINABLE;
        }

        ServerInfo serverInfo = new ServerInfo(state, ServerManager.getServerName(), _serverManager.getGameType(),
                Bukkit.getOnlinePlayers().size(), currentTime, _serverManager.getIP() + ":" + Bukkit.getPort(),
                false);

        new BukkitRunnable() {
            public void run() {
                new RedisPublishServerInfo(serverInfo);
            }
        }.runTaskAsynchronously(GearApi.getMainPlugin());
    }
}
