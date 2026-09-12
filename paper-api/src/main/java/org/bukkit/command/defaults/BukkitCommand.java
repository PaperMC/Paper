package org.bukkit.command.defaults;

import java.util.List;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

/**
 * @deprecated plugin developers should prefer to use the
 *     <a href="https://docs.papermc.io/paper/dev/command-api/basics/introduction/">Brigadier command API</a>
 */
@Deprecated(since = "26.3")
public abstract class BukkitCommand extends Command {
    protected BukkitCommand(@NotNull String name) {
        super(name);
    }

    protected BukkitCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}
