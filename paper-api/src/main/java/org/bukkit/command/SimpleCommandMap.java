package org.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Server;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import static org.bukkit.util.Java15Compat.Arrays_copyOfRange;

public final class SimpleCommandMap implements CommandMap {
    private final Map<String, Command> knownCommands = new HashMap<String, Command>();
    private final Set<String> aliases = new HashSet<String>();
    private final Server server;

    public SimpleCommandMap(final Server server) {
        this.server = server;
        setDefaultCommands(server);
    }

    private void setDefaultCommands(final Server server) {
        register("bukkit", new VersionCommand("version", server));
        register("bukkit", new ReloadCommand("reload", server));
        register("bukkit", new PluginsCommand("plugins", server));
    }

    /**
     * {@inheritDoc}
     */
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        if (commands != null) {
            for (Command c : commands) {
                register(fallbackPrefix, c);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean register(String fallbackPrefix, Command command) {
        return register(command.getName(), fallbackPrefix, command);
    }

    /**
     * {@inheritDoc}
     */
    public boolean register(String label, String fallbackPrefix, Command command) {
        boolean registeredPassedLabel = register(label, fallbackPrefix, command, false);

        Iterator iterator = command.getAliases().iterator();
        while (iterator.hasNext()) {
            if (!register((String) iterator.next(), fallbackPrefix, command, true)) {
                iterator.remove();
            }
        }

        // Register to us so further updates of the commands label and aliases are postponed until its reregistered
        command.register(this);

        return registeredPassedLabel;
    }

    /**
     * Registers a command with the given name is possible, otherwise uses fallbackPrefix to create a unique name if its not an alias
     * @param name the name of the command, without the '/'-prefix.
     * @param fallbackPrefix a prefix which is prepended to the command with a ':' one or more times to make the command unique
     * @param command the command to register
     * @return true if command was registered with the passed in label, false otherwise.
     * If isAlias was true a return of false indicates no command was registerd
     * If isAlias was false a return of false indicates the fallbackPrefix was used one or more times to create a unique name for the command
     */
    private synchronized boolean register(String label, String fallbackPrefix, Command command, boolean isAlias) {
        String lowerLabel = label.trim().toLowerCase();

        if (isAlias && knownCommands.containsKey(lowerLabel)) {
            // Request is for an alias and it conflicts with a existing command or previous alias ignore it
            // Note: This will mean it gets removed from the commands list of active aliases
            return false;
        }

        String lowerPrefix = fallbackPrefix.trim().toLowerCase();
        boolean registerdPassedLabel = true;

        // If the command exists but is an alias we overwrite it, otherwise we rename it based on the fallbackPrefix
        while (knownCommands.containsKey(lowerLabel) && !aliases.contains(lowerLabel)) {
            lowerLabel = lowerPrefix + ":" + lowerLabel;
            registerdPassedLabel = false;
        }

        if (isAlias) {
            aliases.add(lowerLabel);
        } else {
            // Ensure lowerLabel isn't listed as a alias anymore and update the commands registered name
            aliases.remove(lowerLabel);
            command.setLabel(lowerLabel);
        }
        knownCommands.put(lowerLabel, command);

        return registerdPassedLabel;
    }

    /**
     * {@inheritDoc}
     */
    public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
        String[] args = commandLine.split(" ");

        if (args.length == 0) {
            return false;
        }

        String sentCommandLabel = args[0].toLowerCase();
        Command target = getCommand(sentCommandLabel);
        if (target == null) {
            return false;
        }

        try {
            // Note: we don't return the result of target.execute as thats success / failure, we return handled (true) or not handled (false)
            target.execute(sender, sentCommandLabel, Arrays_copyOfRange(args, 1, args.length));
        } catch (CommandException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing '" + commandLine + "' in " + target, ex);
        }

        // return true as command was handled
        return true;
    }

    public synchronized void clearCommands() {
        for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
            entry.getValue().unregister(this);
        }
        knownCommands.clear();
        aliases.clear();
        setDefaultCommands(server);
    }

    public Command getCommand(String name) {
        return knownCommands.get(name.toLowerCase());
    }

    public void registerServerAliases() {
        Map<String, String[]> values = server.getCommandAliases();
        
        for (String alias : values.keySet()) {
            String[] targetNames = values.get(alias);
            List<Command> targets = new ArrayList<Command>();
            String bad = "";

            for (String name : targetNames) {
                Command command = getCommand(name);

                if (command == null) {
                    if (bad.length() > 0) {
                        bad += ", ";
                    }
                } else {
                    targets.add(command);
                }
            }

            // We register these as commands so they have absolute priority.
            
            if (targets.size() > 0) {
                knownCommands.put(alias.toLowerCase(), new MultipleCommandAlias(alias.toLowerCase(), targets.toArray(new Command[0])));
            } else {
                knownCommands.remove(alias.toLowerCase());
            }

            if (bad.length() > 0) {
                server.getLogger().warning("The following command(s) could not be aliased under '" + alias + "' because they do not exist: " + bad);
            }
        }
    }

    private static class VersionCommand extends Command {
        private final Server server;

        public VersionCommand(String name, Server server) {
            super(name);
            this.server = server;
            this.description = "Gets the version of this server including any plugins in use";
            this.usageMessage = "/version [plugin name]";
            this.setAliases(Arrays.asList("ver", "about"));
        }

        @Override
        public boolean execute(CommandSender sender, String currentAlias, String[] args) {
            if (args.length == 0) {
                sender.sendMessage("This server is running " + ChatColor.GREEN + server.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + server.getVersion());
                sender.sendMessage("This server is also sporting some funky dev build of Bukkit!");
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

                    sender.sendMessage(ChatColor.GREEN + desc.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + desc.getVersion());

                    if (desc.getDescription() != null) {
                        sender.sendMessage(desc.getDescription());
                    }

                    if (desc.getWebsite() != null) {
                        sender.sendMessage("Website: " + ChatColor.GREEN + desc.getWebsite());
                    }

                    if (!desc.getAuthors().isEmpty()) {
                        if (desc.getAuthors().size() == 1) {
                            sender.sendMessage("Author: " + getAuthors(desc));
                        } else {
                            sender.sendMessage("Authors: " + getAuthors(desc));
                        }
                    }
                } else {
                    sender.sendMessage("This server is not running any plugin by that name.");
                    sender.sendMessage("Use /plugins to get a list of plugins.");
                }
            }

            return true;
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
            this.description = "Reloads the server configuration and plugins";
            this.usageMessage = "/reload";
            this.setAliases(Arrays.asList("rl"));
        }

        @Override
        public boolean execute(CommandSender sender, String currentAlias, String[] args) {
            if (sender.isOp()) {
                server.reload();
                sender.sendMessage(ChatColor.GREEN + "Reload complete.");
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have sufficient access to reload this server.");
            }
            return true;
        }
    }

    private static class PluginsCommand extends Command {

        private final Server server;

        public PluginsCommand(String name, Server server) {
            super(name);
            this.server = server;
            this.description = "Gets a list of plugins running on the server";
            this.usageMessage = "/plugins";
            this.setAliases(Arrays.asList("pl"));
        }

        @Override
        public boolean execute(CommandSender sender, String currentAlias, String[] args) {
            sender.sendMessage("Plugins: " + getPluginList());
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

                pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
                pluginList.append(plugin.getDescription().getName());
            }

            return pluginList.toString();
        }
    }
}
