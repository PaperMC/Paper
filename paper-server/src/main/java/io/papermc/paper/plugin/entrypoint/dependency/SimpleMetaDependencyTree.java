package io.papermc.paper.plugin.entrypoint.dependency;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import io.papermc.paper.plugin.configuration.PluginMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class SimpleMetaDependencyTree extends MetaDependencyTree {

    public SimpleMetaDependencyTree() {
    }

    public SimpleMetaDependencyTree(final MutableGraph<String> graph) {
        super(graph);
    }

    @Override
    protected void registerDependencies(final String identifier, final PluginMeta meta) {
        for (String dependency : meta.getPluginDependencies()) {
            this.graph.putEdge(identifier, dependency);
        }
        for (String dependency : meta.getPluginSoftDependencies()) {
            this.graph.putEdge(identifier, dependency);
        }
    }

    @Override
    protected void unregisterDependencies(final String identifier, final PluginMeta meta) {
        for (String dependency : meta.getPluginDependencies()) {
            this.graph.removeEdge(identifier, dependency);
        }
        for (String dependency : meta.getPluginSoftDependencies()) {
            this.graph.removeEdge(identifier, dependency);
        }
    }
}
