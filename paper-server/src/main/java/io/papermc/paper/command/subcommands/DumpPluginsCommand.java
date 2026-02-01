package io.papermc.paper.command.subcommands;

import com.google.common.graph.GraphBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.entrypoint.Entrypoint;
import io.papermc.paper.plugin.entrypoint.LaunchEntryPointHandler;
import io.papermc.paper.plugin.entrypoint.classloader.group.LockingClassLoaderGroup;
import io.papermc.paper.plugin.entrypoint.classloader.group.PaperPluginClassLoaderStorage;
import io.papermc.paper.plugin.entrypoint.classloader.group.SimpleListPluginClassLoaderGroup;
import io.papermc.paper.plugin.entrypoint.classloader.group.SpigotPluginClassLoaderGroup;
import io.papermc.paper.plugin.entrypoint.classloader.group.StaticPluginClassLoaderGroup;
import io.papermc.paper.plugin.entrypoint.dependency.SimpleMetaDependencyTree;
import io.papermc.paper.plugin.entrypoint.strategy.ProviderConfiguration;
import io.papermc.paper.plugin.entrypoint.strategy.modern.ModernPluginLoadingStrategy;
import io.papermc.paper.plugin.manager.PaperPluginManagerImpl;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import io.papermc.paper.plugin.provider.classloader.PaperClassLoaderStorage;
import io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import io.papermc.paper.plugin.storage.ConfiguredProviderStorage;
import io.papermc.paper.plugin.storage.ProviderStorage;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@NullMarked
public final class DumpPluginsCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("dumpplugins")
            .requires(PaperCommand.hasPermission("dumpplugins"))
            .executes(context -> {
                return dumpPlugins(context.getSource().getSender());
            });
    }

    private static int dumpPlugins(final CommandSender sender) {
        Path path = Path.of("debug", "plugin-info-" + PaperCommand.FILENAME_DATE_TIME_FORMATTER.format(LocalDateTime.now()) + ".json");
        try {
            Files.createDirectories(path.getParent());
            sender.sendMessage(
                text("Writing plugin debug information into", GREEN)
                    .appendSpace()
                    .append(PaperCommand.asFriendlyPath(path))
            );

            final JsonObject data = writeDebug();

            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent(" ");
            jsonWriter.setStrictness(Strictness.STRICT);
            Streams.write(data, jsonWriter);

            try (PrintStream out = new PrintStream(Files.newOutputStream(path), false, StandardCharsets.UTF_8)) {
                out.print(stringWriter);
            }
            sender.sendMessage(text("Successfully written plugin debug information", GREEN));
            return Command.SINGLE_SUCCESS;
        } catch (Throwable thr) {
            sender.sendMessage(text("Failed to write plugin information! See the console for more info.", RED));
            MinecraftServer.LOGGER.warn("Error occurred while dumping plugin info", thr);
            return 0;
        }
    }

    private static JsonObject writeDebug() {
        JsonObject root = new JsonObject();
        if (ConfiguredProviderStorage.LEGACY_PLUGIN_LOADING) {
            root.addProperty("legacy-loading-strategy", true);
        }

        writeProviders(root);
        writePlugins(root);
        writeClassloaders(root);

        return root;
    }

    @SuppressWarnings("unchecked")
    private static void writeProviders(JsonObject root) {
        JsonObject rootProviders = new JsonObject();
        root.add("providers", rootProviders);

        for (Map.Entry<Entrypoint<?>, ProviderStorage<?>> entry : LaunchEntryPointHandler.INSTANCE.getStorage().entrySet()) {
            JsonObject entrypoint = new JsonObject();

            JsonArray providers = new JsonArray();
            entrypoint.add("providers", providers);

            List<PluginProvider<Object>> pluginProviders = new ArrayList<>();
            for (PluginProvider<?> provider : entry.getValue().getRegisteredProviders()) {
                JsonObject providerObj = new JsonObject();
                providerObj.addProperty("name", provider.getMeta().getName());
                providerObj.addProperty("version", provider.getMeta().getVersion());
                providerObj.addProperty("dependencies", provider.getMeta().getPluginDependencies().toString());
                providerObj.addProperty("soft-dependencies", provider.getMeta().getPluginSoftDependencies().toString());
                providerObj.addProperty("load-before", provider.getMeta().getLoadBeforePlugins().toString());

                providers.add(providerObj);
                pluginProviders.add((PluginProvider<Object>) provider);
            }

            JsonArray loadOrder = new JsonArray();
            entrypoint.add("load-order", loadOrder);

            ModernPluginLoadingStrategy<Object> modernPluginLoadingStrategy = new ModernPluginLoadingStrategy<>(new ProviderConfiguration<>() {
                @Override
                public void applyContext(PluginProvider<Object> provider, DependencyContext dependencyContext) {
                }

                @Override
                public boolean load(PluginProvider<Object> provider, Object provided) {
                    return true;
                }

                @Override
                public boolean preloadProvider(PluginProvider<Object> provider) {
                    // Don't load provider
                    loadOrder.add(provider.getMeta().getName());
                    return false;
                }
            });
            modernPluginLoadingStrategy.loadProviders(pluginProviders, new SimpleMetaDependencyTree(GraphBuilder.directed().build()));

            rootProviders.add(entry.getKey().getDebugName(), entrypoint);
        }
    }

    private static void writePlugins(JsonObject root) {
        JsonArray rootPlugins = new JsonArray();
        root.add("plugins", rootPlugins);

        for (Plugin plugin : PaperPluginManagerImpl.getInstance().getPlugins()) {
            rootPlugins.add(plugin.toString());
        }
    }

    private static void writeClassloaders(JsonObject root) {
        JsonObject classLoadersRoot = new JsonObject();
        root.add("classloaders", classLoadersRoot);

        PaperPluginClassLoaderStorage storage = (PaperPluginClassLoaderStorage) PaperClassLoaderStorage.instance();
        classLoadersRoot.addProperty("global", storage.getGlobalGroup().toString());
        classLoadersRoot.addProperty("dependency_graph", PaperPluginManagerImpl.getInstance().getInstanceManagerGraph().toString());

        JsonArray array = new JsonArray();
        classLoadersRoot.add("children", array);
        for (PluginClassLoaderGroup group : storage.getGroups()) {
            array.add(writeClassloader(group));
        }
    }

    private static JsonObject writeClassloader(PluginClassLoaderGroup group) {
        JsonObject classLoadersRoot = new JsonObject();
        if (group instanceof SimpleListPluginClassLoaderGroup listGroup) {
            JsonArray array = new JsonArray();
            classLoadersRoot.addProperty("main", listGroup.toString());
            if (group instanceof StaticPluginClassLoaderGroup staticPluginClassLoaderGroup) {
                classLoadersRoot.addProperty("plugin-holder", staticPluginClassLoaderGroup.getPluginClassloader().toString());
            } else if (group instanceof SpigotPluginClassLoaderGroup spigotPluginClassLoaderGroup) {
                classLoadersRoot.addProperty("plugin-holder", spigotPluginClassLoaderGroup.getPluginClassLoader().toString());
            }

            classLoadersRoot.add("children", array);
            for (ConfiguredPluginClassLoader innerGroup : listGroup.getClassLoaders()) {
                array.add(writeClassloader(innerGroup));
            }

        } else if (group instanceof LockingClassLoaderGroup locking) {
            // Unwrap
            return writeClassloader(locking.getParent());
        } else {
            classLoadersRoot.addProperty("raw", group.toString());
        }

        return classLoadersRoot;
    }

    private static JsonElement writeClassloader(ConfiguredPluginClassLoader innerGroup) {
        return new JsonPrimitive(innerGroup.toString());
    }
}
