package io.papermc.paper.plugin.entrypoint.strategy;

import io.papermc.paper.plugin.entrypoint.dependency.MetaDependencyTree;
import io.papermc.paper.plugin.provider.PluginProvider;

import java.util.List;

/**
 * Used by a {@link io.papermc.paper.plugin.storage.SimpleProviderStorage} to load plugin providers in a certain order.
 * <p>
 * Returns providers loaded.
 *
 * @param <P> provider type
 */
public interface ProviderLoadingStrategy<P> {

    List<ProviderPair<P>> loadProviders(List<PluginProvider<P>> providers, MetaDependencyTree dependencyTree);

    record ProviderPair<P>(PluginProvider<P> provider, P provided) {

    }
}
