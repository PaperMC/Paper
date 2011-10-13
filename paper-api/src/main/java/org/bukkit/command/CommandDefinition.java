package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command that can be registered dynamically.
 * 
 * @author sk89q
 */
public class CommandDefinition {
    
    private String name;
    private String description;
    private String usage;
    private List<String> aliases = new ArrayList<String>();
    private String permission;
    
    /**
     * Construct the command with a given name.
     * 
     * @param name The command name (without a preceding /)
     */
    public CommandDefinition(String name) {
        setName(name);
    }
    
    /**
     * Construct the command with a given name and description.
     * 
     * @param name The command name
     * @param description The command description (without a preceding /)
     */
    public CommandDefinition(String name, String description) {
        setName(name);
        setDescription(description);
    }

    /**
     * Get the name.
     * 
     * @return Command name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the command name.
     * 
     * @param name Command name
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Cannot have a null command name");
        }
        
        this.name = name;
    }

    /**
     * Get the description.
     * 
     * @return Command description, possibly null
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the command description.
     * 
     * @param name Command description, possibly null
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the usage string.
     * 
     * @return Command description, possibly null
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets the command usage string.
     * 
     * @param name Command usage string, possibly null
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * Get the list of aliases.
     * 
     * @return List of aliases, possibly null
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Set the list of aliases.
     * 
     * @param aliases List of aliases
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    /**
     * Get the permission.
     * 
     * @return Command permission, possibly null
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Sets the permission.
     * 
     * @param name Command permission, possibly null
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

}
