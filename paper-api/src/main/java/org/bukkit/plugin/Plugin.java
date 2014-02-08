package org.bukkit.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

import com.avaje.ebean.EbeanServer;

/**
 * Represents a Plugin
 * <p>
 * The use of {@link PluginBase} is recommended for actual Implementation
 */
public interface Plugin extends TabExecutor {
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
     * Gets a {@link FileConfiguration} for this plugin, read through
     * "config.yml"
     * <p>
     * If there is a default config.yml embedded in this plugin, it will be
     * provided as a default for this Configuration.
     *
     * @return Plugin configuration
     */
    public FileConfiguration getConfig();

    /**
     * Gets an embedded resource in this plugin
     *
     * @param filename Filename of the resource
     * @return File if found, otherwise null
     */
    public InputStream getResource(String filename);

    /**
     * Saves the {@link FileConfiguration} retrievable by {@link #getConfig()}.
     */
    public void saveConfig();

    /**
     * Saves the raw contents of the default config.yml file to the location
     * retrievable by {@link #getConfig()}. If there is no default config.yml
     * embedded in the plugin, an empty config.yml file is saved. This should
     * fail silently if the config.yml already exists.
     */
    public void saveDefaultConfig();

    /**
     * Saves the raw contents of any resource embedded with a plugin's .jar
     * file assuming it can be found using {@link #getResource(String)}.
     * <p>
     * The resource is saved into the plugin's data folder using the same
     * hierarchy as the .jar file (subdirectories are preserved).
     *
     * @param resourcePath the embedded resource path to look for within the
     *     plugin's .jar file. (No preceding slash).
     * @param replace if true, the embedded resource will overwrite the
     *     contents of an existing file.
     * @throws IllegalArgumentException if the resource path is null, empty,
     *     or points to a nonexistent resource.
     */
    public void saveResource(String resourcePath, boolean replace);

    /**
     * Discards any data in {@link #getConfig()} and reloads from disk.
     */
    public void reloadConfig();

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
     * Returns a value indicating whether or not this plugin is currently
     * enabled
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
     * <p>
     * When mulitple plugins are loaded, the onLoad() for all plugins is
     * called before any onEnable() is called.
     */
    public void onLoad();

    /**
     * Called when this plugin is enabled
     */
    public void onEnable();

    /**
     * Simple boolean if we can still nag to the logs about things
     *
     * @return boolean whether we can nag
     */
    public boolean isNaggable();

    /**
     * Set naggable state
     *
     * @param canNag is this plugin still naggable?
     */
    public void setNaggable(boolean canNag);

    /**
     * Gets the {@link EbeanServer} tied to this plugin. This will only be
     * available if enabled in the {@link
     * PluginDescriptionFile#isDatabaseEnabled()}
     * <p>
     * <i>For more information on the use of <a href="http://www.avaje.org/">
     * Avaje Ebeans ORM</a>, see <a
     * href="http://www.avaje.org/ebean/documentation.html">Avaje Ebeans
     * Documentation</a></i>
     * <p>
     * <i>For an example using Ebeans ORM, see <a
     * href="https://github.com/Bukkit/HomeBukkit">Bukkit's Homebukkit Plugin
     * </a></i>
     *
     * @return ebean server instance or null if not enabled
     */
    public EbeanServer getDatabase();

    /**
     * Gets a {@link ChunkGenerator} for use in a default world, as specified
     * in the server configuration
     *
     * @param worldName Name of the world that this will be applied to
     * @param id Unique ID, if any, that was specified to indicate which
     *     generator was requested
     * @return ChunkGenerator for use in the default world generation
     */
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id);

    /**
     * Returns the plugin logger associated with this server's logger. The
     * returned logger automatically tags all log messages with the plugin's
     * name.
     *
     * @return Logger associated with this plugin
     */
    public Logger getLogger();

    /**
     * Returns the name of the plugin.
     * <p>
     * This should return the bare name of the plugin and should be used for
     * comparison.
     *
     * @return name of the plugin
     */
    public String getName();
}
