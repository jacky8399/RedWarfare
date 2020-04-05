package me.libraryaddict.hub;

import me.libraryaddict.core.utils.UtilFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.libraryaddict.hub.managers.HubManager;

public class Hub extends JavaPlugin
{
    public void onEnable()
    {
        new HubManager(this);

        // UtilFile.delete(new File("world"));
        // UtilFile.extractZip(new File(UtilFile.getUpdateFolder(), "Maps/Hub/World.zip"), new File("world"));
    }
}
