package org.bukkit.plugin;

import com.avaje.ebean.EbeanServer;
import java.io.File;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.config.Configuration;

/**
 * Represents a Plugin
 */
public interface Plugin extends CommandExecutor {

    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder
     */
    public File getDataFolder();

    /**
     * Returns the plugin.yaml file containing the details for this plugin
     *
     * @return Contents of the plugin.yaml file
     */
    public PluginDescriptionFile getDescription();

    /**
     * Returns the main configuration file. It should be loaded.
     *
     * @return The configuration
     */
    public Configuration getConfiguration();

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    public PluginLoader getPluginLoader();

    /**
     * Returns the Server instance currently running this plugin
     *
     * @return Server running this plugin
     */
    public Server getServer();

    /**
     * Returns a value indicating whether or not this plugin is currently enabled
     *
     * @return true if this plugin is enabled, otherwise false
     */
    public boolean isEnabled();

    /**
     * Called when this plugin is disabled
     */
    public void onDisable();

    /**
     * Called after a plugin is loaded but before it has been enabled.
     * When mulitple plugins are loaded, the onLoad() for all plugins is called before any onEnable() is called.
     */
    public void onLoad();

    /**
     * Called when this plugin is enabled
     */
    public void onEnable();

    /**
     * Simple boolean if we can still nag to the logs about things
     * @return boolean whether we can nag
     */
    public boolean isNaggable();

    /**
     * Set naggable state
     * @param canNag is this plugin still naggable?
     */
    public void setNaggable(boolean canNag);

    /**
     * Gets the {@link EbeanServer} tied to this plugin
     *
     * @return Ebean server instance
     */
    public EbeanServer getDatabase();

    /**
     * Gets a {@link ChunkGenerator} for use in a default world, as specified in the server configuration
     *
     * @param worldName Name of the world that this will be applied to
     * @param id Unique ID, if any, that was specified to indicate which generator was requested
     * @return ChunkGenerator for use in the default world generation
     */
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id);
}
