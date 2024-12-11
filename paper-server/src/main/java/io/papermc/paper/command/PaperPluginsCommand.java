package io.papermc.paper.command;

import com.google.common.collect.Lists;
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.Entrypoint;
import io.papermc.paper.plugin.entrypoint.LaunchEntryPointHandler;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.ProviderStatus;
import io.papermc.paper.plugin.provider.ProviderStatusHolder;
import io.papermc.paper.plugin.provider.type.paper.PaperPluginParent;
import io.papermc.paper.plugin.provider.type.spigot.SpigotPluginProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class PaperPluginsCommand extends BukkitCommand {

    private static final TextColor INFO_COLOR = TextColor.color(52, 159, 218);

    // TODO: LINK?
    private static final Component SERVER_PLUGIN_INFO = Component.text("ℹ What is a server plugin?", INFO_COLOR)
        .append(asPlainComponents("""
            Server plugins can add new behavior to your server!
            You can find new plugins on Paper's plugin repository, Hangar.
                        
            <link to hangar>
            """));

    private static final Component SERVER_INITIALIZER_INFO = Component.text("ℹ What is a server initializer?", INFO_COLOR)
        .append(asPlainComponents("""
            Server initializers are ran before your server
            starts and are provided by paper plugins.
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
    private static final Component PAPER_HEADER = Component.text("Paper Plugins:", TextColor.color(2, 136, 209));
    private static final Component BUKKIT_HEADER = Component.text("Bukkit Plugins:", TextColor.color(237, 129, 6));
    private static final Component PLUGIN_TICK = Component.text("- ", NamedTextColor.DARK_GRAY);
    private static final Component PLUGIN_TICK_EMPTY = Component.text(" ");

    private static final Type JAVA_PLUGIN_PROVIDER_TYPE = new TypeToken<PluginProvider<JavaPlugin>>() {}.getType();

    public PaperPluginsCommand() {
        super("plugins");
        this.description = "Gets a list of plugins running on the server";
        this.usageMessage = "/plugins";
        this.setPermission("bukkit.command.plugins");
        this.setAliases(Arrays.asList("pl"));
    }

    private static <T> List<Component> formatProviders(TreeMap<String, PluginProvider<T>> plugins) {
        List<Component> components = new ArrayList<>(plugins.size());
        for (PluginProvider<T> entry : plugins.values()) {
            components.add(formatProvider(entry));
        }

        boolean isFirst = true;
        List<Component> formattedSublists = new ArrayList<>();
        /*
        Split up the plugin list for each 10 plugins to get size down

        Plugin List:
        - Plugin 1, Plugin 2, .... Plugin 10,
          Plugin 11, Plugin 12 ... Plugin 20,
         */
        for (List<Component> componentSublist : Lists.partition(components, 10)) {
            Component component = Component.space();
            if (isFirst) {
                component = component.append(PLUGIN_TICK);
                isFirst = false;
            } else {
                component = PLUGIN_TICK_EMPTY;
                //formattedSublists.add(Component.empty()); // Add an empty line, the auto chat wrapping and this makes it quite jarring.
            }

            formattedSublists.add(component.append(Component.join(JoinConfiguration.commas(true), componentSublist)));
        }

        return formattedSublists;
    }

    private static Component formatProvider(PluginProvider<?> provider) {
        TextComponent.Builder builder = Component.text();
        if (provider instanceof SpigotPluginProvider spigotPluginProvider && CraftMagicNumbers.isLegacy(spigotPluginProvider.getMeta())) {
            builder.append(LEGACY_PLUGIN_STAR);
        }

        String name = provider.getMeta().getName();
        Component pluginName = Component.text(name, fromStatus(provider))
            .clickEvent(ClickEvent.runCommand("/version " + name));

        builder.append(pluginName);

        return builder.build();
    }

    private static Component asPlainComponents(String strings) {
        net.kyori.adventure.text.TextComponent.Builder builder = Component.text();
        for (String string : strings.split("\n")) {
            builder.append(Component.newline());
            builder.append(Component.text(string, NamedTextColor.WHITE));
        }

        return builder.build();
    }

    private static TextColor fromStatus(PluginProvider<?> provider) {
        if (provider instanceof ProviderStatusHolder statusHolder && statusHolder.getLastProvidedStatus() != null) {
            ProviderStatus status = statusHolder.getLastProvidedStatus();

            // Handle enabled/disabled game plugins
            if (status == ProviderStatus.INITIALIZED && GenericTypeReflector.isSuperType(JAVA_PLUGIN_PROVIDER_TYPE, provider.getClass())) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(provider.getMeta().getName());
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
        } else if (provider instanceof PaperPluginParent.PaperServerPluginProvider serverPluginProvider && serverPluginProvider.shouldSkipCreation()) {
            // Paper plugins will be skipped if their provider is skipped due to their initializer failing.
            // Show them as red
            return NamedTextColor.RED;
        } else {
            // Separated for future logic choice, but this indicated a provider that failed to load due to
            // dependency issues or what not.
            return NamedTextColor.RED;
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;

        TreeMap<String, PluginProvider<JavaPlugin>> paperPlugins = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, PluginProvider<JavaPlugin>> spigotPlugins = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


        for (PluginProvider<JavaPlugin> provider : LaunchEntryPointHandler.INSTANCE.get(Entrypoint.PLUGIN).getRegisteredProviders()) {
            PluginMeta configuration = provider.getMeta();

            if (provider instanceof SpigotPluginProvider) {
                spigotPlugins.put(configuration.getDisplayName(), provider);
            } else if (provider instanceof PaperPluginParent.PaperServerPluginProvider) {
                paperPlugins.put(configuration.getDisplayName(), provider);
            }
        }

        Component infoMessage = Component.text("Server Plugins (%s):".formatted(paperPlugins.size() + spigotPlugins.size()), NamedTextColor.WHITE);
            //.append(INFO_ICON_START.hoverEvent(SERVER_PLUGIN_INFO)); TODO: Add docs

        sender.sendMessage(infoMessage);

        if (!paperPlugins.isEmpty()) {
            sender.sendMessage(PAPER_HEADER);
        }

        for (Component component : formatProviders(paperPlugins)) {
            sender.sendMessage(component);
        }

        if (!spigotPlugins.isEmpty()) {
            sender.sendMessage(BUKKIT_HEADER);
        }
        
        for (Component component : formatProviders(spigotPlugins)) {
            sender.sendMessage(component);
        }

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }

}
