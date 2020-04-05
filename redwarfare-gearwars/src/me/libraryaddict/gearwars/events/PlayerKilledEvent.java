package me.libraryaddict.gearwars.events;

import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKilledEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private String deathMessage;
    private Gamer killed;
    private Gamer killer;

    public PlayerKilledEvent(Gamer killed, Gamer killer, String deathMessage) {
        this.killed = killed;
        this.killer = killer;
        this.deathMessage = deathMessage;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Gamer getKilled() {
        return killed;
    }

    public Gamer getKillerGamer() {
        return killer;
    }

    public void setDeathMessage(String newMessage) {
        deathMessage = newMessage;
    }
}
