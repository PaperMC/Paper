package io.papermc.paper.plugin.bootstrap;

import io.papermc.paper.plugin.provider.util.ProviderUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A plugin bootstrap is meant for loading certain parts of the plugin before the server is loaded.
 * <p>
 * Plugin bootstrapping allows values to be initialized in certain parts of the server that might not be allowed
 * when the server is running.
 * <p>
 * Your bootstrap class will be on the same classloader as your JavaPlugin.
 * <p>
 * <b>All calls to Bukkit may throw a NullPointerExceptions or return null unexpectedly. You should only call api methods that are explicitly documented to work in the bootstrapper</b>
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.OverrideOnly
public interface PluginBootstrap {

    /**
     * Called by the server, allowing you to bootstrap the plugin with a context that provides things like a logger and your shared plugin configuration file.
     *
     * @param context the server provided context
     */
    void bootstrap(BootstrapContext context);

    /**
     * Called by the server to instantiate your main class.
     * Plugins may override this logic to define custom creation logic for said instance, like passing additional
     * constructor arguments.
     *
     * @param context the server created bootstrap object
     * @return the server requested instance of the plugins main class.
     */
    default JavaPlugin createPlugin(final PluginProviderContext context) {
        return ProviderUtil.loadClass(context.getConfiguration().getMainClass(), JavaPlugin.class, this.getClass().getClassLoader());
    }
}
