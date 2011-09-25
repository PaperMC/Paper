package org.bukkit.command;

import java.util.List;

public interface CommandMap {

    /**
     * Registers all the commands belonging to a certain plugin.
     * Caller can use:-
     * command.getName() to determine the label registered for this command
     * command.getAliases() to determine the aliases which where registered
     *
     * @param fallbackPrefix a prefix which is prepended to each command with a ':' one or more times to make the command unique
     * @param commands a list of commands to register
     */
    public void registerAll(String fallbackPrefix, List<Command> commands);

    /**
     * Registers a command. Returns true on success; false if name is already taken and fallback had to be used.
     * Caller can use:-
     * command.getName() to determine the label registered for this command
     * command.getAliases() to determine the aliases which where registered
     *
     * @param label the label of the command, without the '/'-prefix.
     * @param fallbackPrefix a prefix which is prepended to the command with a ':' one or more times to make the command unique
     * @param command the command to register
     * @return true if command was registered with the passed in label, false otherwise, which indicates the fallbackPrefix was used one or more times
     */
    public boolean register(String label, String fallbackPrefix, Command command);

    /**
     * Registers a command. Returns true on success; false if name is already taken and fallback had to be used.
     * Caller can use:-
     * command.getName() to determine the label registered for this command
     * command.getAliases() to determine the aliases which where registered
     *
     * @param fallbackPrefix a prefix which is prepended to the command with a ':' one or more times to make the command unique
     * @param command the command to register, from which label is determined from the command name
     * @return true if command was registered with the passed in label, false otherwise, which indicates the fallbackPrefix was used one or more times
     */
    public boolean register(String fallbackPrefix, Command command);

    /**
     * Looks for the requested command and executes it if found.
     *
     * @param sender The command's sender
     * @param cmdLine command + arguments. Example: "/test abc 123"
     * @return returns false if no target is found, true otherwise.
     * @throws CommandException Thrown when the executor for the given command fails with an unhandled exception
     */
    public boolean dispatch(CommandSender sender, String cmdLine) throws CommandException;

    /**
     * Clears all registered commands.
     */
    public void clearCommands();

    /**
     * Gets the command registered to the specified name
     *
     * @param name Name of the command to retrieve
     * @return Command with the specified name or null if a command with that label doesn't exist
     */
    public Command getCommand(String name);
}
