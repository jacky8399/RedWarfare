package me.libraryaddict.gearwars.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.misc.Kits;
import me.libraryaddict.gearwars.types.Gamer;
import me.libraryaddict.gearwars.types.Kit;

public class KitManager {
    private List<Kit> kits = Kits.getKits();
    private HashMap<Player, HashMap<Kit, Long>> ownedKits = new HashMap<Player, HashMap<Kit, Long>>();
    private HashMap<Player, Kit> usedKit = new HashMap<Player, Kit>();

    public Kit getKit(Player player) {
        if (!usedKit.containsKey(player)) {
            return getKit("Basic");
        }
        return usedKit.get(player);
    }

    public Kit getKit(String kitName) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(kitName)) {
                return kit;
            }
        }
        return null;
    }

    public List<Kit> getKits() {
        return kits;
    }

    public void giveoutKits() {
        for (Gamer gamer : GearApi.getPlayerManager().getGamers()) {
            getKit(gamer.getPlayer()).giveKit(gamer.getPlayer());
        }
    }

    public boolean ownsKit(Player p, Kit kit) {
        return kit.isFree() || (ownedKits.containsKey(p) && ownedKits.get(p).containsKey(kit));
    }

    public void removeKit(Player player) {
        usedKit.remove(player);
        ownedKits.remove(player);
    }

    public void setKit(Player player, Kit kit) {
        while (kit == null) {
            Kit k = kits.get(new Random().nextInt(kits.size()));
            if (ownsKit(player, k)) {
                kit = k;
            }
        }
        usedKit.put(player, kit);
    }
}
