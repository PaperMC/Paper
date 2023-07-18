package io.papermc.paper.plugin.bootstrap;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents the context provided to a {@link PluginBootstrap} during both the bootstrapping and plugin
 * instantiation logic.
 * A bootstrap context may be used to access data or logic usually provided to {@link org.bukkit.plugin.Plugin} instances
 * like the plugin's configuration or logger during the plugins bootstrap.
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface BootstrapContext extends PluginProviderContext, LifecycleEventOwner {

    /**
     * Get the lifecycle event manager for registering handlers
     * for lifecycle events allowed on the {@link BootstrapContext}.
     *
     * @return the lifecycle event manager
     */
    LifecycleEventManager<BootstrapContext> getLifecycleManager();
}
