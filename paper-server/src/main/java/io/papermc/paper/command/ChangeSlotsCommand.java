package io.papermc.paper.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@DefaultQualifier(NonNull.class)
public final class ChangeSlotsCommand extends Command {

    public ChangeSlotsCommand(final String name) {
        super(name);
        this.description = "Change server max player slots";
        this.usageMessage = "/changeslots <amount>";
        this.setPermission("bukkit.command.changeslots");
        this.setAliases(List.of("slots", "maxplayers", "setslots"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("20", "50", "100", "200");
        }
        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length == 0) {
            sender.sendMessage(text("Usage: " + this.usageMessage, RED));
            return true;
        }

        try {
            int newMaxPlayers = Integer.parseInt(args[0]);

            if (newMaxPlayers < 1) {
                sender.sendMessage(text("Max players must be at least 1", RED));
                return true;
            }

            Bukkit.getServer().setMaxPlayers(newMaxPlayers);

            sender.sendMessage(text("Max players changed to " + newMaxPlayers));

        } catch (NumberFormatException e) {
            sender.sendMessage(text("'" + args[0] + "' is not a valid number", RED));
            return true;
        }

        return true;
    }
}
