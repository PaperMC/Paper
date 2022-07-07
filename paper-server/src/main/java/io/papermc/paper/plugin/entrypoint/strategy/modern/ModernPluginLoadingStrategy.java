package io.papermc.paper.plugin.entrypoint.strategy.modern;

import com.google.common.collect.Maps;
import com.google.common.graph.GraphBuilder;
import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.dependency.GraphDependencyContext;
import io.papermc.paper.plugin.entrypoint.dependency.MetaDependencyTree;
import io.papermc.paper.plugin.entrypoint.strategy.ProviderConfiguration;
import io.papermc.paper.plugin.entrypoint.strategy.ProviderLoadingStrategy;
import io.papermc.paper.plugin.provider.PluginProvider;
import org.bukkit.plugin.UnknownDependencyException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class ModernPluginLoadingStrategy<T> implements ProviderLoadingStrategy<T> {

    private static final Logger LOGGER = LogUtils.getClassLogger();
    private final ProviderConfiguration<T> configuration;

    public ModernPluginLoadingStrategy(ProviderConfiguration<T> onLoad) {
        this.configuration = onLoad;
    }

    @Override
    public List<ProviderPair<T>> loadProviders(List<PluginProvider<T>> pluginProviders, MetaDependencyTree dependencyTree) {
        Map<String, PluginProviderEntry<T>> providerMap = new HashMap<>();
        Map<String, PluginProvider<?>> providerMapMirror = Maps.transformValues(providerMap, (entry) -> entry.provider);
        List<PluginProvider<T>> validatedProviders = new ArrayList<>();

        // Populate provider map
        for (PluginProvider<T> provider : pluginProviders) {
            PluginMeta providerConfig = provider.getMeta();
            PluginProviderEntry<T> entry = new PluginProviderEntry<>(provider);

            PluginProviderEntry<T> replacedProvider = providerMap.put(providerConfig.getName(), entry);
            if (replacedProvider != null) {
                LOGGER.error(String.format(
                    "Ambiguous plugin name '%s' for files '%s' and '%s' in '%s'",
                    providerConfig.getName(),
                    provider.getSource(),
                    replacedProvider.provider.getSource(),
                    replacedProvider.provider.getParentSource()
                ));
                this.configuration.onGenericError(replacedProvider.provider);
            }

            for (String extra : providerConfig.getProvidedPlugins()) {
                PluginProviderEntry<T> replacedExtraProvider = providerMap.putIfAbsent(extra, entry);
                if (replacedExtraProvider != null) {
                    LOGGER.warn(String.format(
                        "`%s' is provided by both `%s' and `%s'",
                        extra,
                        providerConfig.getName(),
                        replacedExtraProvider.provider.getMeta().getName()
                    ));
                }
            }
        }

        // Populate dependency tree
        for (PluginProvider<?> validated : pluginProviders) {
            dependencyTree.add(validated);
        }

        // Validate providers, ensuring all of them have valid dependencies. Removing those who are invalid
        for (PluginProvider<T> provider : pluginProviders) {
            PluginMeta configuration = provider.getMeta();

            // Populate missing dependencies to capture if there are multiple missing ones.
            List<String> missingDependencies = provider.validateDependencies(dependencyTree);

            if (missingDependencies.isEmpty()) {
                validatedProviders.add(provider);
            } else {
                LOGGER.error("Could not load '%s' in '%s'".formatted(provider.getSource(), provider.getParentSource()), new UnknownDependencyException(missingDependencies, configuration.getName())); // Paper
                // Because the validator is invalid, remove it from the provider map
                providerMap.remove(configuration.getName());
                // Cleanup plugins that failed to load
                dependencyTree.remove(provider);
                this.configuration.onGenericError(provider);
            }
        }

        LoadOrderTree loadOrderTree = new LoadOrderTree(providerMapMirror, GraphBuilder.directed().build());
        // Populate load order tree
        for (PluginProvider<?> validated : validatedProviders) {
            loadOrderTree.add(validated);
        }

        // Reverse the topographic search to let us see which providers we can load first.
        List<String> reversedTopographicSort = loadOrderTree.getLoadOrder();
        List<ProviderPair<T>> loadedPlugins = new ArrayList<>();
        for (String providerIdentifier : reversedTopographicSort) {
            // It's possible that this will be null because the above dependencies for soft/load before aren't validated if they exist.
            // The graph could be MutableGraph<PluginProvider<T>>, but we would have to check if each dependency exists there... just
            // nicer to do it here TBH.
            PluginProviderEntry<T> retrievedProviderEntry = providerMap.get(providerIdentifier);
            if (retrievedProviderEntry == null || retrievedProviderEntry.provided) {
                // OR if this was already provided (most likely from a plugin that already "provides" that dependency)
                // This won't matter since the provided plugin is loaded as a dependency, meaning it should have been loaded correctly anyways
                continue; // Skip provider that doesn't exist....
            }
            retrievedProviderEntry.provided = true;
            PluginProvider<T> retrievedProvider = retrievedProviderEntry.provider;
            try {
                this.configuration.applyContext(retrievedProvider, dependencyTree);

                if (this.configuration.preloadProvider(retrievedProvider)) {
                    T instance = retrievedProvider.createInstance();
                    if (this.configuration.load(retrievedProvider, instance)) {
                        loadedPlugins.add(new ProviderPair<>(retrievedProvider, instance));
                    }
                }
            } catch (Throwable ex) {
                LOGGER.error("Could not load plugin '%s' in folder '%s'".formatted(retrievedProvider.getFileName(), retrievedProvider.getParentSource()), ex); // Paper
            }
        }

        return loadedPlugins;
    }

    private static class PluginProviderEntry<T> {

        private final PluginProvider<T> provider;
        private boolean provided;

        private PluginProviderEntry(PluginProvider<T> provider) {
            this.provider = provider;
        }
    }
}
