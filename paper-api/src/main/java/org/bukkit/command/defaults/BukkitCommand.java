package org.bukkit.command.defaults;

import org.bukkit.command.Command;

import java.util.List;

public abstract class BukkitCommand extends Command{
    protected BukkitCommand(String name) {
        super(name);
    }

    protected BukkitCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}
