package org.bukkit.command;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class FormattedCommandAlias extends Command {
    private final String[] formatStrings;

    public FormattedCommandAlias(String alias, String[] formatStrings) {
        super(alias);
        this.formatStrings = formatStrings;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean result = false;
        ArrayList<String> commands = new ArrayList<String>();
        for (String formatString : formatStrings) {
            try {
                if (sender instanceof Player) {
                    PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent((Player) sender, "/" + formatString);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return false;
                    } else {
                        formatString = event.getMessage().substring(1);
                    }
                } else if (sender instanceof RemoteConsoleCommandSender) {
                    RemoteServerCommandEvent event = new RemoteServerCommandEvent(sender, formatString);
                    Bukkit.getPluginManager().callEvent(event);
                    formatString = event.getCommand();
                } else if (sender instanceof ConsoleCommandSender) {
                    ServerCommandEvent event = new ServerCommandEvent(sender, formatString);
                    Bukkit.getPluginManager().callEvent(event);
                    formatString = event.getCommand();
                }

                commands.add(buildCommand(formatString, args));
            } catch (Throwable throwable) {
                if (throwable instanceof IllegalArgumentException) {
                    sender.sendMessage(throwable.getMessage());
                } else {
                    sender.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
                }
                Bukkit.getLogger().log(Level.WARNING, "Failed to parse command alias " + commandLabel + ": " + formatString, throwable);
                return false;
            }
        }

        for (String command : commands) {
            result |= Bukkit.dispatchCommand(sender, command);
        }

        return result;
    }

    private String buildCommand(String formatString, String[] args) {
        int index = formatString.indexOf("$");
        while (index != -1) {
            int start = index;

            boolean required = false;
            if (formatString.charAt(index + 1) == '$') {
                required = true;
                // Move index past the second $
                index++;
            }

            // Move index past the $
            index++;
            int position = Character.getNumericValue(formatString.charAt(index)) - 1;
            // Move index past the position
            index++;

            boolean rest = false;
            if (index < formatString.length() && formatString.charAt(index) == '-') {
                rest = true;
                // Move index past the -
                index++;
            }

            int end = index;

            if (required && position >= args.length) {
                throw new IllegalArgumentException("Missing required argument " + (position + 1));
            }

            StringBuilder replacement = new StringBuilder();
            if (rest && position < args.length) {
                for (int i = position; i < args.length; i++) {
                    if (i != position) {
                        replacement.append(' ');
                    }
                    replacement.append(args[i]);
                }
            } else if (position < args.length) {
                replacement.append(args[position]);
            }

            formatString = formatString.substring(0, start) + replacement.toString() + formatString.substring(end);
            // Move index past the replaced data so we don't process it again
            index = start + replacement.length();

            // Move to the next replacement token
            index = formatString.indexOf("$", index);
        }

        return formatString;
    }
}
