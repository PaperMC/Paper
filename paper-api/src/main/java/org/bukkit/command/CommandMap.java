package org.bukkit.command;

import java.util.List;

public interface CommandMap {
    /**
     * Registers all the commands belonging to a certain plugin.
     * @param plugin
     * @return
     */
    public void registerAll(String fallbackPrefix, List<Command> commands);

    /**
     * Registers a command. Returns true on success; false if name is already taken and fallback had to be used.
     *
     * @param a label for this command, without the '/'-prefix.
     * @return Returns true if command was registered; false if label was already in use.
     */
    public boolean register(String label, String fallbackPrefix, Command command);

    /**
     * Looks for the requested command and executes it if found.
     *
     * @param cmdLine command + arguments. Example: "/test abc 123"
     * @return targetFound returns false if no target is found.
     * @throws CommandException Thrown when the executor for the given command fails with an unhandled exception
     */
    public boolean dispatch(CommandSender sender, String cmdLine);

    /**
     * Clears all registered commands.
     */
    public void clearCommands();

    /**
     * Gets the command registered to the specified name
     *
     * @param name Name of the command to retrieve
     * @return Command with the specified name
     */
    public Command getCommand(String name);
}
