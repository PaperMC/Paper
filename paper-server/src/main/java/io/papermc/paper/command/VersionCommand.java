package io.papermc.paper.command;

import com.destroystokyo.paper.PaperVersionFetcher;
import com.destroystokyo.paper.util.VersionFetcher;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class VersionCommand {
    private static final VersionFetcher versionFetcher = new PaperVersionFetcher();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // todo: what about description? how are we doing this?
        //  "Gets the version of this server including any plugins in use"
        dispatcher.register(create("version"));
        dispatcher.register(create("about"));
        dispatcher.register(create("ver"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> create(String name) {
        return Commands.literal(name)
            .requires(source -> source.getBukkitSender().hasPermission("bukkit.command.version"))
            .then(Commands.argument("plugin", StringArgumentType.word())
                .suggests(VersionCommand::suggestPlugins)
                .executes(VersionCommand::pluginVersion))
            .executes(VersionCommand::serverVersion);
    }

    private static CompletableFuture<Suggestions> suggestPlugins(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return CompletableFuture.runAsync(() -> Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .map(Plugin::getName)
                .filter(name -> StringUtil.startsWithIgnoreCase(name, builder.getRemainingLowerCase()))
                .forEach(builder::suggest))
            .thenApply(ignored -> builder.build());
    }

    private static int pluginVersion(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getBukkitSender();
        String pluginName = context.getArgument("plugin", String.class);
        Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginName))
            .or(() -> Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(plugin -> plugin.getName().toLowerCase(Locale.ROOT).contains(pluginName))
                .findAny())
            .ifPresentOrElse(plugin -> describeToSender(plugin, sender), () -> {
                sender.sendMessage(Component.text("This server is not running any plugin by that name."));
                sender.sendMessage(Component.text("Use /plugins to get a list of plugins.")
                    .clickEvent(ClickEvent.suggestCommand("/plugins")));
            });
        return Command.SINGLE_SUCCESS;
    }

    private static void describeToSender(Plugin plugin, CommandSender sender) {
        PluginMeta meta = plugin.getPluginMeta();

        sender.sendMessage(Component.text(meta.getName(), NamedTextColor.GREEN)
            .append(Component.text(" version ", NamedTextColor.WHITE))
            .append(Component.text(meta.getVersion(), NamedTextColor.GREEN)
                .hoverEvent(Component.translatable("chat.copy.click"))
                .clickEvent(ClickEvent.copyToClipboard(meta.getVersion()))
            ));
        if (meta.getDescription() != null) {
            sender.sendMessage(Component.text(meta.getDescription()));
        }

        if (meta.getWebsite() != null) {
            sender.sendMessage(Component.text("Website: ")
                .append(Component.text(meta.getWebsite(), NamedTextColor.GREEN)
                    .clickEvent(ClickEvent.openUrl(meta.getWebsite()))));
        }

        if (!meta.getAuthors().isEmpty()) {
            String prefix = meta.getAuthors().size() == 1 ? "Author: " : "Authors: ";
            sender.sendMessage(Component.text(prefix).append(getNameList(meta.getAuthors())));
        }

        if (!meta.getContributors().isEmpty()) {
            sender.sendMessage(Component.text("Contributors: ").append(getNameList(meta.getContributors())));
        }
    }

    private static Component getNameList(List<String> names) {
        JoinConfiguration configuration = JoinConfiguration.separators(
            Component.text(", ", NamedTextColor.WHITE),
            Component.text(" and ", NamedTextColor.WHITE)
        );
        return Component.join(configuration,
            names.stream().map(Component::text).toList()
        ).color(NamedTextColor.GREEN);
    }


    private static int serverVersion(CommandContext<CommandSourceStack> context) {
        sendVersion(context.getSource().getBukkitSender());
        return Command.SINGLE_SUCCESS;
    }

    private static final ReentrantLock versionLock = new ReentrantLock();
    private static @Nullable Component versionMessage = null;
    private static final Set<CommandSender> versionWaiters = new HashSet<>();
    private static boolean versionTaskStarted = false;
    private static long lastCheck = 0;

        if (versionMessage != null) {
            if (System.currentTimeMillis() - lastCheck > versionFetcher.getCacheTime()) {
                lastCheck = System.currentTimeMillis();
                versionMessage = null;
            } else {
                sender.sendMessage(versionMessage);
                return;
            }
        }
        versionLock.lock();
        try {
            versionWaiters.add(sender);
            sender.sendMessage(Component.text("Checking version, please wait...", NamedTextColor.WHITE, TextDecoration.ITALIC));
            if (!versionTaskStarted) {
                versionTaskStarted = true;
                new Thread(VersionCommand::obtainVersion).start();
            }
        } finally {
            versionLock.unlock();
        }
    }


    private static void obtainVersion() {
        setVersionMessage(versionFetcher.getVersionMessage(Bukkit.getVersion()));
    }

    private static void setVersionMessage(final @NotNull Component msg) {
        lastCheck = System.currentTimeMillis();
        final Component message = Component.textOfChildren(
            Component.text(Bukkit.getVersionMessage(), NamedTextColor.WHITE),
            Component.newline(),
            msg
        );
        versionMessage = Component.text()
            .append(message)
            .hoverEvent(Component.text("Click to copy to clipboard", NamedTextColor.WHITE))
            .clickEvent(ClickEvent.copyToClipboard(PlainTextComponentSerializer.plainText().serialize(message)))
            .build();

        versionLock.lock();
        try {
            versionTaskStarted = false;
            for (CommandSender sender : versionWaiters) {
                sender.sendMessage(versionMessage);
            }
            versionWaiters.clear();
        } finally {
            versionLock.unlock();
        }
    }
}
