package io.papermc.paper.plugin.storage;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import io.papermc.paper.plugin.entrypoint.dependency.DependencyContextHolder;
import io.papermc.paper.plugin.entrypoint.strategy.ProviderConfiguration;
import io.papermc.paper.plugin.manager.PaperPluginManagerImpl;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.ProviderStatus;
import io.papermc.paper.plugin.provider.ProviderStatusHolder;
import io.papermc.paper.plugin.provider.type.paper.PaperPluginParent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.util.List;

public class ServerPluginProviderStorage extends ConfiguredProviderStorage<JavaPlugin> {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    public ServerPluginProviderStorage() {
        super(new ProviderConfiguration<>() {
            @Override
            public void applyContext(PluginProvider<JavaPlugin> provider, DependencyContext dependencyContext) {
                Plugin alreadyLoadedPlugin = PaperPluginManagerImpl.getInstance().getPlugin(provider.getMeta().getName());
                if (alreadyLoadedPlugin != null) {
                    throw new IllegalStateException("Provider " + provider + " attempted to add duplicate plugin identifier " + alreadyLoadedPlugin + " THIS WILL CREATE BUGS!!!");
                }

                if (provider instanceof DependencyContextHolder contextHolder) {
                    contextHolder.setContext(dependencyContext);
                }
            }

            @Override
            public boolean load(PluginProvider<JavaPlugin> provider, JavaPlugin provided) {
                // Add it to the map here, we have to run the actual loading logic later.
                PaperPluginManagerImpl.getInstance().loadPlugin(provided);
                return true;
            }
        });
    }

    @Override
    protected void filterLoadingProviders(List<PluginProvider<JavaPlugin>> pluginProviders) {
         /*
        Have to do this to prevent loading plugin providers that have failed initializers.
        This is a hack and a better solution here would be to store failed plugin providers elsewhere.
         */
        pluginProviders.removeIf((provider) -> (provider instanceof PaperPluginParent.PaperServerPluginProvider pluginProvider && pluginProvider.shouldSkipCreation()));
    }

    // We need to call the load methods AFTER all plugins are constructed
    @Override
    public void processProvided(PluginProvider<JavaPlugin> provider, JavaPlugin provided) {
        try {
            provided.getLogger().info(String.format("Loading server plugin %s", provided.getPluginMeta().getDisplayName()));
            provided.onLoad();
        } catch (Throwable ex) {
            // Don't mark that provider as ERRORED, as this apparently still needs to run the onEnable logic.
            provided.getSLF4JLogger().error("Error initializing plugin '%s' in folder '%s' (Is it up to date?)".formatted(provider.getFileName(), provider.getParentSource()), ex);
        }
    }

    @Override
    public String toString() {
        return "PLUGIN:" + super.toString();
    }
}
