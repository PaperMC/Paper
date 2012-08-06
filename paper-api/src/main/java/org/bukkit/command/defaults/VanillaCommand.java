package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class VanillaCommand extends Command {
    static final List<String> EMPTY_LIST = new ArrayList(0);

    protected VanillaCommand(String name) {
        super(name);
    }

    protected VanillaCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    public boolean matches(String input) {
        return input.equalsIgnoreCase(this.getName());
    }

    protected int getInteger(CommandSender sender, String value, int min) {
        return getInteger(sender, value, min, Integer.MAX_VALUE);
    }

    protected int getInteger(CommandSender sender, String value, int min, int max) {
        int i = min;

        try {
            i = Integer.valueOf(value);
        } catch (NumberFormatException ex) {}

        if (i < min) {
            i = min;
        } else if (i > max) {
            i = max;
        }

        return i;
    }

    protected String createString(String[] args, int start) {
        StringBuilder string = new StringBuilder();

        for (int x = start; x < args.length; x++) {
            string.append(args[x]);
            if (x != args.length - 1) {
                string.append(" ");
            }
        }

        return string.toString();
    }
}
