package org.bukkit.command;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleCommandMap implements CommandMap {
    protected final Map<String, Command> knownCommands;
    private final Server server;

    @org.jetbrains.annotations.ApiStatus.Internal
    public SimpleCommandMap(@NotNull final Server server, Map<String, Command> backing) {
        this.knownCommands = backing;
        this.server = server;
        setDefaultCommands();
    }

    private void setDefaultCommands() {
        final ReloadCommand reload = new ReloadCommand("reload");
        this.knownCommands.put("bukkit:reload", reload);
        this.knownCommands.put("bukkit:rl", reload);
        register("bukkit", new co.aikar.timings.TimingsCommand("timings"));
    }

    public void setFallbackCommands() {
        register("bukkit", new HelpCommand());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAll(@NotNull String fallbackPrefix, @NotNull List<Command> commands) {
        if (commands != null) {
            for (Command c : commands) {
                register(fallbackPrefix, c);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean register(@NotNull String fallbackPrefix, @NotNull Command command) {
        return register(command.getName(), fallbackPrefix, command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean register(@NotNull String label, @NotNull String fallbackPrefix, @NotNull Command command) {
        command.timings = co.aikar.timings.TimingsManager.getCommandTiming(fallbackPrefix, command); // Paper
        label = label.toLowerCase(Locale.ROOT).trim();
        fallbackPrefix = fallbackPrefix.toLowerCase(Locale.ROOT).trim();
        boolean registered = register(label, command, false, fallbackPrefix);

        Iterator<String> iterator = command.getAliases().iterator();
        while (iterator.hasNext()) {
            if (!register(iterator.next(), command, true, fallbackPrefix)) {
                iterator.remove();
            }
        }

        // If we failed to register under the real name, we need to set the command label to the direct address
        if (!registered) {
            command.setLabel(fallbackPrefix + ":" + label);
        }

        // Register to us so further updates of the commands label and aliases are postponed until its reregistered
        command.register(this);

        return registered;
    }

    /**
     * Registers a command with the given name is possible. Also uses
     * fallbackPrefix to create a unique name.
     *
     * @param label the name of the command, without the '/'-prefix.
     * @param command the command to register
     * @param isAlias whether the command is an alias
     * @param fallbackPrefix a prefix which is prepended to the command for a
     *     unique address
     * @return true if command was registered, false otherwise.
     */
    private synchronized boolean register(@NotNull String label, @NotNull Command command, boolean isAlias, @NotNull String fallbackPrefix) {
        knownCommands.put(fallbackPrefix + ":" + label, command);
        // Paper start
        Command known = knownCommands.get(label);
        if ((command instanceof BukkitCommand || isAlias) && (known != null && !known.canBeOverriden())) {
        // Paper end
            // Request is for an alias/fallback command and it conflicts with
            // a existing command or previous alias ignore it
            // Note: This will mean it gets removed from the commands list of active aliases
            return false;
        }

        boolean registered = true;

        // If the command exists but is an alias we overwrite it, otherwise we return
        Command conflict = knownCommands.get(label);
        if (conflict != null && conflict.getLabel().equals(label)) {
            if (!conflict.canBeOverriden()) { // Paper
            return false;
            } // Paper
        }

        if (!isAlias) {
            command.setLabel(label);
        }
        knownCommands.put(label, command);

        return registered;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatch(@NotNull CommandSender sender, @NotNull String commandLine) throws CommandException {
        String[] args = org.apache.commons.lang3.StringUtils.split(commandLine, ' '); // Paper - fix adjacent spaces (from console/plugins) causing empty array elements

        if (args.length == 0) {
            return false;
        }

        String sentCommandLabel = args[0].toLowerCase(Locale.ROOT);
        Command target = getCommand(sentCommandLabel);

        if (target == null) {
            return false;
        }

        // Paper start - Plugins do weird things to workaround normal registration
        if (target.timings == null) {
            target.timings = co.aikar.timings.TimingsManager.getCommandTiming(null, target);
        }
        // Paper end

        try {
            try (co.aikar.timings.Timing ignored = target.timings.startTiming()) { // Paper - use try with resources
            // Note: we don't return the result of target.execute as thats success / failure, we return handled (true) or not handled (false)
            target.execute(sender, sentCommandLabel, Arrays.copyOfRange(args, 1, args.length));
            } // target.timings.stopTiming(); // Spigot // Paper
        } catch (CommandException ex) {
            server.getPluginManager().callEvent(new com.destroystokyo.paper.event.server.ServerExceptionEvent(new com.destroystokyo.paper.exception.ServerCommandException(ex, target, sender, args))); // Paper
            //target.timings.stopTiming(); // Spigot // Paper
            throw ex;
        } catch (Throwable ex) {
            //target.timings.stopTiming(); // Spigot // Paper
            String msg = "Unhandled exception executing '" + commandLine + "' in " + target;
            server.getPluginManager().callEvent(new com.destroystokyo.paper.event.server.ServerExceptionEvent(new com.destroystokyo.paper.exception.ServerCommandException(ex, target, sender, args))); // Paper
            throw new CommandException(msg, ex);
        }

        // return true as command was handled
        return true;
    }

    @Override
    public synchronized void clearCommands() {
        for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
            entry.getValue().unregister(this);
        }
        knownCommands.clear();
        setDefaultCommands();
    }

    @Override
    @Nullable
    public Command getCommand(@NotNull String name) {
        Command target = knownCommands.get(name.toLowerCase(Locale.ROOT));
        return target;
    }

    @Override
    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String cmdLine) {
        return tabComplete(sender, cmdLine, null);
    }

    @Override
    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String cmdLine, @Nullable Location location) {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        Preconditions.checkArgument(cmdLine != null, "Command line cannot null");

        int spaceIndex = cmdLine.indexOf(' ');

        if (spaceIndex == -1) {
            ArrayList<String> completions = new ArrayList<String>();
            Map<String, Command> knownCommands = this.knownCommands;

            final String prefix = (sender instanceof Player ? "/" : "");

            for (Map.Entry<String, Command> commandEntry : knownCommands.entrySet()) {
                Command command = commandEntry.getValue();

                if (!command.testPermissionSilent(sender)) {
                    continue;
                }

                String name = commandEntry.getKey(); // Use the alias, not command name

                if (StringUtil.startsWithIgnoreCase(name, cmdLine)) {
                    completions.add(prefix + name);
                }
            }

            Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
            return completions;
        }

        String commandName = cmdLine.substring(0, spaceIndex);
        Command target = getCommand(commandName);

        if (target == null) {
            return null;
        }

        if (!target.testPermissionSilent(sender)) {
            return null;
        }

        String[] args = cmdLine.substring(spaceIndex + 1, cmdLine.length()).split(" ", -1);

        try {
            return target.tabComplete(sender, commandName, args, location);
        } catch (CommandException ex) {
            throw ex;
        } catch (Throwable ex) {
            String msg = "Unhandled exception executing tab-completer for '" + cmdLine + "' in " + target;
            server.getPluginManager().callEvent(new com.destroystokyo.paper.event.server.ServerExceptionEvent(new com.destroystokyo.paper.exception.ServerTabCompleteException(msg, ex, target, sender, args))); // Paper
            throw new CommandException(msg, ex);
        }
    }

    @NotNull
    public Collection<Command> getCommands() {
        return Collections.unmodifiableCollection(knownCommands.values());
    }

    public void registerServerAliases() {
        Map<String, String[]> values = server.getCommandAliases();

        for (Map.Entry<String, String[]> entry : values.entrySet()) {
            String alias = entry.getKey();
            if (alias.contains(" ")) {
                server.getLogger().warning("Could not register alias " + alias + " because it contains illegal characters");
                continue;
            }

            String[] commandStrings = entry.getValue();
            List<String> targets = new ArrayList<String>();
            StringBuilder bad = new StringBuilder();

            for (String commandString : commandStrings) {
                String[] commandArgs = commandString.split(" ");
                Command command = getCommand(commandArgs[0]);

                if (command == null) {
                    if (bad.length() > 0) {
                        bad.append(", ");
                    }
                    bad.append(commandString);
                } else {
                    targets.add(commandString);
                }
            }

            if (bad.length() > 0) {
                server.getLogger().warning("Could not register alias " + alias + " because it contains commands that do not exist: " + bad);
                continue;
            }

            // We register these as commands so they have absolute priority.
            if (targets.size() > 0) {
                knownCommands.put(alias.toLowerCase(Locale.ROOT), new FormattedCommandAlias(alias.toLowerCase(Locale.ROOT), targets.toArray(new String[targets.size()])));
            } else {
                knownCommands.remove(alias.toLowerCase(Locale.ROOT));
            }
        }
    }

    // Paper start - Expose Known Commands
    @NotNull
    public Map<String, Command> getKnownCommands() {
        return knownCommands;
    }
    // Paper end
}
