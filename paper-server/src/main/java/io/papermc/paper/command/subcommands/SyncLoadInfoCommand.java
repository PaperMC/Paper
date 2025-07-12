package io.papermc.paper.command.subcommands;

import com.destroystokyo.paper.io.SyncLoadFinder;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

@NullMarked
public final class SyncLoadInfoCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("syncloadinfo")
            .requires(stack -> SyncLoadFinder.ENABLED && stack.getSender().hasPermission(PaperCommand.BASE_PERM + "syncloadinfo"))
            .then(Commands.literal("clear")
                .executes(ctx -> {
                    doSyncLoadInfo(ctx.getSource().getSender(), true);
                    return Command.SINGLE_SUCCESS;
                })
            )
            .executes(ctx -> {
                doSyncLoadInfo(ctx.getSource().getSender(), false);
                return Command.SINGLE_SUCCESS;
            });
    }

    private static void doSyncLoadInfo(final CommandSender sender, final boolean clear) {
        // This if-statement is technically not needed anymore, but in case somebody decided that it would
        // be a wonderful idea to call this method using reflection, we keep it in.
        if (!SyncLoadFinder.ENABLED) {
            String systemFlag = "-Dpaper.debug-sync-loads=true";
            sender.sendMessage(text().color(RED).append(text("This command requires the server startup flag '")).append(
                text(systemFlag, WHITE).clickEvent(ClickEvent.copyToClipboard(systemFlag))
                    .hoverEvent(HoverEvent.showText(text("Click to copy the system flag")))).append(
                text("' to be set.")));
            return;
        }

        if (clear) {
            SyncLoadFinder.clear();
            sender.sendMessage(text("Sync load data cleared.", GRAY));
            return;
        }

        File file = new File(new File(new File("."), "debug"),
            "sync-load-info-" + FORMATTER.format(LocalDateTime.now()) + ".txt");
        file.getParentFile().mkdirs();
        sender.sendMessage(text("Writing sync load info to " + file, GREEN));


        try {
            final JsonObject data = SyncLoadFinder.serialize();

            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent(" ");
            jsonWriter.setLenient(false);
            Streams.write(data, jsonWriter);

            try (
                PrintStream out = new PrintStream(new FileOutputStream(file), false, StandardCharsets.UTF_8)
            ) {
                out.print(stringWriter);
            }
            sender.sendMessage(text("Successfully written sync load information!", GREEN));
        } catch (Throwable thr) {
            sender.sendMessage(text("Failed to write sync load information! See the console for more info.", RED));
            MinecraftServer.LOGGER.warn("Error occurred while dumping sync chunk load info", thr);
        }
    }
}
