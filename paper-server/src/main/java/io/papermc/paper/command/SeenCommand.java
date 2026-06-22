package io.papermc.paper.command;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
public final class SeenCommand extends Command {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("MMM dd, yyyy 'at' HH:mm:ss z", Locale.ENGLISH)
        .withZone(ZoneId.systemDefault());

    public SeenCommand(final String name) {
        super(name);
        this.description = "Check when a player was last online";
        this.usageMessage = "/seen <player>";
        this.setPermission("bukkit.command.seen");
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length == 0) {
            sender.sendMessage(text("Usage: " + this.usageMessage, RED));
            return true;
        }

        final String targetName = args[0];
        final Player online = Bukkit.getPlayerExact(targetName);
        if (online != null) {
            sender.sendMessage(
                text(online.getName(), GOLD)
                    .append(text(" is currently ", GRAY))
                    .append(text("online", GREEN))
                    .append(text(".", GRAY))
            );
            return true;
        }

        @SuppressWarnings("deprecation")
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
        if (!offlinePlayer.hasPlayedBefore()) {
            sender.sendMessage(text("Player '" + targetName + "' has never joined this server.", RED));
            return true;
        }

        final long lastPlayed = offlinePlayer.getLastPlayed();
        if (lastPlayed == 0) {
            sender.sendMessage(text("No last seen data available for '" + targetName + "'.", RED));
            return true;
        }

        final String displayName = offlinePlayer.getName() != null ? offlinePlayer.getName() : targetName;
        final String formatted = FORMATTER.format(Instant.ofEpochMilli(lastPlayed));
        sender.sendMessage(
            text(displayName, GOLD)
                .append(text(" was last seen on ", GRAY))
                .append(text(formatted, YELLOW))
                .append(text(".", GRAY))
        );
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args, final Location location) {
        if (args.length == 1) {
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
