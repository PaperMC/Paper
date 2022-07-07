package io.papermc.paper.plugin.provider.type.paper;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.configuration.LoadOrderConfiguration;
import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;
import io.papermc.paper.plugin.provider.configuration.type.DependencyConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaperBootstrapOrderConfiguration implements LoadOrderConfiguration {

    private final PaperPluginMeta paperPluginMeta;
    private final List<String> loadBefore = new ArrayList<>();
    private final List<String> loadAfter = new ArrayList<>();

    public PaperBootstrapOrderConfiguration(PaperPluginMeta paperPluginMeta) {
        this.paperPluginMeta = paperPluginMeta;

        for (Map.Entry<String, DependencyConfiguration> configuration : paperPluginMeta.getBootstrapDependencies().entrySet()) {
            String name = configuration.getKey();
            DependencyConfiguration dependencyConfiguration = configuration.getValue();

            if (dependencyConfiguration.load() == DependencyConfiguration.LoadOrder.AFTER) {
                // This plugin will load BEFORE all dependencies (so dependencies will load AFTER plugin)
                this.loadBefore.add(name);
            } else if (dependencyConfiguration.load() == DependencyConfiguration.LoadOrder.BEFORE) {
                // This plugin will load AFTER all dependencies (so dependencies will load BEFORE plugin)
                this.loadAfter.add(name);
            }
        }
    }

    @Override
    public @NotNull List<String> getLoadBefore() {
        return this.loadBefore;
    }

    @Override
    public @NotNull List<String> getLoadAfter() {
        return this.loadAfter;
    }

    @Override
    public @NotNull PluginMeta getMeta() {
        return this.paperPluginMeta;
    }
}
