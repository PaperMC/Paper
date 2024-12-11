package io.papermc.paper.plugin;

import com.mojang.logging.LogUtils;
import io.papermc.paper.configuration.PaperConfigurations;
import io.papermc.paper.plugin.entrypoint.Entrypoint;
import io.papermc.paper.plugin.entrypoint.LaunchEntryPointHandler;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.type.paper.PaperPluginParent;
import io.papermc.paper.plugin.provider.type.spigot.SpigotPluginProvider;
import io.papermc.paper.pluginremap.PluginRemapper;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import joptsimple.OptionSet;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.LibraryLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PluginInitializerManager {

    private static final Logger LOGGER = LogUtils.getClassLogger();
    private static PluginInitializerManager impl;
    private final Path pluginDirectory;
    private final Path updateDirectory;
    public final io.papermc.paper.pluginremap.@org.checkerframework.checker.nullness.qual.MonotonicNonNull PluginRemapper pluginRemapper; // Paper

    PluginInitializerManager(final Path pluginDirectory, final Path updateDirectory) {
        this.pluginDirectory = pluginDirectory;
        this.updateDirectory = updateDirectory;
        this.pluginRemapper = Boolean.getBoolean("paper.disablePluginRemapping")
            ? null
            : PluginRemapper.create(pluginDirectory);
        LibraryLoader.REMAPPER = this.pluginRemapper == null ? Function.identity() : this.pluginRemapper::remapLibraries;
    }

    private static PluginInitializerManager parse(@NotNull final OptionSet minecraftOptionSet) throws Exception {
        // We have to load the bukkit configuration inorder to get the update folder location.
        final File configFileLocationBukkit = (File) minecraftOptionSet.valueOf("bukkit-settings");

        final Path pluginDirectory = ((File) minecraftOptionSet.valueOf("plugins")).toPath();

        final YamlConfiguration configuration = PaperConfigurations.loadLegacyConfigFile(configFileLocationBukkit);

        final String updateDirectoryName = configuration.getString("settings.update-folder", "update");
        if (updateDirectoryName.isBlank()) {
            return new PluginInitializerManager(pluginDirectory, null);
        }

        final Path resolvedUpdateDirectory = pluginDirectory.resolve(updateDirectoryName);
        if (!Files.isDirectory(resolvedUpdateDirectory)) {
            if (Files.exists(resolvedUpdateDirectory)) {
                LOGGER.error("Misconfigured update directory!");
                LOGGER.error("Your configured update directory ({}) in bukkit.yml is pointing to a non-directory path. " +
                    "Auto updating functionality will not work.", resolvedUpdateDirectory);
            }
            return new PluginInitializerManager(pluginDirectory, null);
        }

        boolean isSameFile;
        try {
            isSameFile = Files.isSameFile(resolvedUpdateDirectory, pluginDirectory);
        } catch (final IOException e) {
            LOGGER.error("Misconfigured update directory!");
            LOGGER.error("Failed to compare update/plugin directory", e);
            return new PluginInitializerManager(pluginDirectory, null);
        }

        if (isSameFile) {
            LOGGER.error("Misconfigured update directory!");
            LOGGER.error(("Your configured update directory (%s) in bukkit.yml is pointing to the same location as the plugin directory (%s). " +
                "Disabling auto updating functionality.").formatted(resolvedUpdateDirectory, pluginDirectory));

            return new PluginInitializerManager(pluginDirectory, null);
        }

        return new PluginInitializerManager(pluginDirectory, resolvedUpdateDirectory);
    }

    public static PluginInitializerManager init(final OptionSet optionSet) throws Exception {
        impl = parse(optionSet);
        return impl;
    }

    public static PluginInitializerManager instance() {
        return impl;
    }

    @NotNull
    public Path pluginDirectoryPath() {
        return pluginDirectory;
    }

    @Nullable
    public Path pluginUpdatePath() {
        return updateDirectory;
    }

    public static void load(OptionSet optionSet) throws Exception {
        LOGGER.info("Initializing plugins...");
        // We have to load the bukkit configuration inorder to get the update folder location.
        io.papermc.paper.plugin.PluginInitializerManager pluginSystem = io.papermc.paper.plugin.PluginInitializerManager.init(optionSet);
        if (pluginSystem.pluginRemapper != null) pluginSystem.pluginRemapper.loadingPlugins();

        // Register the default plugin directory
        io.papermc.paper.plugin.util.EntrypointUtil.registerProvidersFromSource(io.papermc.paper.plugin.provider.source.DirectoryProviderSource.INSTANCE, pluginSystem.pluginDirectoryPath());

        // Register plugins from the flag
        @SuppressWarnings("unchecked")
        java.util.List<Path> files = ((java.util.List<File>) optionSet.valuesOf("add-plugin")).stream().map(File::toPath).toList();
        io.papermc.paper.plugin.util.EntrypointUtil.registerProvidersFromSource(io.papermc.paper.plugin.provider.source.PluginFlagProviderSource.INSTANCE, files);

        final Set<String> paperPluginNames = new TreeSet<>();
        final Set<String> legacyPluginNames = new TreeSet<>();
        LaunchEntryPointHandler.INSTANCE.getStorage().forEach((entrypoint, providerStorage) -> {
            providerStorage.getRegisteredProviders().forEach(provider -> {
                if (provider instanceof final SpigotPluginProvider legacy) {
                    legacyPluginNames.add(String.format("%s (%s)", legacy.getMeta().getName(), legacy.getMeta().getVersion()));
                } else if (provider instanceof final PaperPluginParent.PaperServerPluginProvider paper) {
                    paperPluginNames.add(String.format("%s (%s)", provider.getMeta().getName(), provider.getMeta().getVersion()));
                }
            });
        });
        final int total = paperPluginNames.size() + legacyPluginNames.size();
        LOGGER.info("Initialized {} plugin{}", total, total == 1 ? "" : "s");
        if (!paperPluginNames.isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Paper plugins ({}):\n - {}", paperPluginNames.size(), String.join("\n - ", paperPluginNames));
            } else {
                LOGGER.info("Paper plugins ({}):\n - {}", paperPluginNames.size(), String.join(", ", paperPluginNames));
            }
        }
        if (!legacyPluginNames.isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Bukkit plugins ({}):\n - {}", legacyPluginNames.size(), String.join("\n - ", legacyPluginNames));
            } else {
                LOGGER.info("Bukkit plugins ({}):\n - {}", legacyPluginNames.size(), String.join(", ", legacyPluginNames));
            }
        }
    }

    // This will be the end of me...
    public static void reload(DedicatedServer dedicatedServer) {
        // Wipe the provider storage
        LaunchEntryPointHandler.INSTANCE.populateProviderStorage();
        try {
            load(dedicatedServer.options);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reload!", e);
        }

        boolean hasPaperPlugin = false;
        for (PluginProvider<?> provider : LaunchEntryPointHandler.INSTANCE.getStorage().get(Entrypoint.PLUGIN).getRegisteredProviders()) {
            if (provider instanceof PaperPluginParent.PaperServerPluginProvider) {
                hasPaperPlugin = true;
                break;
            }
        }

        if (hasPaperPlugin) {
            LOGGER.warn("======== WARNING ========");
            LOGGER.warn("You are reloading while having Paper plugins installed on your server.");
            LOGGER.warn("Paper plugins do NOT support being reloaded. This will cause some unexpected issues.");
            LOGGER.warn("=========================");
        }
    }
}
