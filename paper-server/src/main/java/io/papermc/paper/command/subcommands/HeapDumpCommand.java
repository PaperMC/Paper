package io.papermc.paper.command.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.nio.file.Path;
import java.time.LocalDateTime;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@NullMarked
public final class HeapDumpCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("heap")
            .requires(source -> source.getSender().hasPermission(PaperCommand.BASE_PERM + ".heap"))
            .executes(context -> {
                return dumpHeap(context.getSource().getSender());
            });
    }

    private static int dumpHeap(final CommandSender sender) {
        Path dir = Path.of("dumps");
        String name = "heap-dump-" + PaperCommand.FILENAME_DATE_TIME_FORMATTER.format(LocalDateTime.now());

        sender.sendMessage(text("Writing JVM heap data...", YELLOW));

        Path path = CraftServer.dumpHeap(dir, name);
        if (path != null) {
            sender.sendMessage(
                text("Heap dump saved to", GREEN)
                    .appendSpace()
                    .append(PaperCommand.asFriendlyPath(path))
            );
            return Command.SINGLE_SUCCESS;
        } else {
            sender.sendMessage(text("Failed to write heap dump, see server log for details", RED));
            return 0;
        }
    }
}
