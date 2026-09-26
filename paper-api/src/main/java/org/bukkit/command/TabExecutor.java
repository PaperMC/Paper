package org.bukkit.command;

/**
 * This class is provided as a convenience to implement both TabCompleter and
 * CommandExecutor.
 *
 * @deprecated plugin developers should prefer to use the
 *     <a href="https://docs.papermc.io/paper/dev/command-api/basics/introduction/">Brigadier command API</a>.
 *     For a direct alternative to Bukkit commands, <a href="https://docs.papermc.io/paper/dev/command-api/misc/basic-command/">Basic commands</a> are recommended
 */
@Deprecated(since = "26.4")
public interface TabExecutor extends TabCompleter, CommandExecutor {
}
