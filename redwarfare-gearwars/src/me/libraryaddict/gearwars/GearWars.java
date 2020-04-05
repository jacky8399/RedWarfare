package me.libraryaddict.gearwars;

import me.libraryaddict.gearwars.commands.CommandForceStart;
import me.libraryaddict.gearwars.commands.CommandGoto;
import me.libraryaddict.gearwars.commands.CommandTime;
import me.libraryaddict.gearwars.listeners.GeneralListener;
import me.libraryaddict.gearwars.listeners.InventoryListener;
import me.libraryaddict.gearwars.listeners.PlayerListener;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import me.libraryaddict.gearwars.types.MapLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

public class GearWars extends JavaPlugin {

    public static GearWars _instance;

    public void onEnable() {
        _instance = this;
        GearApi.init(this);
        GearApi.getGameManager();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new GeneralListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        MapLoader.loadMap();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                GearApi.getGameManager().setWorld(Bukkit.getWorlds().get(0));
            }
        });
        ScoreboardManager.setDisplayName(DisplaySlot.SIDEBAR, ChatColor.DARK_AQUA + "Information");
        getCommand("forcestart").setExecutor(new CommandForceStart());
        getCommand("time").setExecutor(new CommandTime());
        getCommand("goto").setExecutor(new CommandGoto());
    }

    public static GearWars getInstance() {
        return _instance;
    }
}
