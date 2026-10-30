package io.papermc.paper.command;

import ca.spottedleaf.common.time.TickData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@NullMarked
public class PaperMSPTCommand {
    public static final String DESCRIPTION = "View server tick times";

    private static final DecimalFormat DF = new DecimalFormat("########0.0");
    private static final Component SLASH = text("/");

    public static LiteralCommandNode<CommandSourceStack> create() {
        final PaperMSPTCommand command = new PaperMSPTCommand();

        return Commands.literal("mspt")
            .requires(source -> source.getSender().hasPermission("bukkit.command.mspt"))
            .executes(command::execute)
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
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
        return Command.SINGLE_SUCCESS;
    }

    private static List<Component> eval(TickData tickData) {
        TickData.TickReportData reportData = tickData.generateTickReport(null, System.nanoTime(), MinecraftServer.getServer().tickRateManager().nanosecondsPerTick());
        double avgD = reportData == null ? 0.0 : reportData.timePerTickData().segmentAll().average() * 1.0E-6D;
        double minD = reportData == null ? 0.0 : reportData.timePerTickData().segmentAll().least() * 1.0E-6D;
        double maxD = reportData == null ? 0.0 : reportData.timePerTickData().segmentAll().greatest() * 1.0E-6D;
        return Arrays.asList(getColor(avgD), getColor(minD), getColor(maxD));
    }

    private static Component getColor(double avg) {
        return text(DF.format(avg), avg >= 50 ? RED : avg >= 40 ? YELLOW : GREEN);
    }
}
