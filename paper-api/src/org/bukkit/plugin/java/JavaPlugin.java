
package org.bukkit.plugin.java;

import java.io.File;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

/**
 * Represents a Java plugin
 */
public abstract class JavaPlugin implements Plugin {
    private boolean isEnabled = true;
    private final PluginLoader loader;
    private final Server server;
    private final File file;
    private final PluginDescriptionFile description;
    private final ClassLoader classLoader;

    /**
     * Constructs a new Java plugin instance
     *
     * @param pluginLoader PluginLoader that is responsible for this plugin
     * @param instance Server instance that is running this plugin
     * @param desc PluginDescriptionFile containing metadata on this plugin
     * @param plugin File containing this plugin
     * @param cLoader ClassLoader which holds this plugin
     */
    public JavaPlugin(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File plugin, ClassLoader cLoader) {
        loader = pluginLoader;
        server = instance;
        file = plugin;
        description = desc;
        classLoader = cLoader;
    }

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    public final PluginLoader getPluginLoader() {
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
     * Returns the file which contains this plugin
     *
     * @return File containing this plugin
     */
    protected File getFile() {
        return file;
    }

    /**
     * Returns the plugin.yaml file containing the details for this plugin
     *
     * @return Contents of the plugin.yaml file
     */
    public PluginDescriptionFile getDescription() {
        return description;
    }

    /**
     * Returns the ClassLoader which holds this plugin
     *
     * @return ClassLoader holding this plugin
     */
    protected ClassLoader getClassLoader() {
        return classLoader;
    }
}
