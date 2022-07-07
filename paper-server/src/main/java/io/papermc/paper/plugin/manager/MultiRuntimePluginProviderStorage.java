package io.papermc.paper.plugin.manager;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.entrypoint.Entrypoint;
import io.papermc.paper.plugin.entrypoint.LaunchEntryPointHandler;
import io.papermc.paper.plugin.entrypoint.dependency.GraphDependencyContext;
import io.papermc.paper.plugin.entrypoint.dependency.MetaDependencyTree;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.type.paper.PaperPluginParent;
import io.papermc.paper.plugin.storage.ServerPluginProviderStorage;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MultiRuntimePluginProviderStorage extends ServerPluginProviderStorage {

    private static final Logger LOGGER = LogUtils.getClassLogger();
    private final List<JavaPlugin> provided = new ArrayList<>();

    private final MetaDependencyTree dependencyTree;

    MultiRuntimePluginProviderStorage(MetaDependencyTree dependencyTree) {
        this.dependencyTree = dependencyTree;
    }

    @Override
    public void register(PluginProvider<JavaPlugin> provider) {
        if (provider instanceof PaperPluginParent.PaperServerPluginProvider) {
            LOGGER.warn("Skipping loading of paper plugin requested from SimplePluginManager.");
            return;
        }
        super.register(provider);
        /*
        Register the provider into the server entrypoint, this allows it to show in /plugins correctly. Generally it might be better in the future to make a separate storage,
         as putting it into the entrypoint handlers doesn't make much sense.
         */
        LaunchEntryPointHandler.INSTANCE.register(Entrypoint.PLUGIN, provider);
    }

    @Override
    public void processProvided(PluginProvider<JavaPlugin> provider, JavaPlugin provided) {
        super.processProvided(provider, provided);
        this.provided.add(provided);
    }

    @Override
    public boolean throwOnCycle() {
        return false;
    }

    public List<JavaPlugin> getLoaded() {
        return this.provided;
    }

    @Override
    public MetaDependencyTree createDependencyTree() {
        return this.dependencyTree;
    }
}
