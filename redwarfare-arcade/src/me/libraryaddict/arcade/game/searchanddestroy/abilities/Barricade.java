package me.libraryaddict.arcade.game.searchanddestroy.abilities;

import me.libraryaddict.arcade.game.GameTeam;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.UUID;

public class Barricade
{
    private Block _block;
    private UUID _owner;
    private GameTeam _team;
    private boolean _active;

    public Barricade(Block block, UUID owner, GameTeam team) {
        _block = block;
        _owner = owner;
        _team = team;
        _active = true;
    }

    public void destroy() {
        getBlock().setType(Material.AIR);
        setActive(false);
    }

    public Block getBlock() {
        return _block;
    }

    public void setBlock(Block _block) {
        this._block = _block;
    }

    public UUID getOwner() {
        return _owner;
    }

    public void setOwner(UUID _owner) {
        this._owner = _owner;
    }

    public boolean isOwner(UUID uuid) {
        return _owner == uuid;
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
