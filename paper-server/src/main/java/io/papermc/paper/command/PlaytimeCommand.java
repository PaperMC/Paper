package io.papermc.paper.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@DefaultQualifier(NonNull.class)
public final class PlaytimeCommand extends Command {

    public PlaytimeCommand(final String name) {
        super(name);
        this.description = "View a player's total playtime";
        this.usageMessage = "/playtime [player]";
        this.setPermission("bukkit.command.playtime");
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!testPermission(sender)) return true;

        final OfflinePlayer target;
        final String targetName;

        if (args.length == 0) {
            if (!(sender instanceof final Player self)) {
                sender.sendMessage(text("Console must specify a player name.", RED));
                return true;
            }
            target = self;
            targetName = self.getName();
        } else {
            if (!sender.hasPermission("bukkit.command.playtime.others")) {
                sender.sendMessage(text("You don't have permission to check other players' playtime.", RED));
                return true;
            }
            final Player online = Bukkit.getPlayerExact(args[0]);
            if (online != null) {
                target = online;
                targetName = online.getName();
            } else {
                @SuppressWarnings("deprecation")
                final OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
                if (!offline.hasPlayedBefore()) {
                    sender.sendMessage(text("Player not found: " + args[0], RED));
                    return true;
                }
                target = offline;
                targetName = offline.getName() != null ? offline.getName() : args[0];
            }
        }

        final int ticks;
        try {
            ticks = target.getStatistic(Statistic.PLAY_ONE_MINUTE);
        } catch (final Exception e) {
            sender.sendMessage(text("Could not retrieve playtime for " + targetName + ".", RED));
            return true;
        }

        sender.sendMessage(
            text(targetName, GOLD)
                .append(text("'s playtime: ", GRAY))
                .append(text(formatTicks(ticks), YELLOW))
        );
        return true;
    }

    private static String formatTicks(final int ticks) {
        long seconds = ticks / 20;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        final long days = hours / 24;
        minutes %= 60;
        hours %= 24;

        if (days > 0) {
            return days + "d " + hours + "h " + minutes + "m";
        } else if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args, final Location location) {
        if (args.length == 1 && sender.hasPermission("bukkit.command.playtime.others")) {
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
