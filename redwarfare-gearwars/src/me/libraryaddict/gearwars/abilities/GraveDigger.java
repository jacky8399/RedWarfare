package me.libraryaddict.gearwars.abilities;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.scoreboard.ScoreboardManager;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraveDigger extends AbilityListener implements Disableable {

    Map<Player, Integer> souls = new HashMap<Player, Integer>();

    private void buildBook(Player player, String[] wording, boolean newBook) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("Death");
        // 19 chars per sentance
        meta.setPages(wording);
        meta.setTitle(newBook ? "Soul Binding" : "Bound Soul");
        book.setItemMeta(meta);
        if (!newBook) {
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), book);
        } else {
            player.getInventory().addItem(book);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.WRITTEN_BOOK && hasAbility(event.getPlayer())) {
            BookMeta b = (BookMeta) event.getItem().getItemMeta();
            Player p = event.getPlayer();
            if (b != null && b.getAuthor() != null && b.getAuthor().equals("Death")
                    && event.getAction().equals(Action.LEFT_CLICK_BLOCK) && b.getTitle().equals("Bound Soul")) {
                if (souls.containsKey(p) && souls.get(p) > 2) {
                    List<String> pages = b.getPages();
                    String name = pages.get(0).substring(6, pages.get(0).indexOf("."));
                    Player soul = Bukkit.getPlayerExact(name);
                    if (soul != null) {
                        Gamer spec = GearApi.getPlayerManager().getGamer(soul);
                        if (!spec.isAlive()) {
                            Bukkit.broadcastMessage(ChatColor.AQUA + p.getName() + " dug up " + name
                                    + " and breathed life into his lips!");
                            souls.put(p, souls.get(p) - 3);
                            spec.clearInventory();
                            spec.setHuman();
                            spec.setSpectator(false);
                            ScoreboardManager.removeFromTeam(soul);

                            GearApi.getPlayerManager().refreshMeta(soul);
                            spec.getPlayer().setAllowFlight(false);
                            spec.getPlayer().setFallDistance(0);
                            spec.updateSelfToOthers();
                            soul.teleport(p.getLocation().add(0, 1, 0));
                            p.getItemInHand().setType(Material.AIR);
                        } else {
                            p.sendMessage(ChatColor.AQUA + soul.getName() + " is not dead! Stop being creepy!");
                        }
                    } else {
                        p.sendMessage(ChatColor.AQUA + "You can't find their body! Did they quit the game?");
                    }
                } else {
                    p.sendMessage(ChatColor.AQUA + "You do not have enough souls!");
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            Player play = (Player) event.getEntity();
            if (hasAbility(p) && p.getItemInHand().getType() == Material.WRITTEN_BOOK) {
                BookMeta b = (BookMeta) p.getItemInHand().getItemMeta();
                if (b != null && b.getAuthor() != null && b.getAuthor().equals("Death") && b.getTitle().equals("Soul Binding")) {
                    buildBook(
                            p,
                            new String[] { "Name: " + play.getName() + ".",
                                    "This is a soul binding contract between " + p.getName() + " and " + play.getName(), "",
                                    "If " + play.getName() + " dies, " + p.getName() + " may summon him back from the dead" },
                            false);
                    p.sendMessage(ChatColor.AQUA + play.getName() + " has entered into a contract with you");
                    play.sendMessage(ChatColor.AQUA + "You have just entered into a soul binding contract with " + p.getName());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        if (event.getKillerGamer() != null && hasAbility(event.getKillerGamer())) {
            Player p = event.getKillerGamer().getPlayer();
            souls.put(p, (souls.containsKey(p) ? souls.get(p) : 0) + 1);
            p.sendMessage(ChatColor.AQUA + "You captured a soul! You now have " + souls.get(p) + "!");
        }
        souls.remove(event.getKilled().getPlayer());
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            Player p = gamer.getPlayer();
            buildBook(
                    p,
                    new String[] { "This is a soul to bind a player into, You will gain the ability to bring them back from the dead" },
                    true);
            buildBook(
                    p,
                    new String[] { "This is a soul to bind a player into, You will gain the ability to bring them back from the dead" },
                    true);
        }
    }

    @Override
    public void unregisterListener() {
    }
}
