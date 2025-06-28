package io.papermc.paper.command.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.command.Command;
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
            .requires(stack -> stack.getSender().hasPermission(PaperCommand.BASE_PERM + ".heap"))
            .executes(ctx -> {
                dumpHeap(ctx.getSource().getSender());
                return com.mojang.brigadier.Command.SINGLE_SUCCESS;
            });
    }

    private static void dumpHeap(final CommandSender sender) {
        java.nio.file.Path dir = java.nio.file.Paths.get("./dumps");
        String name = "heap-dump-" + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss").format(LocalDateTime.now());

        Command.broadcastCommandMessage(sender, text("Writing JVM heap data...", YELLOW));

        java.nio.file.Path file = CraftServer.dumpHeap(dir, name);
        if (file != null) {
            Command.broadcastCommandMessage(sender, text("Heap dump saved to " + file, GREEN));
        } else {
            Command.broadcastCommandMessage(sender, text("Failed to write heap dump, see server log for details", RED));
        }
    }
}
