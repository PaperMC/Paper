
package org.bukkit.plugin;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Represents a plugin loader, which provides access and management for all plugins
 * currently loaded on a server instance
 */
public interface PluginLoader {
    /**
     * Checks if the given plugin is loaded and returns it when applicable
     *
     * Please note that the name of the plugin is case-sensitive
     *
     * @param name Name of the plugin to check
     * @return Plugin if it exists, otherwise null
     */
    public Plugin getPlugin(String name);
    /**
     * Checks if the given plugin is enabled or not
     *
     * Please note that the name of the plugin is case-sensitive.
     *
     * @param name Name of the plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    public boolean isPluginEnabled(String name);

    /**
     * Checks if the given plugin is enabled or not
     *
     * @param plugin Plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    public boolean isPluginEnabled(Plugin plugin);

    /**
     * Loads the plugin contained in the specified file
     *
     * @param file File to attempt to load
     * @return Plugin that was contained in the specified file, or null if
     * unsuccessful
     */
    public Plugin loadPlugin(File file);

    /**
     * Returns a list of all filename filters expected by this PluginLoader
     */
    public Pattern[] getPluginFileFilters();
}
