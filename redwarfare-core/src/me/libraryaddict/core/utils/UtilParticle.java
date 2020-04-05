package me.libraryaddict.core.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.libraryaddict.core.data.ParticleColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class UtilParticle {
    public enum ParticleType {
        ANGRY_VILLAGER(EnumWrappers.Particle.VILLAGER_ANGRY, "angryVillager"),

        BARRIER(EnumWrappers.Particle.BARRIER, "barrier"),

        BLACK_HEART(EnumWrappers.Particle.DAMAGE_INDICATOR, "damageIndicator"),

        BLOCK_CRACK(EnumWrappers.Particle.BLOCK_CRACK, "blockcrack") {
            @Override
            public String getParticle(Material type, int data) {
                return "blockcrack_" + type.getId() + "_" + data;
            }
        },

        BLOCK_DUST(EnumWrappers.Particle.BLOCK_DUST, "blockdust") {
            @Override
            public String getParticle(Material type, int data) {
                return "blockdust_" + type.getId() + "_" + data;
            }
        },

        BUBBLE(EnumWrappers.Particle.WATER_BUBBLE, "bubble"),

        CLOUD(EnumWrappers.Particle.CLOUD, "cloud"),

        CRIT(EnumWrappers.Particle.CRIT, "crit"),

        DEPTH_SUSPEND(EnumWrappers.Particle.SUSPENDED_DEPTH, "depthSuspend"),

        DRAGON_BREATH(EnumWrappers.Particle.DRAGON_BREATH, "dragonbreath"),

        DRIP_LAVA(EnumWrappers.Particle.DRIP_LAVA, "dripLava"),

        DRIP_WATER(EnumWrappers.Particle.DRIP_WATER, "dripWater"),

        DROPLET(EnumWrappers.Particle.WATER_DROP, "droplet"),

        ENCHANTMENT_TABLE(EnumWrappers.Particle.ENCHANTMENT_TABLE, "enchantmenttable"),

        FIREWORKS_SPARK(EnumWrappers.Particle.FIREWORKS_SPARK, "fireworksSpark"),

        FLAME(EnumWrappers.Particle.FLAME, "flame"),

        FOOTSTEP(EnumWrappers.Particle.FOOTSTEP, "footstep"),

        HAPPY_VILLAGER(EnumWrappers.Particle.VILLAGER_HAPPY, "happyVillager"),

        HEART(EnumWrappers.Particle.HEART, "heart"),

        HUGE_EXPLOSION(EnumWrappers.Particle.EXPLOSION_HUGE, "hugeexplosion"),

        ICON_CRACK(EnumWrappers.Particle.ITEM_CRACK, "iconcrack") {
            @Override
            public String getParticle(Material type, int data) {
                return "iconcrack_" + type.getId() + "_" + data;
            }
        },

        INSTANT_SPELL(EnumWrappers.Particle.SPELL_INSTANT, "instantSpell"),

        ITEM_TAKE(EnumWrappers.Particle.ITEM_TAKE, "take"),

        LARGE_CLOUD(EnumWrappers.Particle.EXPLOSION_NORMAL, "explode"),

        LARGE_EXPLODE(EnumWrappers.Particle.EXPLOSION_LARGE, "largeexplode"),

        LARGE_SMOKE(EnumWrappers.Particle.SMOKE_LARGE, "largesmoke"),

        LAVA(EnumWrappers.Particle.LAVA, "lava"),

        MAGIC_CRIT(EnumWrappers.Particle.CRIT_MAGIC, "magicCrit"),

        MOB_APPEARANCE(EnumWrappers.Particle.MOB_APPEARANCE, "mobappearance"),

        MOB_SPELL(EnumWrappers.Particle.SPELL_MOB, "mobSpell"),

        MOB_SPELL_AMBIENT(EnumWrappers.Particle.SPELL_MOB_AMBIENT, "mobSpellAmbient"),

        NOTE(EnumWrappers.Particle.NOTE, "note"),

        PORTAL(EnumWrappers.Particle.PORTAL, "portal"),

        RED_DUST(EnumWrappers.Particle.REDSTONE, "reddust"),

        SLIME(EnumWrappers.Particle.SLIME, "slime"),

        SMOKE(EnumWrappers.Particle.SMOKE_NORMAL, "smoke"),

        SNOW_SHOVEL(EnumWrappers.Particle.SNOW_SHOVEL, "snowshovel"),

        SNOWBALL_POOF(EnumWrappers.Particle.SNOWBALL, "snowballpoof"),

        SPELL(EnumWrappers.Particle.SPELL, "spell"),

        SPLASH(EnumWrappers.Particle.WATER_SPLASH, "splash"),

        SUSPEND(EnumWrappers.Particle.SUSPENDED, "suspended"),

        TOWN_AURA(EnumWrappers.Particle.TOWN_AURA, "townaura"),

        WATER_WAKE(EnumWrappers.Particle.WATER_WAKE, "wake"),

        WITCH_MAGIC(EnumWrappers.Particle.SPELL_WITCH, "witchMagic");

        public EnumWrappers.Particle particle;
        public String particleName;

        ParticleType(EnumWrappers.Particle particle, String particleName) {
            this.particleName = particleName;
            this.particle = particle;
        }

        public String getParticle(Material type, int data) {
            return particleName;
        }
    }

    public enum ViewDist {
        LONG(48),
        LONGER(96),
        MAX(256),
        NORMAL(24),
        SHORT(8);

        private int _dist;

        ViewDist(int dist) {
            _dist = dist;
        }

        public int getDist() {
            return _dist;
        }
    }

    private static PacketConstructor _packetConstructor;

    static {
        try {
            _packetConstructor = ProtocolLibrary.getProtocolManager()
                    .createPacketConstructor(PacketType.Play.Server.WORLD_PARTICLES, EnumWrappers.Particle.BARRIER, true, 0f, 0f,
                            0f, 0f, 0f, 0f, 0f, 0, new int[0]);
        }
        catch (Exception ex) {
            UtilError.handle(ex);
        }
    }

    private static PacketContainer getPacket(String particleName, Location location, double offsetX, double offsetY,
            double offsetZ, double speed, int count, boolean displayFar) {
        String[] parts = particleName.split("_");
        int[] details = new int[parts.length - 1];

        for (int i = 0; i < details.length; i++) {
            details[i] = Integer.parseInt(parts[i + 1]);
        }

        ParticleType particleType = ParticleType.CRIT;

        for (ParticleType type : ParticleType.values()) {
            if (type.particleName.equalsIgnoreCase(parts[0])) {
                particleType = type;
            }
        }

        PacketContainer packet = _packetConstructor
                .createPacket(particleType.particle, displayFar, (float) location.getX(), (float) location.getY(),
                        (float) location.getZ(), (float) offsetX, (float) offsetY, (float) offsetZ, (float) speed,
                        count, details);

        return packet;
    }

    public static void playParticle(Location location, ParticleColor color) {
        playParticle(location, color, ViewDist.NORMAL, UtilPlayer.getPlayers().toArray(new Player[0]));
    }

    public static void playParticle(Location location, ParticleColor color, Player... players) {
        playParticle(location, color, ViewDist.NORMAL, players);
    }

    public static void playParticle(Location location, ParticleColor color, ViewDist viewDist) {
        playParticle(ParticleType.RED_DUST, location, color.getColors()[0], color.getColors()[1], color.getColors()[2],
                1, 0, viewDist, UtilPlayer.getPlayers().toArray(new Player[0]));
    }

    public static void playParticle(Location location, ParticleColor color, ViewDist viewDist, Player... players) {
        playParticle(ParticleType.RED_DUST, location, color.getColors()[0], color.getColors()[1], color.getColors()[2],
                1, 0, viewDist, players);
    }

    public static void playParticle(ParticleType type, Location location) {
        playParticle(type, location, 1);
    }

    public static void playParticle(ParticleType type, Location location, double offsetX, double offsetY,
            double offsetZ) {
        playParticle(type, location, offsetX, offsetY, offsetZ, 1, ViewDist.NORMAL);
    }

    public static void playParticle(ParticleType particleType, Location loc, double offsetX, double offsetY,
            double offsetZ, double speed, int count) {
        playParticle(particleType, loc, offsetX, offsetY, offsetZ, speed, count, ViewDist.NORMAL);
    }

    public static void playParticle(ParticleType type, Location location, double offsetX, double offsetY,
            double offsetZ, double speed, int count, Player... players) {
        playParticle(type, location, offsetX, offsetY, offsetZ, speed, count, ViewDist.NORMAL, players);
    }

    public static void playParticle(ParticleType type, Location location, double offsetX, double offsetY,
            double offsetZ, double speed, int count, ViewDist dist) {
        playParticle(type, location, offsetX, offsetY, offsetZ, speed, count, dist, UtilPlayer.getPlayers());
    }

    public static void playParticle(ParticleType particle, Location location, double offsetX, double offsetY,
            double offsetZ, double speed, int count, ViewDist dist, Collection<Player> players) {
        playParticle(particle, location, offsetX, offsetY, offsetZ, speed, count, dist, players.toArray(new Player[0]));
    }

    public static void playParticle(ParticleType type, Location location, double offsetX, double offsetY,
            double offsetZ, double speed, int count, ViewDist dist, Player... players) {
        playParticle(type.particleName, location, offsetX, offsetY, offsetZ, speed, count, dist, players);
    }

    public static void playParticle(ParticleType particleType, Location loc, double offsetX, double offsetY,
            double offsetZ, int count) {
        playParticle(particleType, loc, offsetX, offsetY, offsetZ, 0F, count);
    }

    public static void playParticle(ParticleType type, Location location, double offsetX, double offsetY,
            double offsetZ, int count, ViewDist dist) {
        playParticle(type, location, offsetX, offsetY, offsetZ, 0, count, dist);
    }

    public static void playParticle(ParticleType type, Location location, double offsetX, double offsetY,
            double offsetZ, int count, ViewDist dist, Player... players) {
        playParticle(type, location, offsetX, offsetY, offsetZ, 0, count, dist, players);
    }

    public static void playParticle(ParticleType type, Location location, int count) {
        playParticle(type, location, count, ViewDist.NORMAL);
    }

    public static void playParticle(ParticleType type, Location location, Player... players) {
        playParticle(type, location, 0F, 0F, 0F, 1, ViewDist.NORMAL, players);
    }

    public static void playParticle(ParticleType type, Location location, int count, ViewDist viewDist) {
        playParticle(type, location, 0F, 0F, 0F, count, viewDist);
    }

    public static void playParticle(ParticleType particleType, Location location, ViewDist viewDist) {
        UtilParticle.playParticle(particleType, location, 1, viewDist);
    }

    public static void playParticle(String particle, Location location, double offsetX, double offsetY, double offsetZ,
            double speed, int count, ViewDist dist, Collection<Player> players) {
        playParticle(particle, location, offsetX, offsetY, offsetZ, speed, count, dist, players.toArray(new Player[0]));
    }

    public static void playParticle(String particle, Location location, double offsetX, double offsetY, double offsetZ,
            double speed, int count, ViewDist dist, Player... players) {
        PacketContainer packet = getPacket(particle, location, offsetX, offsetY, offsetZ, speed, count, true);

        for (Player player : players) {
            if (player.getWorld() != location.getWorld())
                continue;

            // Out of range for player
            if (UtilLoc.getDistance(player.getLocation(), location) > dist.getDist())
                continue;

            UtilPlayer.sendPacket(player, packet);
        }
    }

    public static void playParticle(String particle, Location add, double offsetX, double offsetY, double offsetZ,
            int count) {
        playParticle(particle, add, offsetX, offsetY, offsetZ, 0, count, ViewDist.NORMAL,
                UtilPlayer.getPlayers().toArray(new Player[0]));
    }
}