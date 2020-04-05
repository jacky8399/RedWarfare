package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class Jesus extends AbilityListener implements Disableable {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Location loc = p.getLocation();
        if (hasAbility(p)) {
            if (loc.getBlock().isLiquid()) {
                while (p.getLocation().getBlock().isLiquid()) {
                    p.teleport(loc.add(0, 1, 0));
                }
            }
            loc = p.getLocation();
            for (int x = -4; x <= 4; x++) {
                for (int y = -1; y <= 0; y++) {
                    for (int z = -4; z <= 4; z++) {
                        Block b = loc.clone().add(x, y, z).getBlock();
                        if (b.isLiquid()) {
                            p.sendBlockChange(b.getLocation(), Material.GLASS, (byte) 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void registerListener() {
    }

    @Override
    public void unregisterListener() {
    }
}
