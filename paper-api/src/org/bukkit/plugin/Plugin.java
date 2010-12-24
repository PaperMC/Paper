
package org.bukkit.plugin;

import org.bukkit.Server;

/**
 * Represents a plugin
 */
public abstract class Plugin {
    private boolean isEnabled = false;
    private final PluginLoader loader;
    private final Server server;

    /**
     * Constructs a new plugin instance
     *
     * @param pluginLoader PluginLoader that is responsible for this plugin
     * @param instance Server instance that is running this plugin
     */
    protected Plugin(PluginLoader pluginLoader, Server instance) {
        loader = pluginLoader;
        server = instance;
    }

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    protected final PluginLoader getPluginLoader() {
        return loader;
    }

    /**
     * Returns the Server instance currently running this plugin
     *
     * @return Server running this plugin
     */
    public final Server getServer() {
        return server;
    }

    /**
     * Returns a value indicating whether or not this plugin is currently enabled
     * 
     * @return true if this plugin is enabled, otherwise false
     */
    public final boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Called when this plugin is enabled
     */
    protected abstract void onEnable();

    /**
     * Called when this plugin is disabled
     */
    protected abstract void onDisable();

    /**
     * Called when this plugin is first initialized
     */
    protected abstract void onInitialize();
}
