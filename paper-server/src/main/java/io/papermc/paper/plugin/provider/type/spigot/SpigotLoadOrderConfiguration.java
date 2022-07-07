package io.papermc.paper.plugin.provider.type.spigot;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.configuration.LoadOrderConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpigotLoadOrderConfiguration implements LoadOrderConfiguration {

    private final PluginDescriptionFile meta;
    private final List<String> loadBefore;
    private final List<String> loadAfter;

    public SpigotLoadOrderConfiguration(SpigotPluginProvider spigotPluginProvider, Map<String, PluginProvider<?>> toLoad) {
        this.meta = spigotPluginProvider.getMeta();

        this.loadBefore = meta.getLoadBeforePlugins();
        this.loadAfter = new ArrayList<>();
        this.loadAfter.addAll(meta.getDepend());
        this.loadAfter.addAll(meta.getSoftDepend());

        // First: Remove as load after IF already in loadbefore
        // Some plugins would put a plugin both in depends and in loadbefore,
        // so in this case, we just ignore the effects of depend.
        for (String loadBefore : this.loadBefore) {
            this.loadAfter.remove(loadBefore);
        }

        // Second: Do a basic check to see if any other dependencies refer back to this plugin.
        Iterator<String> iterators = this.loadAfter.iterator();
        while (iterators.hasNext()) {
            String loadAfter = iterators.next();
            PluginProvider<?> provider = toLoad.get(loadAfter);
            if (provider != null) {
                PluginMeta configuration = provider.getMeta();
                // Does a configuration refer back to this plugin?
                Set<String> dependencies = new HashSet<>();
                dependencies.addAll(configuration.getPluginDependencies());
                dependencies.addAll(configuration.getPluginSoftDependencies());

                if (configuration.getName().equals(this.meta.getName()) || dependencies.contains(this.meta.getName())) {
                    iterators.remove(); // Let the other config deal with it
                }
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
        return this.meta;
    }
}
