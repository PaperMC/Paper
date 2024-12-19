package org.bukkit.command.defaults;

import java.util.List;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.2.5 R0.1
 */
public abstract class BukkitCommand extends Command {
    protected BukkitCommand(@NotNull String name) {
        super(name);
    }

    protected BukkitCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}
