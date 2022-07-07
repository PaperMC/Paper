package io.papermc.paper.plugin.provider.configuration;

import io.papermc.paper.plugin.configuration.PluginMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This is used for plugins to configure the load order of strategies.
 */
public interface LoadOrderConfiguration {

    /**
     * Provides a list of plugins that THIS configuration should load
     * before.
     *
     * @return list of plugins
     */
    @NotNull
    List<String> getLoadBefore();

    /**
     * Provides a list of plugins that THIS configuration should load
     * before.
     *
     * @return list of plugins
     */
    @NotNull
    List<String> getLoadAfter();

    /**
     * Gets the responsible plugin provider's meta.
     *
     * @return meta
     */
    @NotNull
    PluginMeta getMeta();
}
