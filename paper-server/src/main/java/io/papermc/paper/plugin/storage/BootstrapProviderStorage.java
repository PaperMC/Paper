package io.papermc.paper.plugin.storage;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.PluginInitializerManager;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginBootstrapContextImpl;
import io.papermc.paper.plugin.entrypoint.dependency.BootstrapMetaDependencyTree;
import io.papermc.paper.plugin.entrypoint.dependency.DependencyContextHolder;
import io.papermc.paper.plugin.entrypoint.dependency.MetaDependencyTree;
import io.papermc.paper.plugin.entrypoint.strategy.ProviderConfiguration;
import io.papermc.paper.plugin.entrypoint.strategy.modern.ModernPluginLoadingStrategy;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.ProviderStatus;
import io.papermc.paper.plugin.provider.ProviderStatusHolder;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import org.slf4j.Logger;

public class BootstrapProviderStorage extends SimpleProviderStorage<PluginBootstrap> {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    public BootstrapProviderStorage() {
        super(new ModernPluginLoadingStrategy<>(new ProviderConfiguration<>() {
            @Override
            public void applyContext(PluginProvider<PluginBootstrap> provider, DependencyContext dependencyContext) {
                if (provider instanceof DependencyContextHolder contextHolder) {
                    contextHolder.setContext(dependencyContext);
                }
            }

            @Override
            public boolean load(PluginProvider<PluginBootstrap> provider, PluginBootstrap provided) {
                try {
                    PluginBootstrapContextImpl context = PluginBootstrapContextImpl.create(provider, PluginInitializerManager.instance().pluginDirectoryPath()); // Paper - lifecycle events
                    provided.bootstrap(context);
                    context.lockLifecycleEventRegistration(); // Paper - lifecycle events
                    return true;
                } catch (Throwable e) {
                    LOGGER.error("Failed to run bootstrapper for %s. This plugin will not be loaded.".formatted(provider.getSource()), e);
                    if (provider instanceof ProviderStatusHolder statusHolder) {
                        statusHolder.setStatus(ProviderStatus.ERRORED);
                    }
                    return false;
                }
            }

            @Override
            public void onGenericError(PluginProvider<PluginBootstrap> provider) {
                if (provider instanceof ProviderStatusHolder statusHolder) {
                    statusHolder.setStatus(ProviderStatus.ERRORED);
                }
            }
        }));
    }

    @Override
    public MetaDependencyTree createDependencyTree() {
        return new BootstrapMetaDependencyTree();
    }

    @Override
    public String toString() {
        return "BOOTSTRAP:" + super.toString();
    }
}
