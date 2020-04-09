package me.libraryaddict.core.command;

import com.comphenix.protocol.ProtocolManager;
import me.libraryaddict.core.C;
import me.libraryaddict.core.command.commands.*;
import me.libraryaddict.core.plugin.MiniPlugin;
import me.libraryaddict.core.ranks.PlayerRank;
import me.libraryaddict.core.ranks.RankManager;
import me.libraryaddict.core.utils.UtilError;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.craftbukkit.v1_15_R1.command.CraftCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

public class CommandManager extends MiniPlugin {
    private ArrayList<String> _bypassCommands = new ArrayList<String>();
    private ArrayList<SimpleCommand> _commands = new ArrayList<SimpleCommand>();
    private ProtocolManager _protocolManager;
    private RankManager _rankManager;

    public CommandManager(JavaPlugin plugin) {
        super(plugin, "Command Manager");


        try {
            SimplePluginManager spm = (SimplePluginManager)getPlugin().getServer().getPluginManager();

            Field commandMapField = spm.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CraftCommandMap commandMap = (CraftCommandMap)commandMapField.get(spm);

            System.out.print(commandMap.getClass() + " fields: " + Arrays.toString(commandMap.getClass().getDeclaredFields()));

            Field knownCommandsField = commandMap.getClass().getSuperclass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);

            Map<String, Command> knownCommands = (Map<String, Command>)knownCommandsField.get(commandMap);
            Iterator<Map.Entry<String, Command>> itr = knownCommands.entrySet().iterator();

            while (itr.hasNext()) {
                Command command = (Command)((Map.Entry)itr.next()).getValue();
                command.setPermission("admincommand.bukkit" + command.getName());
                command.setPermissionMessage(C.DRed + "You do not have permission to use this command");
                command.getPermission();
            }
        } catch (NoSuchFieldException|IllegalAccessException e) {
            UtilError.handle(e);
        }

        registerCommand(new CommandGamemode());
        registerCommand(new CommandTeleport());
        registerCommand(new CommandTeleportAll());
        registerCommand(new CommandTeleportHere());
        registerCommand(new CommandTop());
        registerCommand(new CommandBungeeSettings(plugin));
        registerCommand(new CommandKick(plugin));
        registerCommand(new CommandGiveItem());
        registerCommand(new CommandStuck());
        registerCommand(new CommandSudo(this));
        registerCommand(new CommandClearInventory());
        registerCommand(new CommandRefundMe());
    }

    public void addBypasses(ArrayList<String> bypasses) {
        _bypassCommands.addAll(bypasses);
    }

    public void addBypasses(String... bypasses) {
        _bypassCommands.addAll(Arrays.asList(bypasses));
    }

    public SimpleCommand getCommand(Class<? extends SimpleCommand> classFile) {
        for (SimpleCommand command : _commands) {
            if (command.getClass().isAssignableFrom(classFile))
                return command;
        }

        return null;
    }

    public SimpleCommand getCommand(String commandAlias) {
        for (SimpleCommand command : _commands) {
            if (command.isAlias(commandAlias))
                return command;
        }

        return null;
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        String alias = command.split(" ")[0].substring(1);

        if (_bypassCommands.contains(alias.toLowerCase())) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        String arg = command.substring(command.contains(" ") ? command.indexOf(" ") : command.length()).trim();
        String[] args = arg.isEmpty() ? new String[0] : arg.split(" ");

        for (SimpleCommand simpleCommand : _commands) {
            if (!simpleCommand.isAlias(alias))
                continue;

            PlayerRank rank = _rankManager.getRank(player);

            if (!simpleCommand.canUse(player, rank)) {
                player.sendMessage(C.DRed + "You do not have permission to use this command");
                return;
            }

            try {
                if (simpleCommand.isAdminCommand())
                    simpleCommand.log(player, args);

                simpleCommand.runCommand(player, rank, alias, args);
            }
            catch (Exception ex) {
                player.sendMessage(UtilError.format("There was an error while running the command"));
                UtilError.handle(ex);
            }

            return;
        }

        player.sendMessage(C.DRed + "Command not found");
    }

    private ArrayList<String> onTabComplete(Player player, String alias, String token, String[] args) {
        ArrayList<String> completions = new ArrayList<>();
        PlayerRank rank = this._rankManager.getRank(player);
        for (SimpleCommand simpleCommand : this._commands) {
            if (!simpleCommand.canUse(player, rank))
                continue;
            if (!simpleCommand.isAlias(alias))
                continue;
            simpleCommand.onTab(player, rank, args, token, completions);
        }
        return completions;
    }

    public void registerCommand(SimpleCommand command) {
        for (String commandAlias : command.getAliases()) {
            if (getCommand(commandAlias) != null) {
                throw new IllegalArgumentException(
                        "The command '" + commandAlias + "' is already registered to " + getCommand(commandAlias));
            }
        }

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap)bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(command.getAliases()[0], new BukitCommand(command, (Plugin)getPlugin()));

        } catch (NoSuchFieldException|IllegalAccessException e) {
            UtilError.handle(e);
        }

        _commands.add(command);
        command.setPlugin(getPlugin());
    }

    public void setRankManager(RankManager rankManager) {
        _rankManager = rankManager;
    }

    public void unregisterCommand(SimpleCommand command) {
        _commands.remove(command);
        command.setPlugin(null);
    }

    public class BukitCommand extends Command implements PluginIdentifiableCommand {
        private final Plugin _plugin;

        private final SimpleCommand _simpleCommand;

        protected BukitCommand(SimpleCommand command, Plugin plugin) {
            super(command.getAliases()[0]);
            this._plugin = plugin;
            this._simpleCommand = command;
            if (command.isAdminCommand()) {
                try {
                    Bukkit.getPluginManager().addPermission(new Permission("admincommand." + command.getAliases()[0]));
                    setPermission("admincommand." + command.getAliases()[0]);
                } catch (IllegalArgumentException e) {
                    Bukkit.getPluginManager().addPermission(new Permission("admincommand." + command.getAliases()[0] + "1"));
                    setPermission("admincommand." + command.getAliases()[0] + "1");
                }
                setPermissionMessage(C.DRed + "You do not have permission to use this command");
            }
        }

        public boolean execute(CommandSender commandSender, String s, String[] strings) {
            return false;
        }

        public List<String> tabComplete(CommandSender commandSender, String alias, String[] args) throws IllegalArgumentException {
            if (!(commandSender instanceof Player))
                return new ArrayList<>();
            Player player = (Player)commandSender;
            ArrayList<String> returns = onTabComplete(player, alias, args[args.length - 1], args);
            returns.sort(String.CASE_INSENSITIVE_ORDER);
            return returns;
        }

        public Plugin getPlugin() {
            return this._plugin;
        }
    }
}
