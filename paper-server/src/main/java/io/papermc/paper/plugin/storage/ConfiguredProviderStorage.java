package io.papermc.paper.plugin.storage;

import io.papermc.paper.plugin.entrypoint.strategy.LegacyPluginLoadingStrategy;
import io.papermc.paper.plugin.entrypoint.strategy.modern.ModernPluginLoadingStrategy;
import io.papermc.paper.plugin.entrypoint.strategy.ProviderConfiguration;

public abstract class ConfiguredProviderStorage<T> extends SimpleProviderStorage<T> {

    public static final boolean LEGACY_PLUGIN_LOADING = Boolean.getBoolean("paper.useLegacyPluginLoading");

    protected ConfiguredProviderStorage(ProviderConfiguration<T> onLoad) {
        // This doesn't work with reloading.
        // Should we care?
        super(LEGACY_PLUGIN_LOADING ? new LegacyPluginLoadingStrategy<>(onLoad) : new ModernPluginLoadingStrategy<>(onLoad));
    }

}
