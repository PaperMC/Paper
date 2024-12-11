package io.papermc.paper.plugin.entrypoint.strategy.modern;

import com.google.common.collect.Lists;
import com.google.common.graph.MutableGraph;
import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.strategy.JohnsonSimpleCycles;
import io.papermc.paper.plugin.entrypoint.strategy.PluginGraphCycleException;
import io.papermc.paper.plugin.entrypoint.strategy.TopographicGraphSorter;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.configuration.LoadOrderConfiguration;
import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;
import io.papermc.paper.plugin.provider.type.spigot.SpigotPluginProvider;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class LoadOrderTree {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    private final Map<String, PluginProvider<?>> providerMap;
    private final MutableGraph<String> graph;

    public LoadOrderTree(Map<String, PluginProvider<?>> providerMapMirror, MutableGraph<String> graph) {
        this.providerMap = providerMapMirror;
        this.graph = graph;
    }

    public void add(PluginProvider<?> provider) {
        LoadOrderConfiguration configuration = provider.createConfiguration(this.providerMap);

        // Build a validated provider's load order changes
        String identifier = configuration.getMeta().getName();
        for (String dependency : configuration.getLoadAfter()) {
            if (this.providerMap.containsKey(dependency)) {
                this.graph.putEdge(identifier, dependency);
            }
        }

        for (String loadBeforeTarget : configuration.getLoadBefore()) {
            if (this.providerMap.containsKey(loadBeforeTarget)) {
                this.graph.putEdge(loadBeforeTarget, identifier);
            }
        }

        this.graph.addNode(identifier); // Make sure load order has at least one node
    }

    public List<String> getLoadOrder() throws PluginGraphCycleException {
        List<String> reversedTopographicSort;
        try {
            reversedTopographicSort = Lists.reverse(TopographicGraphSorter.sortGraph(this.graph));
        } catch (TopographicGraphSorter.GraphCycleException exception) {
            List<List<String>> cycles = new JohnsonSimpleCycles<>(this.graph).findAndRemoveSimpleCycles();

            // Only log an error if at least non-Spigot plugin is present in the cycle
            // Due to Spigot plugin metadata making no distinction between load order and dependencies (= class loader access), cycles are an unfortunate reality we have to deal with
            Set<String> cyclingPlugins = new HashSet<>();
            cycles.forEach(cyclingPlugins::addAll);
            if (cyclingPlugins.stream().anyMatch(plugin -> {
                PluginProvider<?> pluginProvider = this.providerMap.get(plugin);
                return pluginProvider != null && !(pluginProvider instanceof SpigotPluginProvider);
            })) {
                logCycleError(cycles, this.providerMap);
            }

            // Try again after hopefully having removed all cycles
            try {
                reversedTopographicSort = Lists.reverse(TopographicGraphSorter.sortGraph(this.graph));
            } catch (TopographicGraphSorter.GraphCycleException e) {
                throw new PluginGraphCycleException(cycles);
            }
        }

        return reversedTopographicSort;
    }

    private void logCycleError(List<List<String>> cycles, Map<String, PluginProvider<?>> providerMapMirror) {
        LOGGER.error("=================================");
        LOGGER.error("Circular plugin loading detected:");
        for (int i = 0; i < cycles.size(); i++) {
            List<String> cycle = cycles.get(i);
            LOGGER.error("{}) {} -> {}", i + 1, String.join(" -> ", cycle), cycle.get(0));
            for (String pluginName : cycle) {
                PluginProvider<?> pluginProvider = providerMapMirror.get(pluginName);
                if (pluginProvider == null) {
                    return;
                }

                logPluginInfo(pluginProvider.getMeta());
            }
        }

        LOGGER.error("Please report this to the plugin authors of the first plugin of each loop or join the PaperMC Discord server for further help.");
        LOGGER.error("=================================");
    }

    private void logPluginInfo(PluginMeta meta) {
        if (!meta.getLoadBeforePlugins().isEmpty()) {
            LOGGER.error("   {} loadbefore: {}", meta.getName(), meta.getLoadBeforePlugins());
        }

        if (meta instanceof PaperPluginMeta paperPluginMeta) {
            if (!paperPluginMeta.getLoadAfterPlugins().isEmpty()) {
                LOGGER.error("   {} loadafter: {}", meta.getName(), paperPluginMeta.getLoadAfterPlugins());
            }
        } else {
            List<String> dependencies = new ArrayList<>();
            dependencies.addAll(meta.getPluginDependencies());
            dependencies.addAll(meta.getPluginSoftDependencies());
            if (!dependencies.isEmpty()) {
                LOGGER.error("   {} depend/softdepend: {}", meta.getName(), dependencies);
            }
        }
    }
}
