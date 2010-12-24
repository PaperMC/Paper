
package org.bukkit.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import org.bukkit.Server;
import java.util.regex.Pattern;

/**
 * Handles all plugin management from the Server
 */
public final class PluginManager {
    private final Server server;
    private final HashMap<Pattern, PluginLoader> fileAssociations = new HashMap<Pattern, PluginLoader>();

    public PluginManager(Server instance) {
        server = instance;
    }

    /**
     * Registers the specified plugin loader
     *
     * @param loader PluginLoader to register
     */
    public void RegisterInterface(PluginLoader loader) {
        Pattern[] patterns = loader.getPluginFileFilters();

        for (Pattern pattern : patterns) {
            fileAssociations.put(pattern, loader);
        }
    }

    /**
     * Loads the plugins contained within the specified directory
     *
     * @param directory Directory to check for plugins
     * @return A list of all plugins loaded
     */
    public Plugin[] loadPlugins(File directory) {
        List<Plugin> result = new ArrayList<Plugin>();
        File[] files = directory.listFiles();

        for (File file : files) {
            Plugin plugin = loadPlugin(file);
            
            if (plugin != null) {
                result.add(plugin);
            }
        }

        return (Plugin[])result.toArray();
    }

    /**
     * Loads the plugin in the specified file
     *
     * File must be valid according to the current enabled Plugin interfaces
     *
     * @param file File containing the plugin to load
     * @return The Plugin loaded, or null if it was invalid
     */
    public Plugin loadPlugin(File file) {
        Set<Pattern> filters = fileAssociations.keySet();
        Plugin result = null;

        for (Pattern filter : filters) {
            String name = file.getName();
            Matcher match = filter.matcher(name);

            if (match.find()) {
                PluginLoader loader = fileAssociations.get(filter);
                result = loader.loadPlugin(file);
            }
        }

        return result;
    }
}
