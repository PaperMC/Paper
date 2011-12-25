package org.bukkit.plugin;

/**
 * Represents the order in which a plugin should be initialized and enabled
 */
public enum PluginLoadOrder {
    /**
     * Indicates that the plugin will be loaded at startup
     */
    STARTUP,
    /**
     * Indicates that the plugin will be loaded after the first/default world was created
     */
    POSTWORLD
}
