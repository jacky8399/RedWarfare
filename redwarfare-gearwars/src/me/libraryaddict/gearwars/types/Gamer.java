package me.libraryaddict.gearwars.types;

import me.libraryaddict.gearwars.GearApi;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Gamer {
    private boolean isInvis;
    private boolean isSpectator;
    private Player player;
    private boolean isRandomAbility;

    public void setRandomAbility(boolean isRandom) {
        this.isRandomAbility = isRandom;
    }

    public boolean isRandomAbility() {
        return this.isRandomAbility;
    }

    public Gamer(Player player) {
        this.player = player;
        if (GearApi.getGameManager().getGTime() >= 0) {
            isSpectator = true;
        }
    }

    /**
     * If this player can see that gamer
     */
    public boolean canSee(Gamer gamer) {
        return !gamer.isInvis && (gamer.isAlive() || !isAlive());
    }

    /**
     * Set their width and length to 0, Makes arrows move through them
     */
    public void setGhost() {
        EntityPlayer p = ((CraftPlayer) getPlayer()).getHandle();
        p.width = 0;
        p.length = 0;
        getPlayer().spigot().setCollidesWithEntities(false);
    }

    /**
     * Restore their width and length. Makes arrows hit them
     */
    public void setHuman() {
        EntityPlayer p = ((CraftPlayer) getPlayer()).getHandle();
        p.width = 0.6F;
        p.length = 1.8F;
        getPlayer().spigot().setCollidesWithEntities(true);
    }

    /**
     * Clears his inventory
     */
    public void clearInventory() {
        getPlayer().getInventory().setArmorContents(new ItemStack[4]);
        getPlayer().getInventory().clear();
        getPlayer().setItemOnCursor(new ItemStack(0));
    }

    public Ability getAbility() {
        return GearApi.getAbilityManager().getAbility(this);
    }

    public List<ItemStack> getInventory() {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (ItemStack item : getPlayer().getInventory().getContents())
            if (item != null && item.getType() != Material.AIR)
                items.add(item.clone());
        for (ItemStack item : getPlayer().getInventory().getArmorContents())
            if (item != null && item.getType() != Material.AIR)
                items.add(item.clone());
        if (getPlayer().getItemOnCursor() != null && getPlayer().getItemOnCursor().getType() != Material.AIR)
            items.add(getPlayer().getItemOnCursor().clone());
        return items;
    }

    public Kit getKit() {
        return GearApi.getKitManager().getKit(this.getPlayer());
    }

    public String getName() {
        return getPlayer().getName();
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAlive() {
        return !isSpectator() && GearApi.getGameManager().getGTime() >= 0;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public void setInvis(boolean invis) {
        isInvis = invis;
    }

    public void setSpectator(boolean spectator) {
        isSpectator = spectator;
        isInvis = false;
    }

    /**
     * Updates the invis for this player to see everyone else
     */
    public void updateOthersToSelf() {
        for (Gamer gamer : GearApi.getPlayerManager().getGamers())
            if (gamer != this)
                if (canSee(gamer))
                    getPlayer().showPlayer(gamer.getPlayer());
                else
                    getPlayer().hidePlayer(gamer.getPlayer());
    }

    /**
     * Updates the invis for everyone if they can see this player or not
     */
    public void updateSelfToOthers() {
        for (Gamer gamer : GearApi.getPlayerManager().getGamers())
            if (gamer != this)
                if (gamer.canSee(this)) {
                    gamer.getPlayer().showPlayer(getPlayer());
                } else {
                    gamer.getPlayer().hidePlayer(getPlayer());
                }
    }
}
