package org.bukkit.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

/**
 * Represents a Command, which executes various tasks upon user input
 */
public abstract class Command {
    private String name;
    private String nextLabel;
    private String label;
    private List<String> aliases;
    private List<String> activeAliases;
    private CommandMap commandMap = null;
    protected String description = "";
    protected String usageMessage;
    private String permission;
    private String permissionMessage;

    protected Command(String name) {
        this(name, "", "/" + name, new ArrayList<String>());
    }

    protected Command(String name, String description, String usageMessage, List<String> aliases) {
        this.name = name;
        this.nextLabel = name;
        this.label = name;
        this.description = description;
        this.usageMessage = usageMessage;
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
    public abstract boolean execute(CommandSender sender, String commandLabel, String[] args);

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
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
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
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        return tabComplete(sender, alias, args);
    }

    private List<String> tabComplete0(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 0) {
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
    public boolean setName(String name) {
        if (!isRegistered()) {
            this.name = name;
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
    public String getPermission() {
        return permission;
    }

    /**
     * Sets the permission required by users to be able to perform this
     * command
     *
     * @param permission Permission name or null
     */
    public void setPermission(String permission) {
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
    public boolean testPermission(CommandSender target) {
        if (testPermissionSilent(target)) {
            return true;
        }

        if (permissionMessage == null) {
            target.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
        } else if (permissionMessage.length() != 0) {
            for (String line : permissionMessage.replace("<permission>", permission).split("\n")) {
                target.sendMessage(line);
            }
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
    public boolean testPermissionSilent(CommandSender target) {
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
    public boolean setLabel(String name) {
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
    public boolean register(CommandMap commandMap) {
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
     * @return true if the unregistration was successfull (the current
     *     registered CommandMap was the passed CommandMap or null) false
     *     otherwise
     */
    public boolean unregister(CommandMap commandMap) {
        if (allowChangesFrom(commandMap)) {
            this.commandMap = null;
            this.activeAliases = new ArrayList<String>(this.aliases);
            this.label = this.nextLabel;
            return true;
        }

        return false;
    }

    private boolean allowChangesFrom(CommandMap commandMap) {
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
    public List<String> getAliases() {
        return activeAliases;
    }

    /**
     * Returns a message to be displayed on a failed permission check for this
     * command
     *
     * @return Permission check failed message
     */
    public String getPermissionMessage() {
        return permissionMessage;
    }

    /**
     * Gets a brief description of this command
     *
     * @return Description of this command
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets an example usage of this command
     *
     * @return One or more example usages
     */
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
    public Command setAliases(List<String> aliases) {
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
    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the message sent when a permission check fails
     *
     * @param permissionMessage new permission message, null to indicate
     *     default message, or an empty string to indicate no message
     * @return this command object, for chaining
     */
    public Command setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }

    /**
     * Sets the example usage of this command
     *
     * @param usage new example usage
     * @return this command object, for chaining
     */
    public Command setUsage(String usage) {
        this.usageMessage = usage;
        return this;
    }

    public static void broadcastCommandMessage(CommandSender source, String message) {
        broadcastCommandMessage(source, message, true);
    }

    public static void broadcastCommandMessage(CommandSender source, String message, boolean sendToSource) {
        String result = source.getName() + ": " + message;

        if (source instanceof BlockCommandSender) {
            BlockCommandSender blockCommandSender = (BlockCommandSender) source;

            if (blockCommandSender.getBlock().getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
                Bukkit.getConsoleSender().sendMessage(result);
                return;
            }
        } else if (source instanceof CommandMinecart) {
            CommandMinecart commandMinecart = (CommandMinecart) source;

            if (commandMinecart.getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
                Bukkit.getConsoleSender().sendMessage(result);
                return;
            }
        }

        Set<Permissible> users = Bukkit.getPluginManager().getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
        String colored = ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + result + ChatColor.GRAY + ChatColor.ITALIC + "]";

        if (sendToSource && !(source instanceof ConsoleCommandSender)) {
            source.sendMessage(message);
        }

        for (Permissible user : users) {
            if (user instanceof CommandSender) {
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
}
