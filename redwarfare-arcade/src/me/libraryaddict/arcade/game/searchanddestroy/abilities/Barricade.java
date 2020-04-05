package me.libraryaddict.arcade.game.searchanddestroy.abilities;

import me.libraryaddict.arcade.game.GameTeam;
import me.libraryaddict.core.utils.UtilTime;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Barricade
{
    private Block _block;
    private Player _owner;
    private GameTeam _team;
    private boolean _active;
    private int _regenTime = 0;

    public Barricade(Block block, Player owner, GameTeam team) {
        _block = block;
        _owner = owner;
        _team = team;
        _active = true;
    }

    public void destroy() {
        getBlock().setType(Material.AIR);
        _active = false;
        _regenTime = 0;
    }

    public void regenerate() {

    }

    public Block getBlock() {
        return _block;
    }

    public void setBlock(Block _block) {
        this._block = _block;
    }

    public Player getOwner() {
        return _owner;
    }

    public void setOwner(Player _owner) {
        this._owner = _owner;
    }

    public boolean isOwner(UUID uuid) {
        return _owner.getUniqueId() == uuid;
    }

    public void setRegenTime(int delay) {
        _regenTime = UtilTime.currentTick + delay;
    }

    public int getRegenTime() {
        return _regenTime;
    }

    public GameTeam getTeam() {
        return _team;
    }

    public void setTeam(GameTeam _team) {
        this._team = _team;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean _active) {
        this._active = _active;
    }
}
