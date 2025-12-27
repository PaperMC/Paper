package io.papermc.paper.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.Entrypoint;
import io.papermc.paper.plugin.entrypoint.LaunchEntryPointHandler;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.ProviderStatus;
import io.papermc.paper.plugin.provider.ProviderStatusHolder;
import io.papermc.paper.plugin.provider.type.paper.PaperPluginParent;
import io.papermc.paper.plugin.provider.type.spigot.SpigotPluginProvider;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperPluginsCommand {
    public static final String DESCRIPTION = "Gets a list of plugins running on the server";

    private static final TextColor INFO_COLOR = TextColor.color(52, 159, 218);

    private static final Component SERVER_PLUGIN_INFO = Component.text("ℹ What is a server plugin?", INFO_COLOR)
        .append(asPlainComponents("""
                                      Server plugins can add new behavior to your server!
                                      You can find new plugins on Paper's plugin repository, Hangar.
                                      
                                      https://hangar.papermc.io/
                                      """));

    private static final Component LEGACY_PLUGIN_INFO = Component.text("ℹ What is a legacy plugin?", INFO_COLOR)
        .append(asPlainComponents("""
                                      A legacy plugin is a plugin that was made on
                                      very old unsupported versions of the game.
                                      
                                      It is encouraged that you replace this plugin,
                                      as they might not work in the future and may cause
                                      performance issues.
                                      """));

    private static final Component LEGACY_PLUGIN_STAR = Component.text('*', TextColor.color(255, 212, 42)).hoverEvent(LEGACY_PLUGIN_INFO);
    private static final Component INFO_ICON_START = Component.text("ℹ ", INFO_COLOR);
    private static final Component PLUGIN_TICK = Component.text("- ", NamedTextColor.DARK_GRAY);
    private static final Component PLUGIN_TICK_EMPTY = Component.text(" ");

    private static final Component INFO_ICON_SERVER_PLUGIN = INFO_ICON_START.hoverEvent(SERVER_PLUGIN_INFO).clickEvent(ClickEvent.openUrl("https://docs.papermc.io/paper/adding-plugins"));

    private static final Type JAVA_PLUGIN_PROVIDER_TYPE = new TypeToken<PluginProvider<JavaPlugin>>() {}.getType();

    public static LiteralCommandNode<CommandSourceStack> create() {
        final PaperPluginsCommand command = new PaperPluginsCommand();
        return Commands.literal("plugins")
            .requires(source -> source.getSender().hasPermission("bukkit.command.plugins"))
            .executes(command::execute)
            .build();
    }

    private static <T> List<Component> formatProviders(final TreeMap<String, PluginProvider<T>> plugins) {
        final List<Component> components = new ArrayList<>(plugins.size());
        for (final PluginProvider<T> entry : plugins.values()) {
            components.add(formatProvider(entry));
        }

        boolean isFirst = true;
        final List<Component> formattedSubLists = new ArrayList<>();
        /*
        Split up the plugin list for each 10 plugins to get size down

        Plugin List:
        - Plugin 1, Plugin 2, .... Plugin 10,
          Plugin 11, Plugin 12 ... Plugin 20,
         */
        for (final List<Component> componentSublist : Lists.partition(components, 10)) {
            Component component = Component.space();
            if (isFirst) {
                component = component.append(PLUGIN_TICK);
                isFirst = false;
            } else {
                component = PLUGIN_TICK_EMPTY;
            }

            formattedSubLists.add(component.append(Component.join(JoinConfiguration.commas(true), componentSublist)));
        }

        return formattedSubLists;
    }

    private static Component formatProvider(final PluginProvider<?> provider) {
        final TextComponent.Builder builder = Component.text();
        if (provider instanceof final SpigotPluginProvider spigotPluginProvider && CraftMagicNumbers.isLegacy(spigotPluginProvider.getMeta())) {
            builder.append(LEGACY_PLUGIN_STAR);
        }

        final String name = provider.getMeta().getName();
        final Component pluginName = Component.text(name, fromStatus(provider))
            .clickEvent(ClickEvent.runCommand("/version " + name));

        builder.append(pluginName);

        return builder.build();
    }

    private static Component header(final String header, final int color, final int count, final boolean showSize) {
        final TextComponent.Builder componentHeader = Component.text().color(TextColor.color(color))
            .append(Component.text(header));

        if (showSize) {
            componentHeader.appendSpace().append(Component.text("(" + count + ")"));
        }

        return componentHeader.append(Component.text(":")).build();
    }

    private static Component asPlainComponents(final String strings) {
        final net.kyori.adventure.text.TextComponent.Builder builder = Component.text();
        for (final String string : strings.split("\n")) {
            builder.append(Component.newline());
            builder.append(Component.text(string, NamedTextColor.WHITE));
        }

        return builder.build();
    }

    private static TextColor fromStatus(final PluginProvider<?> provider) {
        if (provider instanceof final ProviderStatusHolder statusHolder && statusHolder.getLastProvidedStatus() != null) {
            final ProviderStatus status = statusHolder.getLastProvidedStatus();

            // Handle enabled/disabled game plugins
            if (status == ProviderStatus.INITIALIZED && GenericTypeReflector.isSuperType(JAVA_PLUGIN_PROVIDER_TYPE, provider.getClass())) {
                final Plugin plugin = Bukkit.getPluginManager().getPlugin(provider.getMeta().getName());
                // Plugin doesn't exist? Could be due to it being removed.
                if (plugin == null) {
                    return NamedTextColor.RED;
                }

                return plugin.isEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED;
            }

            return switch (status) {
                case INITIALIZED -> NamedTextColor.GREEN;
                case ERRORED -> NamedTextColor.RED;
            };
        } else if (provider instanceof final PaperPluginParent.PaperServerPluginProvider serverPluginProvider && serverPluginProvider.shouldSkipCreation()) {
            // Paper plugins will be skipped if their provider is skipped due to their initializer failing.
            // Show them as red
            return NamedTextColor.RED;
        } else {
            // Separated for future logic choice, but this indicated a provider that failed to load due to
            // dependency issues or what not.
            return NamedTextColor.RED;
        }
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();
        final TreeMap<String, PluginProvider<JavaPlugin>> paperPlugins = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        final TreeMap<String, PluginProvider<JavaPlugin>> spigotPlugins = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (final PluginProvider<JavaPlugin> provider : LaunchEntryPointHandler.INSTANCE.get(Entrypoint.PLUGIN).getRegisteredProviders()) {
            final PluginMeta configuration = provider.getMeta();

            if (provider instanceof SpigotPluginProvider) {
                spigotPlugins.put(configuration.getDisplayName(), provider);
            } else if (provider instanceof PaperPluginParent.PaperServerPluginProvider) {
                paperPlugins.put(configuration.getDisplayName(), provider);
            }
        }

        final int sizePaperPlugins = paperPlugins.size();
        final int sizeSpigotPlugins = spigotPlugins.size();
        final int sizePlugins = sizePaperPlugins + sizeSpigotPlugins;
        final boolean hasAllPluginTypes = (sizePaperPlugins > 0 && sizeSpigotPlugins > 0);

        final Component infoMessage = Component.text().append(INFO_ICON_SERVER_PLUGIN).append(Component.text("Server Plugins (%s):".formatted(sizePlugins), NamedTextColor.WHITE)).build();

        sender.sendMessage(infoMessage);

        if (!paperPlugins.isEmpty()) {
            sender.sendMessage(header("Paper Plugins", 0x0288D1, sizePaperPlugins, hasAllPluginTypes));
        }

        for (final Component component : formatProviders(paperPlugins)) {
            sender.sendMessage(component);
        }

        if (!spigotPlugins.isEmpty()) {
            sender.sendMessage(header("Bukkit Plugins", 0xED8106, sizeSpigotPlugins, hasAllPluginTypes));
        }

        for (final Component component : formatProviders(spigotPlugins)) {
            sender.sendMessage(component);
        }

        return Command.SINGLE_SUCCESS;
    }
}
