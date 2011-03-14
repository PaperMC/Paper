package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Command, which executes various tasks upon user input
 */
public abstract class Command {
    private final String name;
    private List<String> aliases;
    protected String description = "";
    protected String usageMessage;

    protected Command(String name) {
        this.name = name;
        this.aliases = new ArrayList<String>();
        this.usageMessage = "/" + name;
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
     * Returns a list of aliases registered to this command
     *
     * @return List of aliases
     */
    public List<String> getAliases() {
        return aliases;
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
     * Sets the list of aliases registered to this command
     *
     * @param aliases Aliases to register to this command
     * @return This command object, for linking
     */
    public Command setAliases(List<String> aliases) {
        this.aliases = aliases;
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
}