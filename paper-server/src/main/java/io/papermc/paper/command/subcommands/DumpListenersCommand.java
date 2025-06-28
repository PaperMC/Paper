
package io.papermc.paper.command.subcommands;

import com.destroystokyo.paper.util.SneakyThrow;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

@NullMarked
public final class DumpListenersCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");
    private static final String COMMAND_ARGUMENT_TO_FILE = "tofile";
    private static final MethodHandle EVENT_TYPES_HANDLE;

    private static final Component HELP_MESSAGE = text("Usage: /paper dumplisteners " + COMMAND_ARGUMENT_TO_FILE + "|<className>", RED);
    private static final SuggestionProvider<CommandSourceStack> EVENT_SUGGESTIONS = (ctx, builder) -> CompletableFuture.supplyAsync(() -> {
        eventClassNames().stream()
            .filter(name -> name.toLowerCase().startsWith(builder.getRemainingLowerCase()))
            .forEach(builder::suggest);
        return builder.build();
    });

    static {
        try {
            final Field eventTypesField = HandlerList.class.getDeclaredField("EVENT_TYPES");
            eventTypesField.setAccessible(true);
            EVENT_TYPES_HANDLE = MethodHandles.lookup().unreflectGetter(eventTypesField);
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("dumplisteners")
            .requires(stack -> stack.getSender().hasPermission(PaperCommand.BASE_PERM + "dumplisteners"))

            .then(Commands.literal("tofile")
                .executes(ctx -> {
                    dumpToFile(ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
            )

            .then(Commands.argument("event", StringArgumentType.word())
                .suggests(EVENT_SUGGESTIONS)
                .executes(ctx -> {
                    doDumpListeners(ctx.getSource().getSender(), StringArgumentType.getString(ctx, "event"));
                    return Command.SINGLE_SUCCESS;
                })
            )

            .executes(ctx -> {
                ctx.getSource().getSender().sendMessage(HELP_MESSAGE);
                return Command.SINGLE_SUCCESS;
            });
    }

    private static void dumpToFile(final CommandSender sender) {
        Path parent = Path.of("debug");
        Path path = parent.resolve("listeners-" + FORMATTER.format(LocalDateTime.now()) + ".txt");
        sender.sendMessage(
            text("Writing listeners into directory", GREEN)
                .appendSpace()
                .append(
                    text(parent.toString(), WHITE)
                        .hoverEvent(text("Click to copy the full path of debug directory", WHITE))
                        .clickEvent(ClickEvent.copyToClipboard(parent.toAbsolutePath().toString()))
                )
        );
        try {
            Files.createDirectories(parent);
            Files.createFile(path);
            try (final PrintWriter writer = new PrintWriter(path.toFile())) {
                for (final String eventClass : eventClassNames()) {
                    final HandlerList handlers;
                    try {
                        handlers = (HandlerList) findClass(eventClass).getMethod("getHandlerList").invoke(null);
                    } catch (final ReflectiveOperationException e) {
                        continue;
                    }
                    if (handlers.getRegisteredListeners().length != 0) {
                        writer.println(eventClass);
                    }
                    for (final RegisteredListener registeredListener : handlers.getRegisteredListeners()) {
                        writer.println(" - " + registeredListener);
                    }
                }
            }
        } catch (final IOException ex) {
            sender.sendMessage(text("Failed to write dumped listener! See the console for more info.", RED));
            MinecraftServer.LOGGER.warn("Error occurred while dumping listeners", ex);
            return;
        }
        sender.sendMessage(
            text("Successfully written listeners into", GREEN)
                .appendSpace()
                .append(
                    text(path.toString(), WHITE)
                        .hoverEvent(text("Click to copy the full path of the file", WHITE))
                        .clickEvent(ClickEvent.copyToClipboard(path.toAbsolutePath().toString()))
                )
        );
    }

    private static void doDumpListeners(final CommandSender sender, final String className) {
        try {
            final HandlerList handlers = (HandlerList) findClass(className).getMethod("getHandlerList").invoke(null);

            if (handlers.getRegisteredListeners().length == 0) {
                sender.sendMessage(text(className + " does not have any registered listeners."));
                return;
            }

            sender.sendMessage(text("Listeners for " + className + ":"));

            for (final RegisteredListener listener : handlers.getRegisteredListeners()) {
                final Component hoverText = text("Priority: " + listener.getPriority().name() + " (" + listener.getPriority().getSlot() + ")", WHITE)
                    .append(newline())
                    .append(text("Listener: " + listener.getListener()))
                    .append(newline())
                    .append(text("Executor: " + listener.getExecutor()))
                    .append(newline())
                    .append(text("Ignoring cancelled: " + listener.isIgnoringCancelled()));

                sender.sendMessage(text(listener.getPlugin().getName(), GREEN)
                    .append(space())
                    .append(text("(" + listener.getListener().getClass().getName() + ")", GRAY).hoverEvent(hoverText)));
            }

            sender.sendMessage(text("Total listeners: " + handlers.getRegisteredListeners().length));

        } catch (final ClassNotFoundException e) {
            sender.sendMessage(text("Unable to find a class named '" + className + "'. Make sure to use the fully qualified name.", RED));
        } catch (final NoSuchMethodException e) {
            sender.sendMessage(text("Class '" + className + "' does not have a valid getHandlerList method.", RED));
        } catch (final ReflectiveOperationException e) {
            sender.sendMessage(text("Something went wrong, see the console for more details.", RED));
            MinecraftServer.LOGGER.warn("Error occurred while dumping listeners for class {}", className, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<String> eventClassNames() {
        try {
            return (Set<String>) EVENT_TYPES_HANDLE.invokeExact();
        } catch (final Throwable e) {
            SneakyThrow.sneaky(e);
            return Collections.emptySet(); // Unreachable
        }
    }

    private static Class<?> findClass(final String className) throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException ignore) {
            for (final Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
                if (!plugin.isEnabled()) {
                    continue;
                }

                try {
                    return Class.forName(className, false, plugin.getClass().getClassLoader());
                } catch (final ClassNotFoundException ignore0) {
                }
            }
        }
        throw new ClassNotFoundException(className);
    }
}
