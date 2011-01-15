
package org.bukkit.plugin.java;

import java.io.File;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.util.config.Configuration;

/**
 * Represents a Java plugin
 */
public abstract class JavaPlugin implements Plugin {
    private boolean isEnabled = false;
    private final PluginLoader loader;
    private final Server server;
    private final File file;
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final ClassLoader classLoader;
    private final Configuration config;

    /**
     * Constructs a new Java plugin instance
     *
     * @param pluginLoader PluginLoader that is responsible for this plugin
     * @param instance Server instance that is running this plugin
     * @param desc PluginDescriptionFile containing metadata on this plugin
     * @param folder Folder containing the plugin's data
     * @param plugin File containing this plugin
     * @param cLoader ClassLoader which holds this plugin
     */
    public JavaPlugin(PluginLoader pluginLoader, Server instance,
            PluginDescriptionFile desc, File folder, File plugin,
            ClassLoader cLoader) {
        loader = pluginLoader;
        server = instance;
        file = plugin;
        description = desc;
        dataFolder = folder;
        classLoader = cLoader;
        config = new Configuration(new File(dataFolder, "config.yml"));
        config.load();
    }

    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     * 
     * @return
     */
    public File getDataFolder() {
        return dataFolder;
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
     * Returns the main configuration located at
     * <plugin name>/config.yml and loads the file. If the configuration file
     * does not exist and it cannot be loaded, no error will be emitted and
     * the configuration file will have no values.
     * 
     * @return
     */
    public Configuration getConfiguration() {
        return config;
    }

    /**
     * Returns the ClassLoader which holds this plugin
     *
     * @return ClassLoader holding this plugin
     */
    protected ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Sets the enabled state of this plugin
     *
     * @param enabled true if enabled, otherwise false
     */
    protected void setEnabled(final boolean enabled) {
        if (isEnabled != enabled) {
            isEnabled = enabled;

            if (isEnabled) {
                onEnable();
            }  else {
                onDisable();
            }
        }
    }
}
