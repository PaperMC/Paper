
package org.bukkit.plugin;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Represents a plugin loader, which handles direct access to specific types
 * of plugins
 */
public interface PluginLoader {
    /**
     * Loads the plugin contained in the specified file
     *
     * @param file File to attempt to load
     * @return Plugin that was contained in the specified file, or null if
     * unsuccessful
     * @throws InvalidPluginException Thrown when the specified file is not a plugin
     */
    public Plugin loadPlugin(File file) throws InvalidPluginException;

    /**
     * Returns a list of all filename filters expected by this PluginLoader
     */
    public Pattern[] getPluginFileFilters();
}
