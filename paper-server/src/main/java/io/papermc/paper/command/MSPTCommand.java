package io.papermc.paper.command;

import ca.spottedleaf.moonrise.common.time.TickData;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@DefaultQualifier(NonNull.class)
public final class MSPTCommand extends Command {
    private static final ThreadLocal<DecimalFormat> ONE_DECIMAL_PLACES = ThreadLocal.withInitial(() -> {
        return new DecimalFormat("########0.0");
    });

    private static final Component SLASH = text("/");

    public MSPTCommand(final String name) {
        super(name);
        this.description = "View server tick times";
        this.usageMessage = "/mspt";
        this.setPermission("bukkit.command.mspt");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        MinecraftServer server = MinecraftServer.getServer();

        List<Component> times = new ArrayList<>();
        times.addAll(eval(server.tickTimes5s));
        times.addAll(eval(server.tickTimes10s));
        times.addAll(eval(server.tickTimes1m));

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

    private static List<Component> eval(TickData tickData) {
        TickData.TickReportData reportData = tickData.generateTickReport(null, System.nanoTime(), MinecraftServer.getServer().tickRateManager().nanosecondsPerTick());
        double avgD = reportData == null ? 0.0 : reportData.timePerTickData().segmentAll().average() * 1.0E-6D;
        double minD = reportData == null ? 0.0 : reportData.timePerTickData().segmentAll().least() * 1.0E-6D;
        double maxD = reportData == null ? 0.0 : reportData.timePerTickData().segmentAll().greatest() * 1.0E-6D;
        return Arrays.asList(getColor(avgD), getColor(minD), getColor(maxD));
    }

    private static Component getColor(double avg) {
        return text(ONE_DECIMAL_PLACES.get().format(avg), avg >= 50 ? RED : avg >= 40 ? YELLOW : GREEN);
    }
}
