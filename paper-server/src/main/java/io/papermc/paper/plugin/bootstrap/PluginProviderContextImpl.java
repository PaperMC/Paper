package io.papermc.paper.plugin.bootstrap;

import io.papermc.paper.plugin.PluginInitializerManager;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class PluginProviderContextImpl implements PluginProviderContext {

    private final PluginMeta config;
    private final Path dataFolder;
    private final ComponentLogger logger;
    private final Path pluginSource;

    public PluginProviderContextImpl(PluginMeta config, Path dataFolder, ComponentLogger logger, Path pluginSource) {
        this.config = config;
        this.dataFolder = dataFolder;
        this.logger = logger;
        this.pluginSource = pluginSource;
    }

    public static PluginProviderContextImpl create(PluginMeta config, ComponentLogger logger, Path pluginSource) {
        Path dataFolder = PluginInitializerManager.instance().pluginDirectoryPath().resolve(config.getName());

        return new PluginProviderContextImpl(config, dataFolder, logger, pluginSource);
    }

    @Override
    public @NotNull PluginMeta getConfiguration() {
        return this.config;
    }

    @Override
    public @NotNull Path getDataDirectory() {
        return this.dataFolder;
    }

    @Override
    public @NotNull ComponentLogger getLogger() {
        return this.logger;
    }

    @Override
    public @NotNull Path getPluginSource() {
        return this.pluginSource;
    }
}
