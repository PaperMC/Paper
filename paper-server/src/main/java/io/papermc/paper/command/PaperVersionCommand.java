package io.papermc.paper.command;

import com.destroystokyo.paper.PaperVersionFetcher;
import com.destroystokyo.paper.util.VersionFetcher;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperVersionCommand {
    public static final String DESCRIPTION = "Gets the version of this server including any plugins in use";

    private static final Component NOT_RUNNING = Component.text()
        .append(Component.text("This server is not running any plugin by that name."))
        .appendNewline()
        .append(Component.text("Use /plugins to get a list of plugins.").clickEvent(ClickEvent.suggestCommand("/plugins")))
        .build();
    private static final JoinConfiguration PLAYER_JOIN_CONFIGURATION = JoinConfiguration.separators(
        Component.text(", ", NamedTextColor.WHITE),
        Component.text(", and ", NamedTextColor.WHITE)
    );
    private static final Component FAILED_TO_FETCH = Component.text("Could not fetch version information!", NamedTextColor.RED);
    private static final Component FETCHING = Component.text("Checking version, please wait...", NamedTextColor.WHITE, TextDecoration.ITALIC);

    private final VersionFetcher versionFetcher = new PaperVersionFetcher();
    private CompletableFuture<ComputedVersion> computedVersion = CompletableFuture.completedFuture(new ComputedVersion(Component.empty(), -1)); // Precompute-- someday move that stuff out of bukkit

    public static LiteralCommandNode<CommandSourceStack> create() {
        final PaperVersionCommand command = new PaperVersionCommand();

        return Commands.literal("version")
            .requires(source -> source.getSender().hasPermission("bukkit.command.version"))
            .then(Commands.argument("plugin", StringArgumentType.word())
                .suggests(command::suggestPlugins)
                .executes(command::pluginVersion))
            .executes(command::serverVersion)
            .build();
    }

    private int pluginVersion(final CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();
        final String pluginName = context.getArgument("plugin", String.class).toLowerCase(Locale.ROOT);

        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin == null) {
            plugin = Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(checkPlugin -> checkPlugin.getName().toLowerCase(Locale.ROOT).contains(pluginName))
                .findAny()
                .orElse(null);
        }

        if (plugin != null) {
            this.sendPluginInfo(plugin, sender);
        } else {
            sender.sendMessage(NOT_RUNNING);
        }

        return Command.SINGLE_SUCCESS;
    }

    private CompletableFuture<Suggestions> suggestPlugins(final CommandContext<CommandSourceStack> context, final SuggestionsBuilder builder) {
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            final String name = plugin.getName();
            if (StringUtil.startsWithIgnoreCase(name, builder.getRemainingLowerCase())) {
                builder.suggest(name);
            }
        }

        return CompletableFuture.completedFuture(builder.build());
    }

    private void sendPluginInfo(final Plugin plugin, final CommandSender sender) {
        final PluginMeta meta = plugin.getPluginMeta();

        final TextComponent.Builder builder = Component.text()
            .append(Component.text(meta.getName()))
            .append(Component.text(" version "))
            .append(Component.text(meta.getVersion(), NamedTextColor.GREEN)
                .hoverEvent(Component.translatable("chat.copy.click"))
                .clickEvent(ClickEvent.copyToClipboard(meta.getVersion()))
            );

        if (meta.getDescription() != null) {
            builder
                .appendNewline()
                .append(Component.text(meta.getDescription()));
        }

        if (meta.getWebsite() != null) {
            Component websiteComponent = Component.text(meta.getWebsite(), NamedTextColor.GREEN).clickEvent(ClickEvent.openUrl(meta.getWebsite()));
            builder.appendNewline().append(Component.text("Website: ").append(websiteComponent));
        }

        if (!meta.getAuthors().isEmpty()) {
            String prefix = meta.getAuthors().size() == 1 ? "Author: " : "Authors: ";
            builder.appendNewline().append(Component.text(prefix).append(formatNameList(meta.getAuthors())));
        }

        if (!meta.getContributors().isEmpty()) {
            builder.appendNewline().append(Component.text("Contributors: ").append(formatNameList(meta.getContributors())));
        }
        sender.sendMessage(builder.build());
    }

    private static Component formatNameList(final List<String> names) {
        return Component.join(PLAYER_JOIN_CONFIGURATION, names.stream().map(Component::text).toList()).color(NamedTextColor.GREEN);
    }

    private int serverVersion(CommandContext<CommandSourceStack> context) {
        sendVersion(context.getSource().getSender());
        return Command.SINGLE_SUCCESS;
    }

    private void sendVersion(final CommandSender sender) {
        final CompletableFuture<ComputedVersion> version = getVersionOrFetch();
        if (!version.isDone()) {
            sender.sendMessage(FETCHING);
        }

        version.whenComplete((computedVersion, throwable) -> {
            if (computedVersion != null) {
                sender.sendMessage(computedVersion.message);
            } else if (throwable != null) {
                sender.sendMessage(FAILED_TO_FETCH);
                MinecraftServer.LOGGER.warn("Could not fetch version information!", throwable);
            }
        });
    }

    private CompletableFuture<ComputedVersion> getVersionOrFetch() {
        if (!this.computedVersion.isDone()) {
            return this.computedVersion;
        }

        if (this.computedVersion.isCompletedExceptionally() || System.currentTimeMillis() - this.computedVersion.resultNow().computedTime() > this.versionFetcher.getCacheTime()) {
            this.computedVersion = this.fetchVersionMessage();
        }

        return this.computedVersion;
    }

    private CompletableFuture<ComputedVersion> fetchVersionMessage() {
       return CompletableFuture.supplyAsync(() -> {
           final Component message = Component.textOfChildren(
               Component.text(Bukkit.getVersionMessage(), NamedTextColor.WHITE),
               Component.newline(),
               this.versionFetcher.getVersionMessage()
           );

           return new ComputedVersion(
               message.hoverEvent(Component.translatable("chat.copy.click", NamedTextColor.WHITE))
                   .clickEvent(ClickEvent.copyToClipboard(PlainTextComponentSerializer.plainText().serialize(message))),
               System.currentTimeMillis()
           );
       });
    }

    record ComputedVersion(Component message, long computedTime) {

    }
}
