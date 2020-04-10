package me.libraryaddict.arcade.game.searchanddestroy.abilities;

import java.util.UUID;
import me.libraryaddict.arcade.game.GameTeam;
import me.libraryaddict.core.utils.UtilTime;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Barricade {
    private Block _block;

    private Player _owner;

    private GameTeam _team;

    private boolean _active;

    private int _regenTime = 0;

    public Barricade(Block block, Player owner, GameTeam team) {
        this._block = block;
        this._owner = owner;
        this._team = team;
        this._active = true;
    }

    public void destroy() {
        getBlock().setType(Material.AIR);
        this._active = false;
        this._regenTime = 0;
    }

    public void regenerate() {}

    public Block getBlock() {
        return this._block;
    }

    public void setBlock(Block _block) {
        this._block = _block;
    }

    public Player getOwner() {
        return this._owner;
    }

    public void setOwner(Player _owner) {
        this._owner = _owner;
    }

    public boolean isOwner(UUID uuid) {
        return (this._owner.getUniqueId() == uuid);
    }

    public void setRegenTime(int delay) {
        this._regenTime = UtilTime.currentTick + delay;
    }

    public int getRegenTime() {
        return this._regenTime;
    }

    public GameTeam getTeam() {
        return this._team;
    }

    public void setTeam(GameTeam _team) {
        this._team = _team;
    }

    public boolean isActive() {
        return this._active;
    }

    public void setActive(boolean _active) {
        this._active = _active;
    }
}
