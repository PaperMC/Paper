package io.papermc.paper.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class NSPTCommand extends Command {

    private static final Component SLASH = text("/");

    public NSPTCommand(String name) {
        super(name);
        this.description = "View server tick times in nanoseconds";
        this.usageMessage = "/nspt";
        this.setPermission("bukkit.command.nspt");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String @NotNull [] args, Location location) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args) {
        if (!testPermission(sender)) return true;

        MinecraftServer server = MinecraftServer.getServer();

        List<Component> times = new ArrayList<>();
        times.addAll(eval(server.tickTimes5s.getTimes()));
        times.addAll(eval(server.tickTimes10s.getTimes()));
        times.addAll(eval(server.tickTimes60s.getTimes()));

        sender.sendMessage(text().content("Server tick times ").color(GOLD)
            .append(text().color(YELLOW)
                .append(
                    text("("),
                    text("avg", GRAY),
                    text("/"),
                    text("min", GRAY),
                    text("/"),
                    text("max", GRAY),
                    text(")")
                )
            ).append(
                text(" from last 5s"),
                text(",", GRAY),
                text(" 10s"),
                text(",", GRAY),
                text(" 1m"),
                text(":", YELLOW)
            )
        );
        sender.sendMessage(text().content("◴ ").color(GOLD)
            .append(text().color(GRAY)
                .append(
                    times.get(0), SLASH, times.get(1), SLASH, times.get(2), text(", ", YELLOW),
                    times.get(3), SLASH, times.get(4), SLASH, times.get(5), text(", ", YELLOW),
                    times.get(6), SLASH, times.get(7), SLASH, times.get(8)
                )
            )
        );
        return true;
    }

    private static List<Component> eval(long[] times) {
        long min = Integer.MAX_VALUE;
        long max = 0L;
        long total = 0L;
        for (long value : times) {
            if (value > 0L && value < min) min = value;
            if (value > max) max = value;
            total += value;
        }
        double avgD = ((double) total / (double) times.length);
        return Arrays.asList(getColor(avgD), getColor(min), getColor(max));
    }

    private static Component getColor(double avg) {
        return text(avg >= 5E+7 ? "c" : avg >= (4E+7) ? "e" : "a" + avg);
    }
}
