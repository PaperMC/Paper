package org.bukkit.command;

import java.util.List;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandMap {

    /**
     * Registers all the commands belonging to a certain plugin.
     * <p>
     * Caller can use:-
     * <ul>
     * <li>command.getName() to determine the label registered for this
     *     command
     * <li>command.getAliases() to determine the aliases which where
     *     registered
     * </ul>
     *
     * @param fallbackPrefix a prefix which is prepended to each command with
     *     a ':' one or more times to make the command unique
     * @param commands a list of commands to register
     */
    public void registerAll(@NotNull String fallbackPrefix, @NotNull List<Command> commands);

    /**
     * Registers a command. Returns true on success; false if name is already
     * taken and fallback had to be used.
     * <p>
     * Caller can use:-
     * <ul>
     * <li>command.getName() to determine the label registered for this
     *     command
     * <li>command.getAliases() to determine the aliases which where
     *     registered
     * </ul>
     *
     * @param label the label of the command, without the '/'-prefix.
     * @param fallbackPrefix a prefix which is prepended to the command with a
     *     ':' one or more times to make the command unique
     * @param command the command to register
     * @return true if command was registered with the passed in label, false
     *     otherwise, which indicates the fallbackPrefix was used one or more
     *     times
     */
    public boolean register(@NotNull String label, @NotNull String fallbackPrefix, @NotNull Command command);

    /**
     * Registers a command. Returns true on success; false if name is already
     * taken and fallback had to be used.
     * <p>
     * Caller can use:-
     * <ul>
     * <li>command.getName() to determine the label registered for this
     *     command
     * <li>command.getAliases() to determine the aliases which where
     *     registered
     * </ul>
     *
     * @param fallbackPrefix a prefix which is prepended to the command with a
     *     ':' one or more times to make the command unique
     * @param command the command to register, from which label is determined
     *     from the command name
     * @return true if command was registered with the passed in label, false
     *     otherwise, which indicates the fallbackPrefix was used one or more
     *     times
     */
    public boolean register(@NotNull String fallbackPrefix, @NotNull Command command);

    /**
     * Looks for the requested command and executes it if found.
     *
     * @param sender The command's sender
     * @param cmdLine command + arguments. Example: "/test abc 123"
     * @return returns false if no target is found, true otherwise.
     * @throws CommandException Thrown when the executor for the given command
     *     fails with an unhandled exception
     */
    public boolean dispatch(@NotNull CommandSender sender, @NotNull String cmdLine) throws CommandException;

    /**
     * Clears all registered commands.
     */
    public void clearCommands();

    /**
     * Gets the command registered to the specified name
     *
     * @param name Name of the command to retrieve
     * @return Command with the specified name or null if a command with that
     *     label doesn't exist
     */
    @Nullable
    public Command getCommand(@NotNull String name);

    /**
     * Looks for the requested command and executes an appropriate
     * tab-completer if found. This method will also tab-complete partial
     * commands.
     *
     * @param sender The command's sender.
     * @param cmdLine The entire command string to tab-complete, excluding
     *     initial slash.
     * @return a list of possible tab-completions. This list may be immutable.
     *     Will be null if no matching command of which sender has permission.
     * @throws CommandException Thrown when the tab-completer for the given
     *     command fails with an unhandled exception
     * @throws IllegalArgumentException if either sender or cmdLine are null
     */
    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String cmdLine) throws IllegalArgumentException;

    /**
     * Looks for the requested command and executes an appropriate
     * tab-completer if found. This method will also tab-complete partial
     * commands.
     *
     * @param sender The command's sender.
     * @param cmdLine The entire command string to tab-complete, excluding
     *     initial slash.
     * @param location The position looked at by the sender, or null if none
     * @return a list of possible tab-completions. This list may be immutable.
     *     Will be null if no matching command of which sender has permission.
     * @throws CommandException Thrown when the tab-completer for the given
     *     command fails with an unhandled exception
     * @throws IllegalArgumentException if either sender or cmdLine are null
     */
    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String cmdLine, @Nullable Location location) throws IllegalArgumentException;
}
