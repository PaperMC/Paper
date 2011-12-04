package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.permissions.Permissible;

/**
 * Represents a Command, which executes various tasks upon user input
 */
public abstract class Command {
    private final String name;
    private String nextLabel;
    private String label;
    private List<String> aliases;
    private List<String> activeAliases;
    private CommandMap commandMap = null;
    protected String description = "";
    protected String usageMessage;
    private String permission;

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
     * Returns the name of this command
     *
     * @return Name of this command
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the permission required by users to be able to perform this command
     *
     * @return Permission name, or null if none
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Sets the permission required by users to be able to perform this command
     *
     * @param permission Permission name or null
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Tests the given {@link CommandSender} to see if they can perform this command.
     *
     * If they do not have permission, they will be informed that they cannot do this.
     *
     * @param target User to test
     * @return true if they can use it, otherwise false
     */
    public boolean testPermission(CommandSender target) {
        if ((permission == null) || (permission.length() == 0) || (target.hasPermission(permission))) {
            return true;
        }

        target.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you beleieve this is in error.");
        return false;
    }

    /**
     * Returns the current lable for this command
     *
     * @return Label of this command or null if not registered
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of this command
     * If the command is currently registered the label change will only take effect after
     * its been reregistered e.g. after a /reload
     *
     * @param name The command's name
     * @return returns true if the name change happened instantly or false if it was scheduled for reregistration
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
     * Registers this command to a CommandMap
     * Once called it only allows changes the registered CommandMap
     *
     * @param commandMap the CommandMap to register this command to
     * @return true if the registration was successful (the current registered CommandMap was the passed CommandMap or null) false otherwise
     */
    public boolean register(CommandMap commandMap) {
        if (allowChangesFrom(commandMap)) {
            this.commandMap = commandMap;
            return true;
        }

        return false;
    }

    /**
     * Unregisters this command from the passed CommandMap applying any outstanding changes
     *
     * @param commandMap the CommandMap to unregister
     * @return true if the unregistration was successfull (the current registered CommandMap was the passed CommandMap or null) false otherwise
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
     * Sets the list of aliases to request on registration for this command
     *
     * @param aliases Aliases to register to this command
     * @return This command object, for linking
     */
    public Command setAliases(List<String> aliases) {
        this.aliases = aliases;
        if (!isRegistered()) {
            this.activeAliases = new ArrayList<String>(aliases);
        }
        return this;
    }

    /**
     * Sets a brief description of this command
     *
     * @param description New command description
     * @return This command object, for linking
     */
    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the example usage of this command
     *
     * @param usage New example usage
     * @return This command object, for linking
     */
    public Command setUsage(String usage) {
        this.usageMessage = usage;
        return this;
    }

    public static void broadcastCommandMessage(CommandSender source, String message) {
        Set<Permissible> users = Bukkit.getPluginManager().getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
        String result = source.getName() + ": " + message;
        String colored = ChatColor.GRAY + "(" + result + ")";

        if (!(source instanceof ConsoleCommandSender)) {
            source.sendMessage(message);
        }

        for (Permissible user : users) {
            if (user instanceof CommandSender) {
                CommandSender target = (CommandSender)user;

                if (target instanceof ConsoleCommandSender) {
                    target.sendMessage(result);
                } else if (target != source) {
                    target.sendMessage(colored);
                }
            }
        }
    }
}
