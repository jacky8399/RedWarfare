package me.libraryaddict.gearwars.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.Abilities;
import me.libraryaddict.gearwars.types.Ability;
import me.libraryaddict.gearwars.types.Gamer;

public class AbilityManager {
    private List<Ability> abilities = Abilities.getAbilities();
    private HashMap<Player, HashMap<Ability, Long>> ownedAbilities = new HashMap<Player, HashMap<Ability, Long>>();

    public List<Ability> getAbilities() {
        return abilities;
    }

    public Ability getAbility(Gamer gamer) {
        for (Ability ability : abilities) {
            if (ability.getListener().hasAbility(gamer)) {
                return ability;
            }
        }
        return null;
    }

    public Ability getAbility(String abilityName) {
        for (Ability ability : abilities) {
            if (ability.getName().equalsIgnoreCase(abilityName)) {
                return ability;
            }
        }
        return null;
    }

    public boolean ownsAbility(Player p, Ability ability) {
        return ability.isFree() || (ownedAbilities.containsKey(p) && ownedAbilities.get(p).containsKey(ability));
    }

    public void registerAbilities() {
        for (Ability ability : abilities) {
            if (!ability.getListener().isDisableable() || !ability.getListener().getGamers().isEmpty()) {
                ability.getListener().registerListener();
                Bukkit.getPluginManager().registerEvents(ability.getListener(), GearApi.getMainPlugin());
            }
        }
    }

    public void removeGamer(Gamer gamer) {
        // TODO Fix the owned abilities
        ownedAbilities.remove(gamer.getPlayer());
        for (Ability ability : abilities) {
            ability.getListener().removeGamer(gamer);
        }
    }

    public void setAbility(Gamer gamer, Ability ability) {
        for (Ability a : abilities) {
            a.getListener().removeGamer(gamer);
        }
        gamer.setRandomAbility(false);
        while (ability == null) {
            Ability a = abilities.get(new Random().nextInt(abilities.size()));
            if (ownsAbility(gamer.getPlayer(), a)) {
                ability = a;
                gamer.setRandomAbility(true);
            }
        }
        ability.getListener().addGamer(gamer);
    }
}
