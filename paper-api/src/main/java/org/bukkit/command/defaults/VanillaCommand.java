package org.bukkit.command.defaults;

import java.util.List;
import org.bukkit.command.Command;

public abstract class VanillaCommand extends Command {
    protected VanillaCommand(String name) {
        super(name);
    }

    protected VanillaCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    public abstract boolean matches(String input);
}
