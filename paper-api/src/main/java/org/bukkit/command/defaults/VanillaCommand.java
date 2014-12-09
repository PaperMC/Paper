package org.bukkit.command.defaults;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@Deprecated
public abstract class VanillaCommand extends Command {
    static final int MAX_COORD = 30000000;
    static final int MIN_COORD_MINUS_ONE = -30000001;
    static final int MIN_COORD = -30000000;

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

    int getInteger(CommandSender sender, String value, int min, int max) {
        return getInteger(sender, value, min, max, false);
    }

    int getInteger(CommandSender sender, String value, int min, int max, boolean Throws) {
        int i = min;

        try {
            i = Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            if (Throws) {
                throw new NumberFormatException(String.format("%s is not a valid number", value));
            }
        }

        if (i < min) {
            i = min;
        } else if (i > max) {
            i = max;
        }

        return i;
    }

    Integer getInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static double getRelativeDouble(double original, CommandSender sender, String input) {
        if (input.startsWith("~")) {
            double value = getDouble(sender, input.substring(1));
            if (value == MIN_COORD_MINUS_ONE) {
                return MIN_COORD_MINUS_ONE;
            }
            return original + value;
        } else {
            return getDouble(sender, input);
        }
    }

    public static double getDouble(CommandSender sender, String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            return MIN_COORD_MINUS_ONE;
        }
    }

    public static double getDouble(CommandSender sender, String input, double min, double max) {
        double result = getDouble(sender, input);

        // TODO: This should throw an exception instead.
        if (result < min) {
            result = min;
        } else if (result > max) {
            result = max;
        }

        return result;
    }

    String createString(String[] args, int start) {
        return createString(args, start, " ");
    }

    String createString(String[] args, int start, String glue) {
        StringBuilder string = new StringBuilder();

        for (int x = start; x < args.length; x++) {
            string.append(args[x]);
            if (x != args.length - 1) {
                string.append(glue);
            }
        }

        return string.toString();
    }
}
