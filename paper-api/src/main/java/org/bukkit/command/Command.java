package org.bukkit.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Command, which executes various tasks upon user input
 */
public abstract class Command {
    private String name;
    private String nextLabel;
    private String label;
    private List<String> aliases;
    private List<String> activeAliases;
    private CommandMap commandMap;
    protected String description;
    protected String usageMessage;
    private String permission;
    private net.kyori.adventure.text.Component permissionMessage; // Paper
    /**
     * @deprecated Timings will be removed in the future
     */
    @Deprecated(forRemoval = true)
    public co.aikar.timings.Timing timings; // Paper
    /**
     * @deprecated Timings will be removed in the future
     */
    @Deprecated(forRemoval = true)
    @NotNull public String getTimingName() {return getName();} // Paper

    protected Command(@NotNull String name) {
        this(name, "", "/" + name, new ArrayList<String>());
    }

    protected Command(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        this.name = name;
        this.nextLabel = name;
        this.label = name;
        this.description = (description == null) ? "" : description;
        this.usageMessage = (usageMessage == null) ? "/" + name : usageMessage;
        this.aliases = aliases;
        this.activeAliases = new ArrayList<String>(aliases);
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args);

    /**
     * Executed on tab completion for this command, returning a list of
     * options the player can tab through.
     *
     * @param sender Source object which is executing this command
     * @param alias the alias being used
     * @param args All arguments passed to the command, split via ' '
     * @return a list of tab-completions for the specified arguments. This
     *     will never be null. List may be immutable.
     * @throws IllegalArgumentException if sender, alias, or args is null
     */
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return tabComplete0(sender, alias, args, null);
    }

    /**
     * Executed on tab completion for this command, returning a list of
     * options the player can tab through.
     *
     * @param sender Source object which is executing this command
     * @param alias the alias being used
     * @param args All arguments passed to the command, split via ' '
     * @param location The position looked at by the sender, or null if none
     * @return a list of tab-completions for the specified arguments. This
     *     will never be null. List may be immutable.
     * @throws IllegalArgumentException if sender, alias, or args is null
     */
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args, @Nullable Location location) throws IllegalArgumentException {
        return tabComplete(sender, alias, args);
    }

    @NotNull
    private List<String> tabComplete0(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        Preconditions.checkArgument(args != null, "Arguments cannot be null");
        Preconditions.checkArgument(alias != null, "Alias cannot be null");

        if (args.length == 0 || !sender.getServer().suggestPlayerNamesWhenNullTabCompletions()) { // Paper - allow preventing player name suggestions by default
            return ImmutableList.of();
        }

        String lastWord = args[args.length - 1];

        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        ArrayList<String> matchedPlayers = new ArrayList<String>();
        for (Player player : sender.getServer().getOnlinePlayers()) {
            String name = player.getName();
            if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                matchedPlayers.add(name);
            }
        }

        Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
        return matchedPlayers;
    }

    /**
     * Returns the name of this command
     *
     * @return Name of this command
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this command.
     * <p>
     * May only be used before registering the command.
     * Will return true if the new name is set, and false
     * if the command has already been registered.
     *
     * @param name New command name
     * @return returns true if the name change happened instantly or false if
     *     the command was already registered
     */
    public boolean setName(@NotNull String name) {
        if (!isRegistered()) {
            this.name = (name == null) ? "" : name;
            return true;
        }
        return false;
    }

    /**
     * Gets the permission required by users to be able to perform this
     * command
     *
     * @return Permission name, or null if none
     */
    @Nullable
    public String getPermission() {
        return permission;
    }

    /**
     * Sets the permission required by users to be able to perform this
     * command
     *
     * @param permission Permission name or null
     */
    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }

    /**
     * Tests the given {@link CommandSender} to see if they can perform this
     * command.
     * <p>
     * If they do not have permission, they will be informed that they cannot
     * do this.
     *
     * @param target User to test
     * @return true if they can use it, otherwise false
     */
    public boolean testPermission(@NotNull CommandSender target) {
        if (testPermissionSilent(target)) {
            return true;
        }

            // Paper start - use components for permissionMessage
        net.kyori.adventure.text.Component permissionMessage = this.permissionMessage != null ? this.permissionMessage : Bukkit.permissionMessage();
        if (!permissionMessage.equals(net.kyori.adventure.text.Component.empty())) {
            target.sendMessage(permissionMessage.replaceText(net.kyori.adventure.text.TextReplacementConfig.builder().matchLiteral("<permission>").replacement(permission).build()));
            // Paper end
        }

        return false;
    }

    /**
     * Tests the given {@link CommandSender} to see if they can perform this
     * command.
     * <p>
     * No error is sent to the sender.
     *
     * @param target User to test
     * @return true if they can use it, otherwise false
     */
    public boolean testPermissionSilent(@NotNull CommandSender target) {
        if ((permission == null) || (permission.length() == 0)) {
            return true;
        }

        for (String p : permission.split(";")) {
            if (target.hasPermission(p)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the label for this command
     *
     * @return Label of this command
     */
    @NotNull
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of this command.
     * <p>
     * May only be used before registering the command.
     * Will return true if the new name is set, and false
     * if the command has already been registered.
     *
     * @param name The command's name
     * @return returns true if the name change happened instantly or false if
     *     the command was already registered
     */
    public boolean setLabel(@NotNull String name) {
        if (name == null) {
            name = "";
        }
        this.nextLabel = name;
        if (!isRegistered()) {
            this.label = name;
            return true;
        }
        return false;
    }

    /**
     * Registers this command to a CommandMap.
     * Once called it only allows changes the registered CommandMap
     *
     * @param commandMap the CommandMap to register this command to
     * @return true if the registration was successful (the current registered
     *     CommandMap was the passed CommandMap or null) false otherwise
     */
    public boolean register(@NotNull CommandMap commandMap) {
        if (allowChangesFrom(commandMap)) {
            this.commandMap = commandMap;
            return true;
        }

        return false;
    }

    /**
     * Unregisters this command from the passed CommandMap applying any
     * outstanding changes
     *
     * @param commandMap the CommandMap to unregister
     * @return true if the unregistration was successful (the current
     *     registered CommandMap was the passed CommandMap or null) false
     *     otherwise
     */
    public boolean unregister(@NotNull CommandMap commandMap) {
        if (allowChangesFrom(commandMap)) {
            this.commandMap = null;
            this.activeAliases = new ArrayList<String>(this.aliases);
            this.label = this.nextLabel;
            return true;
        }

        return false;
    }

    private boolean allowChangesFrom(@NotNull CommandMap commandMap) {
        return (null == this.commandMap || this.commandMap == commandMap);
    }

    /**
     * Returns the current registered state of this command
     *
     * @return true if this command is currently registered false otherwise
     */
    public boolean isRegistered() {
        return (null != this.commandMap);
    }

    /**
     * Returns a list of active aliases of this command
     *
     * @return List of aliases
     */
    @NotNull
    public List<String> getAliases() {
        return activeAliases;
    }

    /**
     * Returns a message to be displayed on a failed permission check for this
     * command
     *
     * @return Permission check failed message
     * @deprecated permission messages have not worked for player-executed
     * commands since 1.13 as clients without permission to execute a command
     * are unaware of its existence and therefore will not send an unknown
     * command execution to the server. This message will only ever be shown to
     * consoles or when this command is executed with
     * {@link Bukkit#dispatchCommand(CommandSender, String)}.
     */
    @Deprecated(since = "1.20.4")
    @Nullable
    public String getPermissionMessage() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serializeOrNull(permissionMessage); // Paper
    }

    /**
     * Gets a brief description of this command
     *
     * @return Description of this command
     */
    @NotNull
    public String getDescription() {
        return description;
    }

    /**
     * Gets an example usage of this command
     *
     * @return One or more example usages
     */
    @NotNull
    public String getUsage() {
        return usageMessage;
    }

    /**
     * Sets the list of aliases to request on registration for this command.
     * This is not effective outside of defining aliases in the {@link
     * PluginDescriptionFile#getCommands()} (under the
     * `<code>aliases</code>' node) is equivalent to this method.
     *
     * @param aliases aliases to register to this command
     * @return this command object, for chaining
     */
    @NotNull
    public Command setAliases(@NotNull List<String> aliases) {
        this.aliases = aliases;
        if (!isRegistered()) {
            this.activeAliases = new ArrayList<String>(aliases);
        }
        return this;
    }

    /**
     * Sets a brief description of this command. Defining a description in the
     * {@link PluginDescriptionFile#getCommands()} (under the
     * `<code>description</code>' node) is equivalent to this method.
     *
     * @param description new command description
     * @return this command object, for chaining
     */
    @NotNull
    public Command setDescription(@NotNull String description) {
        this.description = (description == null) ? "" : description;
        return this;
    }

    /**
     * Sets the message sent when a permission check fails
     *
     * @param permissionMessage new permission message, null to indicate
     *     default message, or an empty string to indicate no message
     * @return this command object, for chaining
     * @deprecated permission messages have not worked for player-executed
     * commands since 1.13 as clients without permission to execute a command
     * are unaware of its existence and therefore will not send an unknown
     * command execution to the server. This message will only ever be shown to
     * consoles or when this command is executed with
     * {@link Bukkit#dispatchCommand(CommandSender, String)}.
     */
    @Deprecated(since = "1.20.4")
    @NotNull
    public Command setPermissionMessage(@Nullable String permissionMessage) {
        this.permissionMessage = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserializeOrNull(permissionMessage); // Paper
        return this;
    }

    /**
     * Sets the example usage of this command
     *
     * @param usage new example usage
     * @return this command object, for chaining
     */
    @NotNull
    public Command setUsage(@NotNull String usage) {
        this.usageMessage = (usage == null) ? "" : usage;
        return this;
    }
    // Paper start
    /**
     * Gets the permission message.
     *
     * @return the permission message
     * @deprecated permission messages have not worked for player-executed
     * commands since 1.13 as clients without permission to execute a command
     * are unaware of its existence and therefore will not send an unknown
     * command execution to the server. This message will only ever be shown to
     * consoles or when this command is executed with
     * {@link Bukkit#dispatchCommand(CommandSender, String)}.
     */
    @Deprecated
    public net.kyori.adventure.text.@Nullable Component permissionMessage() {
        return this.permissionMessage;
    }

    /**
     * Sets the permission message.
     *
     * @param permissionMessage the permission message
     * @deprecated permission messages have not worked for player-executed
     * commands since 1.13 as clients without permission to execute a command
     * are unaware of its existence and therefore will not send an unknown
     * command execution to the server. This message will only ever be shown to
     * consoles or when this command is executed with
     * {@link Bukkit#dispatchCommand(CommandSender, String)}.
     */
    @Deprecated
    public void permissionMessage(net.kyori.adventure.text.@Nullable Component permissionMessage) {
        this.permissionMessage = permissionMessage;
    }
    // Paper end

    public static void broadcastCommandMessage(@NotNull CommandSender source, @NotNull String message) {
        broadcastCommandMessage(source, message, true);
    }

    public static void broadcastCommandMessage(@NotNull CommandSender source, @NotNull String message, boolean sendToSource) {
        // Paper start
        broadcastCommandMessage(source, net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(message), sendToSource);
    }

    public static void broadcastCommandMessage(@NotNull CommandSender source, net.kyori.adventure.text.@NotNull Component message) {
        broadcastCommandMessage(source, message, true);
    }

    public static void broadcastCommandMessage(@NotNull CommandSender source, net.kyori.adventure.text.@NotNull Component message, boolean sendToSource) {
        net.kyori.adventure.text.TextComponent.Builder result = net.kyori.adventure.text.Component.text()
            .color(net.kyori.adventure.text.format.NamedTextColor.WHITE)
            .decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false)
            .append(source.name())
            .append(net.kyori.adventure.text.Component.text(": "))
            .append(message);
        // Paper end

        if (source instanceof BlockCommandSender) {
            BlockCommandSender blockCommandSender = (BlockCommandSender) source;

            if (!blockCommandSender.getBlock().getWorld().getGameRuleValue(GameRule.COMMAND_BLOCK_OUTPUT)) {
                Bukkit.getConsoleSender().sendMessage(result);
                return;
            }
        } else if (source instanceof CommandMinecart) {
            CommandMinecart commandMinecart = (CommandMinecart) source;

            if (!commandMinecart.getWorld().getGameRuleValue(GameRule.COMMAND_BLOCK_OUTPUT)) {
                Bukkit.getConsoleSender().sendMessage(result);
                return;
            }
        }

        Set<Permissible> users = Bukkit.getPluginManager().getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
        // Paper start
        net.kyori.adventure.text.TextComponent.Builder colored = net.kyori.adventure.text.Component.text()
            .color(net.kyori.adventure.text.format.NamedTextColor.GRAY)
            .decorate(net.kyori.adventure.text.format.TextDecoration.ITALIC)
            .append(net.kyori.adventure.text.Component.text("["), result, net.kyori.adventure.text.Component.text("]"));
        // Paper end

        if (sendToSource && !(source instanceof ConsoleCommandSender)) {
            source.sendMessage(message);
        }

        for (Permissible user : users) {
            if (user instanceof CommandSender && user.hasPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE)) {
                CommandSender target = (CommandSender) user;

                if (target instanceof ConsoleCommandSender) {
                    target.sendMessage(result);
                } else if (target != source) {
                    target.sendMessage(colored);
                }
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' + name + ')';
    }

    // Paper start
    @org.jetbrains.annotations.ApiStatus.Internal
    public boolean canBeOverriden() { return false; }
    // Paper end
}
