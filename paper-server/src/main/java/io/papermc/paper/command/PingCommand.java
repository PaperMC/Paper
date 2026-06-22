package io.papermc.paper.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@DefaultQualifier(NonNull.class)
public final class PingCommand extends Command {

    public PingCommand(final String name) {
        super(name);
        this.description = "View the ping of a player";
        this.usageMessage = "/ping [player]";
        this.setPermission("bukkit.command.ping");
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!testPermission(sender)) return true;

        final Player target;
        if (args.length == 0) {
            if (!(sender instanceof final Player self)) {
                sender.sendMessage(text("Console must specify a player name.", RED));
                return true;
            }
            target = self;
        } else {
            if (!sender.hasPermission("bukkit.command.ping.others")) {
                sender.sendMessage(text("You don't have permission to check other players' ping.", RED));
                return true;
            }
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(text("Player not found: " + args[0], RED));
                return true;
            }
        }

        final int ping = target.getPing();
        sender.sendMessage(
            text(target.getName(), GOLD)
                .append(text("'s ping: ", GRAY))
                .append(text(ping + "ms", pingColor(ping)))
        );
        return true;
    }

    private static net.kyori.adventure.text.format.TextColor pingColor(final int ping) {
        if (ping < 100) return GREEN;
        if (ping < 200) return YELLOW;
        return RED;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args, final Location location) {
        if (args.length == 1 && sender.hasPermission("bukkit.command.ping.others")) {
            final List<String> names = new ArrayList<>();
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (sender instanceof final Player p && !p.canSee(player)) continue;
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    names.add(player.getName());
                }
            }
            return names;
        }
        return Collections.emptyList();
    }
}
