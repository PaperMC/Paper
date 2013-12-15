package org.bukkit.command;

import java.util.List;

/**
 * Represents a class which can suggest tab completions for commands.
 */
public interface TabCompleter {

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param alias The alias used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     *     to default to the command executor
     */
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
}
