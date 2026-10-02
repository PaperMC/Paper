package org.bukkit.command.defaults;

import java.util.List;
import org.bukkit.command.Command;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/// @apiNote plugin developers should prefer to use the
///     [Brigadier command API](https://docs.papermc.io/paper/dev/command-api/basics/introduction/).
///     For a direct alternative to Bukkit commands, [Basic commands](https://docs.papermc.io/paper/dev/command-api/misc/basic-command/) are recommended
@ApiStatus.Obsolete(since = "26.3")
public abstract class BukkitCommand extends Command {
    protected BukkitCommand(@NotNull String name) {
        super(name);
    }

    protected BukkitCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}
