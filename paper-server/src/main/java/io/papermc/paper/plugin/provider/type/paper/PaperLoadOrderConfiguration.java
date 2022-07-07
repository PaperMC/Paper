package io.papermc.paper.plugin.provider.type.paper;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.configuration.LoadOrderConfiguration;
import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;
import io.papermc.paper.plugin.provider.type.spigot.SpigotPluginProvider;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PaperLoadOrderConfiguration implements LoadOrderConfiguration {

    private final PaperPluginMeta meta;
    private final List<String> loadBefore;
    private final List<String> loadAfter;

    public PaperLoadOrderConfiguration(PaperPluginMeta meta) {
        this.meta = meta;

        this.loadBefore = this.meta.getLoadBeforePlugins();
        this.loadAfter = this.meta.getLoadAfterPlugins();
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
        return this.meta;
    }
}
