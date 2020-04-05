package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

public class Skinner extends AbilityListener implements Disableable {

    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        if (event.getKillerGamer() != null && hasAbility(event.getKillerGamer())) {
            Gamer killer = event.getKillerGamer();
            Gamer killed = event.getKilled();
            PlayerDisguise playerDisguise = new PlayerDisguise(killer.getName());
            DisguiseAPI.disguiseToAll(killer.getPlayer(), playerDisguise);
            killer.getPlayer().sendMessage(
                    ChatColor.BLUE + "You quickly skin " + killed.getName() + "'s corpse and slip into their skin!");
            killed.getPlayer().sendMessage(
                    ChatColor.BLUE + "Slice, slash, cut. Your soul quickly departs your body.. Horrified at " + killer.getName()
                            + "'s actions");
        }
    }

    @Override
    public void registerListener() {
    }

    @Override
    public void unregisterListener() {
    }

}
