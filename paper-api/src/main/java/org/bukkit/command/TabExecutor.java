package org.bukkit.command;

import org.jetbrains.annotations.ApiStatus;

/// This class is provided as a convenience to implement both TabCompleter and
/// CommandExecutor.
///
/// @apiNote plugin developers should prefer to use the
///     [Brigadier command API](https://docs.papermc.io/paper/dev/command-api/basics/introduction/).
///     For a direct alternative to Bukkit commands, [Basic commands](https://docs.papermc.io/paper/dev/command-api/misc/basic-command/) are recommended
@ApiStatus.Obsolete(since = "26.3")
public interface TabExecutor extends TabCompleter, CommandExecutor {
}
