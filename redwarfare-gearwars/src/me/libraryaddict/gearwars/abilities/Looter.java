package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.events.ChestLootEvent;
import me.libraryaddict.gearwars.managers.ChestManager.ChestTier;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.event.EventHandler;

public class Looter extends AbilityListener implements Disableable {

    @EventHandler
    public void onChestLoot(ChestLootEvent event) {
        if (hasAbility(event.getGamer())) {
            event.setTier(ChestTier.SPECIAL);
        }
    }

    @Override
    public void registerListener() {
    }

    @Override
    public void unregisterListener() {
    }

}
