package org.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public final class SimpleCommandMap implements CommandMap {
    private final Map<String, Command> knownCommands = new HashMap<String, Command>();
    private final Server server;

    public SimpleCommandMap(final Server server) {
        this.server = server;
        setDefaultCommands(server);
    }

    private void setDefaultCommands(final Server server) {
        register("bukkit", new VersionCommand("version", server));
        register("bukkit", new ReloadCommand("reload", server));
    }

    /**
     * Registers multiple commands. Returns name of first command for which fallback had to be used if any.
     * @param plugin
     * @return
     */
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        if (commands != null) {
            for(Command c : commands) {
                register(fallbackPrefix, c);
            }
        }
    }

    private void register(String fallbackPrefix, Command command) {
        List<String> names = new ArrayList<String>();
        names.add(command.getName());
        names.addAll(command.getAliases());

        for (String name : names) {
            register(name, fallbackPrefix, command);
        }
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean register(String name, String fallbackPrefix, Command command) {
        boolean nameInUse = (knownCommands.get(name) != null);
        if (nameInUse)
            name = fallbackPrefix + ":" + name;
        
        knownCommands.put(name, command);
        return !nameInUse;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean dispatch(Player sender, String commandLine) {
        String[] args = commandLine.split(" ");
        String sentCommandLabel = args[0].substring(1);

        args = Arrays.copyOfRange(args, 1, args.length);
        
        Command target = knownCommands.get(sentCommandLabel);
        boolean isRegisteredCommand = (target != null);
        if (isRegisteredCommand) {
            target.execute(sender, sentCommandLabel, args);
        }
        return isRegisteredCommand;
    }

    public void clearCommands() {
        synchronized (this) {
            knownCommands.clear();
            setDefaultCommands(server);
        }
    }

    private static class VersionCommand extends Command {
        private final Server server;

        public VersionCommand(String name, Server server) {
            super(name);
            this.server = server;
            this.tooltip = "Gets the version of this server including any plugins in use";
            this.usageMessage = "/version [plugin name]";
            this.setAliases(Arrays.asList("ver", "about"));
        }

        @Override
        public boolean execute(Player player, String currentAlias, String[] args) {
            if (args.length == 0) {
                player.sendMessage("This server is running " + ChatColor.GREEN
                        + server.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + server.getVersion());
                player.sendMessage("This server is also sporting some funky dev build of Bukkit!");
                player.sendMessage("Plugins: " + getPluginList());
            } else {
                StringBuilder name = new StringBuilder();

                for (String arg : args) {
                    if (name.length() > 0) {
                        name.append(' ');
                    }
                    name.append(arg);
                }

                Plugin plugin = server.getPluginManager().getPlugin(name.toString());

                if (plugin != null) {
                    PluginDescriptionFile desc = plugin.getDescription();
                    player.sendMessage(ChatColor.GREEN + desc.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + desc.getVersion());

                    if (desc.getDescription() != null) {
                        player.sendMessage(desc.getDescription());
                    }

                    if (desc.getWebsite() != null) {
                        player.sendMessage("Website: " + ChatColor.GREEN + desc.getWebsite());
                    }

                    if (!desc.getAuthors().isEmpty()) {
                        if (desc.getAuthors().size() == 1) {
                            player.sendMessage("Author: " + getAuthors(desc));
                        } else {
                            player.sendMessage("Authors: " + getAuthors(desc));
                        }
                    }
                } else {
                    player.sendMessage("This server is not running any plugin by that name.");
                    player.sendMessage("Plugins: " + getPluginList());
                }
            }

            return true;
        }

        private String getPluginList() {
            StringBuilder pluginList = new StringBuilder();
            Plugin[] plugins = server.getPluginManager().getPlugins();

            for (Plugin plugin : plugins) {
                if (pluginList.length() > 0) {
                    pluginList.append(ChatColor.WHITE);
                    pluginList.append(", ");
                }

                pluginList.append(ChatColor.GREEN);
                pluginList.append(plugin.getDescription().getName());
            }

            return pluginList.toString();
        }

        private String getAuthors(final PluginDescriptionFile desc) {
            StringBuilder result = new StringBuilder();
            ArrayList<String> authors = desc.getAuthors();

            for (int i = 0; i < authors.size(); i++) {
                if (result.length() > 0) {
                    result.append(ChatColor.WHITE);

                    if (i < authors.size() - 1) {
                        result.append(", ");
                    } else {
                        result.append(" and ");
                    }
                }

                result.append(ChatColor.GREEN);
                result.append(authors.get(i));
            }

            return result.toString();
        }
    }

    private static class ReloadCommand extends Command {

        private final Server server;

        public ReloadCommand(String name, Server server) {
            super(name);
            this.server = server;
            this.tooltip = "Reloads the server configuration and plugins";
            this.usageMessage = "/reload";
            this.setAliases(Arrays.asList("rl"));
        }

        @Override
        public boolean execute(Player player, String currentAlias, String[] args) {
            if (player.isOp()) {
                server.reload();
                player.sendMessage(ChatColor.GREEN + "Reload complete.");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have sufficient access" + " to reload this server.");
            }
            return true;
        }
    }
}