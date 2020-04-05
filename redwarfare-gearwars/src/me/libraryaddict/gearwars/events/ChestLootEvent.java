package me.libraryaddict.gearwars.events;

import me.libraryaddict.gearwars.managers.ChestManager.ChestTier;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChestLootEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private ChestTier chestTier;

    private Gamer whoOpened;

    public ChestLootEvent(Gamer whoOpened, ChestTier chestTier) {
        this.whoOpened = whoOpened;
        this.chestTier = chestTier;
    }

    public ChestTier getChestTier() {
        return chestTier;
    }

    public Gamer getGamer() {
        return whoOpened;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public void setTier(ChestTier newTier) {
        chestTier = newTier;
    }
}
