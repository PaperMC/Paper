package io.papermc.paper.plugin.entrypoint.strategy;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.dependency.GraphDependencyContext;
import io.papermc.paper.plugin.entrypoint.dependency.MetaDependencyTree;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.type.paper.PaperPluginParent;
import org.bukkit.plugin.UnknownDependencyException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public class LegacyPluginLoadingStrategy<T> implements ProviderLoadingStrategy<T> {

    private static final Logger LOGGER = Logger.getLogger("LegacyPluginLoadingStrategy");
    private final ProviderConfiguration<T> configuration;

    public LegacyPluginLoadingStrategy(ProviderConfiguration<T> onLoad) {
        this.configuration = onLoad;
    }

    @Override
    public List<ProviderPair<T>> loadProviders(List<PluginProvider<T>> providers, MetaDependencyTree dependencyTree) {
        List<ProviderPair<T>> javapluginsLoaded = new ArrayList<>();
        MutableGraph<String> dependencyGraph = dependencyTree.getGraph();

        Map<String, PluginProvider<T>> providersToLoad = new HashMap<>();
        Set<String> loadedPlugins = new HashSet<>();
        Map<String, String> pluginsProvided = new HashMap<>();
        Map<String, Collection<String>> dependencies = new HashMap<>();
        Map<String, Collection<String>> softDependencies = new HashMap<>();

        for (PluginProvider<T> provider : providers) {
            PluginMeta configuration = provider.getMeta();

            PluginProvider<T> replacedProvider = providersToLoad.put(configuration.getName(), provider);
            dependencyTree.addDirectDependency(configuration.getName()); // add to dependency tree
            if (replacedProvider != null) {
                LOGGER.severe(String.format(
                    "Ambiguous plugin name `%s' for files `%s' and `%s' in `%s'",
                    configuration.getName(),
                    provider.getSource(),
                    replacedProvider.getSource(),
                    replacedProvider.getParentSource()
                ));
            }

            String removedProvided = pluginsProvided.remove(configuration.getName());
            if (removedProvided != null) {
                LOGGER.warning(String.format(
                    "Ambiguous plugin name `%s'. It is also provided by `%s'",
                    configuration.getName(),
                    removedProvided
                ));
            }

            for (String provided : configuration.getProvidedPlugins()) {
                PluginProvider<T> pluginProvider = providersToLoad.get(provided);

                if (pluginProvider != null) {
                    LOGGER.warning(String.format(
                        "`%s provides `%s' while this is also the name of `%s' in `%s'",
                        provider.getSource(),
                        provided,
                        pluginProvider.getSource(),
                        provider.getParentSource()
                    ));
                } else {
                    String replacedPlugin = pluginsProvided.put(provided, configuration.getName());
                    dependencyTree.addDirectDependency(provided); // add to dependency tree
                    if (replacedPlugin != null) {
                        LOGGER.warning(String.format(
                            "`%s' is provided by both `%s' and `%s'",
                            provided,
                            configuration.getName(),
                            replacedPlugin
                        ));
                    }
                }
            }

            Collection<String> softDependencySet = provider.getMeta().getPluginSoftDependencies();
            if (softDependencySet != null && !softDependencySet.isEmpty()) {
                if (softDependencies.containsKey(configuration.getName())) {
                    // Duplicates do not matter, they will be removed together if applicable
                    softDependencies.get(configuration.getName()).addAll(softDependencySet);
                } else {
                    softDependencies.put(configuration.getName(), new LinkedList<String>(softDependencySet));
                }

                for (String depend : softDependencySet) {
                    dependencyGraph.putEdge(configuration.getName(), depend);
                }
            }

            Collection<String> dependencySet = provider.getMeta().getPluginDependencies();
            if (dependencySet != null && !dependencySet.isEmpty()) {
                dependencies.put(configuration.getName(), new LinkedList<String>(dependencySet));

                for (String depend : dependencySet) {
                    dependencyGraph.putEdge(configuration.getName(), depend);
                }
            }

            Collection<String> loadBeforeSet = provider.getMeta().getLoadBeforePlugins();
            if (loadBeforeSet != null && !loadBeforeSet.isEmpty()) {
                for (String loadBeforeTarget : loadBeforeSet) {
                    if (softDependencies.containsKey(loadBeforeTarget)) {
                        softDependencies.get(loadBeforeTarget).add(configuration.getName());
                    } else {
                        // softDependencies is never iterated, so 'ghost' plugins aren't an issue
                        Collection<String> shortSoftDependency = new LinkedList<String>();
                        shortSoftDependency.add(configuration.getName());
                        softDependencies.put(loadBeforeTarget, shortSoftDependency);
                    }

                    dependencyGraph.putEdge(loadBeforeTarget, configuration.getName());
                }
            }
        }

        while (!providersToLoad.isEmpty()) {
            boolean missingDependency = true;
            Iterator<Map.Entry<String, PluginProvider<T>>> providerIterator = providersToLoad.entrySet().iterator();

            while (providerIterator.hasNext()) {
                Map.Entry<String, PluginProvider<T>> entry = providerIterator.next();
                String providerIdentifier = entry.getKey();

                if (dependencies.containsKey(providerIdentifier)) {
                    Iterator<String> dependencyIterator = dependencies.get(providerIdentifier).iterator();
                    final Set<String> missingHardDependencies = new HashSet<>(dependencies.get(providerIdentifier).size()); // Paper - list all missing hard depends

                    while (dependencyIterator.hasNext()) {
                        String dependency = dependencyIterator.next();

                        // Dependency loaded
                        if (loadedPlugins.contains(dependency)) {
                            dependencyIterator.remove();

                            // We have a dependency not found
                        } else if (!providersToLoad.containsKey(dependency) && !pluginsProvided.containsKey(dependency)) {
                            // Paper start
                            missingHardDependencies.add(dependency);
                        }
                    }
                    if (!missingHardDependencies.isEmpty()) {
                        // Paper end
                        missingDependency = false;
                        providerIterator.remove();
                        pluginsProvided.values().removeIf(s -> s.equals(providerIdentifier)); // Paper - remove provided plugins
                        softDependencies.remove(providerIdentifier);
                        dependencies.remove(providerIdentifier);

                        LOGGER.log(
                            Level.SEVERE,
                            "Could not load '" + entry.getValue().getSource() + "' in folder '" + entry.getValue().getParentSource() + "'", // Paper
                            new UnknownDependencyException(missingHardDependencies, providerIdentifier)); // Paper
                    }

                    if (dependencies.containsKey(providerIdentifier) && dependencies.get(providerIdentifier).isEmpty()) {
                        dependencies.remove(providerIdentifier);
                    }
                }
                if (softDependencies.containsKey(providerIdentifier)) {
                    Iterator<String> softDependencyIterator = softDependencies.get(providerIdentifier).iterator();

                    while (softDependencyIterator.hasNext()) {
                        String softDependency = softDependencyIterator.next();

                        // Soft depend is no longer around
                        if (!providersToLoad.containsKey(softDependency) && !pluginsProvided.containsKey(softDependency)) {
                            softDependencyIterator.remove();
                        }
                    }

                    if (softDependencies.get(providerIdentifier).isEmpty()) {
                        softDependencies.remove(providerIdentifier);
                    }
                }
                if (!(dependencies.containsKey(providerIdentifier) || softDependencies.containsKey(providerIdentifier)) && providersToLoad.containsKey(providerIdentifier)) {
                    // We're clear to load, no more soft or hard dependencies left
                    PluginProvider<T> file = providersToLoad.get(providerIdentifier);
                    providerIterator.remove();
                    pluginsProvided.values().removeIf(s -> s.equals(providerIdentifier)); // Paper - remove provided plugins
                    missingDependency = false;

                    try {
                        this.configuration.applyContext(file, dependencyTree);
                        T loadedPlugin = file.createInstance();
                        this.warnIfPaperPlugin(file);

                        if (this.configuration.load(file, loadedPlugin)) {
                            loadedPlugins.add(file.getMeta().getName());
                            loadedPlugins.addAll(file.getMeta().getProvidedPlugins());
                            javapluginsLoaded.add(new ProviderPair<>(file, loadedPlugin));
                        }

                    } catch (Throwable ex) {
                        LOGGER.log(Level.SEVERE, "Could not load '" + file.getSource() + "' in folder '" + file.getParentSource() + "'", ex); // Paper
                    }
                }
            }

            if (missingDependency) {
                // We now iterate over plugins until something loads
                // This loop will ignore soft dependencies
                providerIterator = providersToLoad.entrySet().iterator();

                while (providerIterator.hasNext()) {
                    Map.Entry<String, PluginProvider<T>> entry = providerIterator.next();
                    String plugin = entry.getKey();

                    if (!dependencies.containsKey(plugin)) {
                        softDependencies.remove(plugin);
                        missingDependency = false;
                        PluginProvider<T> file = entry.getValue();
                        providerIterator.remove();

                        try {
                            this.configuration.applyContext(file, dependencyTree);
                            T loadedPlugin = file.createInstance();
                            this.warnIfPaperPlugin(file);

                            if (this.configuration.load(file, loadedPlugin)) {
                                loadedPlugins.add(file.getMeta().getName());
                                loadedPlugins.addAll(file.getMeta().getProvidedPlugins());
                                javapluginsLoaded.add(new ProviderPair<>(file, loadedPlugin));
                            }
                            break;
                        } catch (Throwable ex) {
                            LOGGER.log(Level.SEVERE, "Could not load '" + file.getSource() + "' in folder '" + file.getParentSource() + "'", ex); // Paper
                        }
                    }
                }
                // We have no plugins left without a depend
                if (missingDependency) {
                    softDependencies.clear();
                    dependencies.clear();
                    Iterator<PluginProvider<T>> failedPluginIterator = providersToLoad.values().iterator();

                    while (failedPluginIterator.hasNext()) {
                        PluginProvider<T> file = failedPluginIterator.next();
                        failedPluginIterator.remove();
                        LOGGER.log(Level.SEVERE, "Could not load '" + file.getSource() + "' in folder '" + file.getParentSource() + "': circular dependency detected"); // Paper
                    }
                }
            }
        }

        return javapluginsLoaded;
    }

    private void warnIfPaperPlugin(PluginProvider<T> provider) {
        if (provider instanceof PaperPluginParent.PaperServerPluginProvider) {
            provider.getLogger().warn("Loading Paper plugin in the legacy plugin loading logic. This is not recommended and may introduce some differences into load order. It's highly recommended you move away from this if you are wanting to use Paper plugins.");
        }
    }
}
