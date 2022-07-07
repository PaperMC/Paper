package io.papermc.paper.plugin.storage;

import io.papermc.paper.plugin.entrypoint.dependency.MetaDependencyTree;
import io.papermc.paper.plugin.provider.PluginProvider;

/**
 * A provider storage is meant to be a singleton that stores providers.
 *
 * @param <T> provider type
 */
public interface ProviderStorage<T> {

    void register(PluginProvider<T> provider);

    MetaDependencyTree createDependencyTree();

    void enter();

    Iterable<PluginProvider<T>> getRegisteredProviders();

}
