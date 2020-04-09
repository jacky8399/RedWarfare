package me.libraryaddict.core.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedParticle;
import me.libraryaddict.core.data.ParticleColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Collection;

public class UtilParticle {
    private static PacketConstructor _packetConstructor;

    public enum ViewDist {
        LONG(48),
        LONGER(96),
        MAX(256),
        NORMAL(24),
        SHORT(8);

        private int _dist;

        ViewDist(int dist) {
            this._dist = dist;
        }

        public int getDist() {
            return this._dist;
        }
    }

    static {
        try {
            _packetConstructor =
                    ProtocolLibrary.getProtocolManager().createPacketConstructor(PacketType.Play.Server.WORLD_PARTICLES, WrappedParticle.create(Particle.BARRIER, null), true, 0.0D,
                            0.0D, 0.0D, 0.0F, 0.0F,
                            0.0F, 0.0F, 0);
        } catch (Exception ex) {
            UtilError.handle(ex);
        }
    }

    private static PacketContainer getPacket(Particle particle, Location location, double offsetX, double offsetY,
                                             double offsetZ, double speed, int count, boolean displayFar,
                                             Material material) {
        return _packetConstructor
                .createPacket(WrappedParticle.create(particle, (material != null) ?
                                WrappedBlockData.createData(material) : null), displayFar,
                        location.getX(), location.getY(),
                        location.getZ(), (float) offsetX,
                        (float) offsetY, (float) offsetZ, (float) speed,
                        count);
    }

    public static void playParticle(Particle particle, Location location, double offsetX, double offsetY,
                                    double offsetZ, double speed, int count, ViewDist dist, Material material,
                                    Player... players) {
        PacketContainer packet = getPacket(particle, location, offsetX, offsetY, offsetZ, speed, count, true, material);
        for (Player player : players) {
            if (player.getWorld() == location.getWorld())
                if (UtilLoc.getDistance(player.getLocation(), location) <= dist.getDist())
                    UtilPlayer.sendPacket(player, packet);
        }
    }

    public static void playParticle(Location location, ParticleColor color) {
        playParticle(location, color, ViewDist.NORMAL, UtilPlayer.getPlayers().<Player>toArray(new Player[0]));
    }

    public static void playParticle(Location location, ParticleColor color, Player... players) {
        playParticle(location, color, ViewDist.NORMAL, players);
    }

    public static void playParticle(Location location, ParticleColor color, ViewDist viewDist) {
        playParticle(Particle.REDSTONE, location, color.getColors()[0], color.getColors()[1], color.getColors()[2],
                1.0D, 0, viewDist,
                UtilPlayer.getPlayers().toArray(new Player[0]));
    }

    public static void playParticle(Location location, ParticleColor color, ViewDist viewDist, Player... players) {
        playParticle(Particle.REDSTONE, location, color.getColors()[0], color.getColors()[1], color.getColors()[2],
                1.0D, 0, viewDist, players);
    }

    public static void playParticle(Particle type, Location location) {
        playParticle(type, location, 1);
    }

    public static void playParticle(Particle type, Location location, double offsetX, double offsetY, double offsetZ) {
        playParticle(type, location, offsetX, offsetY, offsetZ, 1, ViewDist.NORMAL);
    }

    public static void playParticle(Particle Particle, Location loc, double offsetX, double offsetY,
                                    double offsetZ, double speed, int count) {
        playParticle(Particle, loc, offsetX, offsetY, offsetZ, speed, count, ViewDist.NORMAL);
    }

    public static void playParticle(Particle Particle, Location loc, double offsetX, double offsetY,
                                    double offsetZ, double speed, int count, Material material) {
        playParticle(Particle, loc, offsetX, offsetY, offsetZ, speed, count, ViewDist.NORMAL, material);
    }

    public static void playParticle(Particle type, Location location, double offsetX, double offsetY, double offsetZ,
                                    double speed, int count, Player... players) {
        playParticle(type, location, offsetX, offsetY, offsetZ, speed, count, ViewDist.NORMAL, players);
    }

    public static void playParticle(Particle type, Location location, double offsetX, double offsetY, double offsetZ,
                                    double speed, int count, ViewDist dist) {
        playParticle(type, location, offsetX, offsetY, offsetZ, speed, count, dist, UtilPlayer.getPlayers());
    }

    public static void playParticle(Particle type, Location location, double offsetX, double offsetY, double offsetZ,
                                    double speed, int count, ViewDist dist, Material material) {
        playParticle(type, location, offsetX, offsetY, offsetZ, speed, count, dist, UtilPlayer.getPlayers(), material);
    }

    public static void playParticle(Particle particle, Location location, double offsetX, double offsetY,
                                    double offsetZ, double speed, int count, ViewDist dist,
                                    Collection<Player> players) {
        playParticle(particle, location, offsetX, offsetY, offsetZ, speed, count, dist,
                players.toArray(new Player[0]));
    }

    public static void playParticle(Particle particle, Location location, double offsetX, double offsetY,
                                    double offsetZ, double speed, int count, ViewDist dist,
                                    Collection<Player> players, Material material) {
        playParticle(particle, location, offsetX, offsetY, offsetZ, speed, count, dist, material,
                players.toArray(new Player[0]));
    }

    public static void playParticle(Particle type, Location location, double offsetX, double offsetY, double offsetZ,
                                    double speed, int count, ViewDist dist, Player... players) {
        playParticle(type, location, offsetX, offsetY, offsetZ, speed, count, dist, null, players);
    }

    public static void playParticle(Particle Particle, Location loc, double offsetX, double offsetY,
                                    double offsetZ, int count) {
        playParticle(Particle, loc, offsetX, offsetY, offsetZ, 0.0D, count);
    }

    public static void playParticle(Particle Particle, Location loc, double offsetX, double offsetY,
                                    double offsetZ, int count, Material material) {
        playParticle(Particle, loc, offsetX, offsetY, offsetZ, 0.0D, count, material);
    }

    public static void playParticle(Particle type, Location location, double offsetX, double offsetY, double offsetZ,
                                    int count, ViewDist dist) {
        playParticle(type, location, offsetX, offsetY, offsetZ, 0.0D, count, dist);
    }

    public static void playParticle(Particle type, Location location, double offsetX, double offsetY, double offsetZ,
                                    int count, ViewDist dist, Player... players) {
        playParticle(type, location, offsetX, offsetY, offsetZ, 0.0D, count, dist, players);
    }

    public static void playParticle(Particle type, Location location, int count) {
        playParticle(type, location, count, ViewDist.NORMAL);
    }

    public static void playParticle(Particle type, Location location, Player... players) {
        playParticle(type, location, 0.0D, 0.0D, 0.0D, 1, ViewDist.NORMAL, players);
    }

    public static void playParticle(Particle type, Location location, int count, ViewDist viewDist) {
        playParticle(type, location, 0.0D, 0.0D, 0.0D, count, viewDist);
    }

    public static void playParticle(Particle Particle, Location location, ViewDist viewDist) {
        playParticle(Particle, location, 1, viewDist);
    }
}
