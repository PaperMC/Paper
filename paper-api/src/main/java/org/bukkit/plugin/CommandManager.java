package org.bukkit.plugin;

import org.bukkit.entity.Player;

public interface CommandManager {
    /**
     * Registers all the commands belonging to a certain plugin.
     * @param plugin
     * @return
     */
    public boolean registerCommands(Plugin plugin);
    
    /**
     * Adds a command to the registeredCommands map. Returns true on success; false if name is already taken.
     * 
     * @param command Name of command, without '/'-prefix.
     * @return Returns true if command string was not already registered; false otherwise.
     */
    public boolean registerCommand(String command, String tooltip, String helpMessage, Plugin plugin);
    
    /** Looks up given string in registeredCommands map and calls the onCommand method on the
     *  appropriate plugin if found. 
     *  
     *   @param cmdLine command + arguments. Example: "/test abc 123"
     *   @return targetFound returns false if no target is found.
     */
    public boolean dispatchCommand(Player sender, String cmdLine);
}
