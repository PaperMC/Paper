package io.papermc.paper.command.subcommands;

import com.destroystokyo.paper.io.SyncLoadFinder;
import com.google.gson.JsonObject;
import com.google.gson.Strictness;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public final class SyncLoadInfoCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("syncloadinfo")
            .requires(PaperCommand.hasPermission("syncloadinfo").and($ -> SyncLoadFinder.ENABLED))
            .executes(context -> {
                return dumpSyncLoadInfo(context.getSource().getSender());
            })
            .then(Commands.literal("clear")
                .executes(context -> {
                    SyncLoadFinder.clear();
                    context.getSource().getSender().sendMessage(text("Sync load data cleared.", GOLD));
                    return Command.SINGLE_SUCCESS;
                })
            );
    }

    private static int dumpSyncLoadInfo(final CommandSender sender) {
        final Path path = Path.of("debug", "sync-load-info-" + PaperCommand.FILENAME_DATE_TIME_FORMATTER.format(LocalDateTime.now()) + ".txt");
        sender.sendMessage(
            text("Writing sync load info into", GREEN)
                .appendSpace()
                .append(PaperCommand.asFriendlyPath(path))
        );

        try {
            Files.createDirectories(path.getParent());

            final JsonObject data = SyncLoadFinder.serialize();

            final StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent(" ");
            jsonWriter.setStrictness(Strictness.STRICT);
            Streams.write(data, jsonWriter);

            try (
                final PrintStream out = new PrintStream(Files.newOutputStream(path), false, StandardCharsets.UTF_8)
            ) {
                out.print(stringWriter);
            }
            sender.sendMessage(text("Successfully written sync load information!", GREEN));
            return Command.SINGLE_SUCCESS;
        } catch (Throwable thr) {
            sender.sendMessage(text("Failed to write sync load information! See the console for more info.", RED));
            MinecraftServer.LOGGER.warn("Error occurred while dumping sync chunk load info", thr);
            return 0;
        }
    }
}
