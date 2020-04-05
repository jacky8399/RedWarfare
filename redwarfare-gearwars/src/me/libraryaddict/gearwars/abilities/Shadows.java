package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.events.TimeSecondEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashMap;

public class Shadows extends AbilityListener implements Disableable {
    private class Shadow {
        private long lastUsed = System.currentTimeMillis() + (120 * 1000);
        private Location pLoc;
        private int secondsInvis;
        private int secondsStill;
    }

    private HashMap<Player, Shadow> shadows = new HashMap<Player, Shadow>();

    public Shadows() {
        registerScoreboard(DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Cooldown");
        registerScoreboard(DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Invis time");
        registerScoreboard(DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Invisible");
    }

    @EventHandler
    public void onKilled(PlayerKilledEvent event) {
        shadows.remove(event.getKilled().getPlayer());
    }

    @EventHandler
    public void onSecond(TimeSecondEvent event) {
        for (Gamer gamer : getGamers()) {
            Player p = gamer.getPlayer();
            Shadow shadow = null;
            if (!shadows.containsKey(p)) {
                shadow = new Shadow();
                shadow.pLoc = p.getLocation();
                shadows.put(p, shadow);
            } else {
                shadow = shadows.get(p);
                if (shadow.lastUsed > 0 && shadow.lastUsed < System.currentTimeMillis()) {
                    p.sendMessage(ChatColor.AQUA + "Shadows cooldown has worn off!");
                    shadow.lastUsed = 0;
                    ScoreboardManager
                            .hideScore(gamer.getPlayer(), DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Cooldown");
                }
                if (shadow.lastUsed == 0) {
                    if (shadow.pLoc.distance(p.getLocation()) < 0.2) {
                        shadow.secondsStill++;
                    } else {
                        shadow.secondsStill = 0;
                    }
                    shadow.pLoc = p.getLocation();
                    if (shadow.secondsStill > 20) {
                        ScoreboardManager.hideScore(gamer.getPlayer(), DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Invis time");
                        p.sendMessage(ChatColor.AQUA + "You stand still and slowly you fade away into the shadows...");
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 20, 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 20, 1));
                        shadow.secondsStill = 0;
                        shadow.lastUsed = System.currentTimeMillis() + (120 * 1000);
                        shadow.secondsInvis = 20;
                        gamer.setInvis(true);
                        gamer.updateSelfToOthers();
                    }
                }
                if (shadow.secondsInvis > 0) {
                    Location g = p.getLocation().clone();
                    for (float a : new float[] { 0F, 0.6F }) {
                        g.add(0, a, 0);
                        for (int i = 0; i < 9; i++) {
                            p.getWorld().playEffect(g, Effect.SMOKE, i);
                        }
                    }
                    shadow.secondsInvis--;
                    if (shadow.secondsInvis <= 0) {
                        ScoreboardManager.hideScore(gamer.getPlayer(), DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Invisible");
                        p.sendMessage(ChatColor.AQUA + "Your stealth wore off, You are now visible!");
                        gamer.setInvis(false);
                        gamer.updateSelfToOthers();
                    }
                }
            }
            if (shadow.lastUsed != 0) {
                ScoreboardManager.makeScore(gamer.getPlayer(), DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Cooldown",
                        (int) ((shadow.lastUsed - System.currentTimeMillis()) / 1000));
            }
            if (shadow.secondsInvis > 0) {
                ScoreboardManager.makeScore(gamer.getPlayer(), DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Invisible",
                        shadow.secondsInvis);
            } else if (shadow.lastUsed == 0) {
                ScoreboardManager.makeScore(gamer.getPlayer(), DisplaySlot.SIDEBAR, ChatColor.DARK_GREEN + "Invis time",
                        20 - shadow.secondsStill);
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