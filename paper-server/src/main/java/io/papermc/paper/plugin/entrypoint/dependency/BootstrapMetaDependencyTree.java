package io.papermc.paper.plugin.entrypoint.dependency;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;

public class BootstrapMetaDependencyTree extends MetaDependencyTree {
    public BootstrapMetaDependencyTree() {
        this(GraphBuilder.directed().build());
    }

    public BootstrapMetaDependencyTree(MutableGraph<String> graph) {
        super(graph);
    }

    @Override
    protected void registerDependencies(final String identifier, final PluginMeta meta) {
        if (!(meta instanceof PaperPluginMeta paperPluginMeta)) {
            throw new IllegalStateException("Only paper plugins can have a bootstrapper!");
        }
        // Build a validated provider's dependencies into the graph
        for (String dependency : paperPluginMeta.getBootstrapDependencies().keySet()) {
            this.graph.putEdge(identifier, dependency);
        }
    }

    @Override
    protected void unregisterDependencies(final String identifier, final PluginMeta meta) {
        if (!(meta instanceof PaperPluginMeta paperPluginMeta)) {
            throw new IllegalStateException("PluginMeta must be a PaperPluginMeta");
        }

        // Build a validated provider's dependencies into the graph
        for (String dependency : paperPluginMeta.getBootstrapDependencies().keySet()) {
            this.graph.removeEdge(identifier, dependency);
        }
    }

    @Override
    public String toString() {
        return "BootstrapDependencyTree{" + "graph=" + this.graph + '}';
    }
}
