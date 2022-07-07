package io.papermc.paper.plugin.entrypoint.strategy;

import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;

/**
 * Used to share code with the modern and legacy plugin load strategy.
 *
 * @param <T>
 */
public interface ProviderConfiguration<T> {

    void applyContext(PluginProvider<T> provider, DependencyContext dependencyContext);

    boolean load(PluginProvider<T> provider, T provided);

    default boolean preloadProvider(PluginProvider<T> provider) {
        return true;
    }

    default void onGenericError(PluginProvider<T> provider) {

    }

}
