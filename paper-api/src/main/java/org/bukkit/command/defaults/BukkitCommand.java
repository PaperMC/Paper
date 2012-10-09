package org.bukkit.command.defaults;

import java.util.List;

import org.bukkit.command.Command;

public abstract class BukkitCommand extends Command {
    protected BukkitCommand(String name) {
        super(name);
    }

    protected BukkitCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}
