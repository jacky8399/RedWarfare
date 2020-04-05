package me.libraryaddict.gearwars.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.libraryaddict.gearwars.GearApi;
import me.libraryaddict.gearwars.events.PlayerKilledEvent;
import me.libraryaddict.gearwars.events.TimeSecondEvent;
import me.libraryaddict.gearwars.misc.Disableable;
import me.libraryaddict.gearwars.types.AbilityListener;
import me.libraryaddict.gearwars.types.Gamer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Teamer extends AbilityListener implements CommandExecutor, Disableable {
    private HashMap<Player, Player> invitations = new HashMap<Player, Player>();

    // Teamer get buffs for every so and so players in team
    // 3 - speed, 5 - strength 7 - regen 10-jump
    // Teamers can't hurt each other
    private HashMap<Player, ArrayList<Player>> teams = new HashMap<Player, ArrayList<Player>>();

    /**
     * When this player accepts a invitation Either join them a team, or say they can't.
     */
    public void acceptInvitation(Player player) {
        if (invitations.containsKey(player)) {
            Player p = invitations.remove(player);
            List<Player> team = teams.get(p);
            team.add(player);
            teamMessage(p, ChatColor.BLUE + player.getName() + " has joined the team!");
        } else {
            player.sendMessage(ChatColor.RED + "You were not invited to any teams! Forever alone..");
        }
    }

    public String getMembers(Player player) {
        String names = "";
        List<Player> members = teams.get(getTeam(player));
        for (Player p : members) {
            if (teams.containsKey(p))
                names += "[Leader] " + p.getName() + ", ";
            else
                names += p.getName() + ", ";
        }
        if (names.length() > 2) {
            names = names.substring(0, names.length() - 2);
        }
        return names;
    }

    /**
     * Get the player who owns the team this player is in
     */
    public Player getTeam(Player player) {
        if (teams.containsKey(player)) {
            return player;
        }
        for (Player p : teams.keySet()) {
            if (teams.get(p).contains(player)) {
                return p;
            }
        }
        return null;
    }

    public void invitePlayer(Player inviter, String[] args) {
        if (getTeam(inviter) != null) {
            if (getTeam(inviter) == inviter) {
                if (args.length > 1) {
                    for (int i = 1; i < args.length; i++) {
                        String name = args[i];
                        Player player = Bukkit.getPlayer(name);
                        if (player != null) {
                            if (getTeam(player) == null) {
                                if (!invitations.containsKey(player)) {
                                    Gamer gamer = GearApi.getPlayerManager().getGamer(player);
                                    if (gamer != null && gamer.isAlive()) {
                                        invitations.put(player, inviter);
                                        player.sendMessage(ChatColor.BLUE + "You have been invited to " + inviter.getName()
                                                + "'s team!\n" + "Their team members: " + ChatColor.RED + getMembers(inviter)
                                                + ChatColor.BLUE + "\nUse " + ChatColor.RED + "/team accept" + ChatColor.BLUE
                                                + " to join the team or use " + ChatColor.RED + "/team refuse" + ChatColor.BLUE
                                                + " to refuse the invitation!");
                                        teamMessage(inviter,
                                                ChatColor.BLUE + inviter.getName() + " is inviting " + player.getName()
                                                        + " into the team!");
                                    } else {
                                        inviter.sendMessage(ChatColor.RED + "Unable to invite spectator");
                                    }
                                } else {
                                    inviter.sendMessage(ChatColor.RED + "That player is already being invited to a team by "
                                            + invitations.get(player) + "!");
                                }
                            } else {
                                inviter.sendMessage(ChatColor.RED + "That player is already in " + getTeam(player).getName()
                                        + "'s team!");
                            }
                        } else {
                            inviter.sendMessage(ChatColor.RED + "Can't find player " + name + ".");
                        }
                    }
                } else {
                    inviter.sendMessage(ChatColor.RED + "You must define a player name. /team invite <Player>");
                }
            } else {
                inviter.sendMessage(ChatColor.RED + "You are not the leader of your team!");
            }
        } else {
            inviter.sendMessage(ChatColor.RED + "You are not in a team!");
        }
    }

    public void kickPlayer(Player kicker, String[] args) {
        if (teams.containsKey(kicker)) {
            if (args.length > 1) {
                if (args[1].equals("all")) {
                    List<Player> team = teams.get(kicker.getName());
                    teamMessage(kicker, ChatColor.BLUE + kicker.getName() + " has kicked everyone out the team!");
                    Iterator<Player> itel = team.iterator();
                    while (itel.hasNext()) {
                        if (itel.next() != kicker) {
                            itel.remove();
                        }
                    }
                    kicker.sendMessage(ChatColor.RED + "Kicked all players out the team!");
                } else if (Bukkit.getPlayer(args[1]) != null) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (getTeam(player) == kicker) {
                        teamMessage(kicker, ChatColor.BLUE + kicker.getName() + " has kicked " + player.getName()
                                + " out the team!");
                        List<Player> team = teams.get(kicker);
                        team.remove(player);
                    } else {

                        kicker.sendMessage(ChatColor.RED + "That player is not in your team!");
                    }
                } else {
                    kicker.sendMessage(ChatColor.RED + "Can't find player. Use /team kick <Player>");
                }
            } else {
                kicker.sendMessage(ChatColor.RED + "You must define a player name. /team kick <Player>");
            }
        } else {
            kicker.sendMessage(ChatColor.RED + "You are not a leader of a team!");
        }
    }

    public void leaveTeam(Player player) {
        Player leader = getTeam(player);
        if (leader != null) {
            if (leader != player) {
                teamMessage(player, ChatColor.BLUE + player.getName() + " has left the team!");
                teams.get(leader).remove(player);
            } else {
                player.sendMessage(ChatColor.RED + "You can't leave your own team!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are not in a team!");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArrowShot(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player hurter = null;
            if (event.getDamager() instanceof Projectile) {
                Projectile arrow = (Projectile) event.getDamager();
                if (arrow.getShooter() instanceof Player && event.getEntity() instanceof Player) {
                    hurter = (Player) arrow.getShooter();
                }
            }
            if (event.getDamager() instanceof Player) {
                hurter = (Player) event.getDamager();
            }
            if (hurter != null) {
                Player p = getTeam(hurter);
                if (p != null && p == getTeam((Player) event.getEntity())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("team")) {
            Player player = (Player) sender;
            Gamer gamer = GearApi.getPlayerManager().getGamer(player);
            if (gamer != null && gamer.isAlive()) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("kick")) {
                        kickPlayer(player, args);
                    } else if (args[0].equalsIgnoreCase("invite")) {
                        invitePlayer(player, args);
                    } else if (args[0].equalsIgnoreCase("players")) {
                        sendMembers(player);
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        leaveTeam(player);
                    } else if (args[0].equalsIgnoreCase("accept")) {
                        acceptInvitation(player);
                    } else if (args[0].equalsIgnoreCase("refuse")) {
                        refuseInvitation(player);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Invalid command");
                        sender.sendMessage(ChatColor.BLUE
                                + "The team leader can use: /team kick <player>/all and /team invite <player> <Player> <Player>");
                        sender.sendMessage(ChatColor.BLUE + "Everyone can use /team leave and /team players");
                    }
                } else {
                    sender.sendMessage(ChatColor.BLUE
                            + "The team leader can use: /team kick <player>/all and /team invite <player> <Player> <Player>");
                    sender.sendMessage(ChatColor.BLUE + "Everyone can use /team leave and /team players");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You must be playing the game to be in a team");
            }
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerKilledEvent event) {
        Player killed = event.getKilled().getPlayer();
        invitations.remove(killed);
        if (invitations.containsValue(killed)) {
            Iterator<Player> itel = invitations.keySet().iterator();
            while (itel.hasNext()) {
                if (invitations.get(itel.next()) == killed) {
                    itel.remove();
                }
            }
        }
        Player leader = getTeam(killed);
        if (leader != null) {
            if (killed == leader) {
                teamMessage(leader, ChatColor.BLUE + "The team has been disbanded as the team leader has died");
                teams.remove(leader);
            } else {
                teamMessage(leader, ChatColor.BLUE + "The team member " + event.getKilled().getName() + " has died");
                teams.get(leader).remove(killed);
            }
        }
    }

    @EventHandler
    public void onSecond(TimeSecondEvent event) {
        for (Player player : teams.keySet()) {
            List<Player> team = teams.get(player);
            List<PotionEffectType> effects = new ArrayList<PotionEffectType>();
            int size = team.size();
            if (size >= 4) {
                effects.add(PotionEffectType.SPEED);
            }
            if (size >= 6) {
                effects.add(PotionEffectType.INCREASE_DAMAGE);
            }
            if (size >= 8) {
                effects.add(PotionEffectType.FIRE_RESISTANCE);
            }
            if (size >= 10) {
                effects.add(PotionEffectType.JUMP);
            }
            for (PotionEffectType pot : effects) {
                for (Player p : team) {
                    p.addPotionEffect(new PotionEffect(pot, 200, 0), true);
                }
            }
        }
    }

    public void refuseInvitation(Player player) {
        if (invitations.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "You declined " + invitations.get(player).getName()
                    + "'s invitation to their team");
            teamMessage(invitations.get(player), ChatColor.BLUE + player.getName() + " has refused the invitation to the team");
            invitations.remove(player);
        } else {
            player.sendMessage(ChatColor.RED + "You were not invitated to any teams");
        }
    }

    @Override
    public void registerListener() {
        for (Gamer gamer : getGamers()) {
            ArrayList<Player> team = new ArrayList<Player>();
            Player p = gamer.getPlayer();
            team.add(p);
            teams.put(p, team);
            p.sendMessage(ChatColor.BLUE + "You just created a team! Use /team to view your commands!");
        }
        GearApi.getMainPlugin().getCommand("team").setExecutor(this);
    }

    public void sendMembers(Player player) {
        if (getTeam(player) != null) {
            player.sendMessage(ChatColor.RED + "Team Members: " + ChatColor.BLUE + getMembers(player) + ".");
        } else {
            player.sendMessage(ChatColor.RED + "You are not in a team!");
        }
    }

    public void teamMessage(Player player, String message) {
        List<Player> team = teams.get(getTeam(player));
        for (Player p : team) {
            p.sendMessage(message);
        }
    }

    @Override
    public void unregisterListener() {
    }
}
