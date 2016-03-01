package io.papermc.paper.command.subcommands;

import io.papermc.paper.command.PaperSubcommand;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@DefaultQualifier(NonNull.class)
public final class HeapDumpCommand implements PaperSubcommand {
    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        this.dumpHeap(sender);
        return true;
    }

    private void dumpHeap(final CommandSender sender) {
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
