package org.bukkit.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;
import net.kyori.adventure.key.Namespaced;
import org.bukkit.Server;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Plugin
 * <p>
 * The use of {@link PluginBase} is recommended for actual Implementation
 */
public interface Plugin extends TabExecutor, io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner, Namespaced { // Paper
    /**
     * Returns the folder that the plugin data files are located in. The
     * folder may not yet exist.
     *
     * @return The folder
     */
    @NotNull
    public File getDataFolder();

    // Paper start - add getDataPath()
    /**
     * Returns the path that the plugin data files are located in.
     * The folder may not yet exist.
     *
     * @see #getDataFolder()
     * @return The folder
     */
    default @NotNull java.nio.file.Path getDataPath() {
        return getDataFolder().toPath();
    }
    // Paper end - add getDataPath()

    /**
     * Returns the plugin.yml file containing the details for this plugin
     *
     * @return Contents of the plugin.yml file
     * @deprecated May be inaccurate due to different plugin implementations.
     * @see Plugin#getPluginMeta()
     */
    @Deprecated // Paper
    @NotNull
    public PluginDescriptionFile getDescription();

    // Paper start
    /**
     * Gets the plugin meta for this plugin.
     * @return configuration
     */
    @NotNull
    io.papermc.paper.plugin.configuration.PluginMeta getPluginMeta();
    // Paper end
    /**
     * Gets a {@link FileConfiguration} for this plugin, read through
     * "config.yml"
     * <p>
     * If there is a default config.yml embedded in this plugin, it will be
     * provided as a default for this Configuration.
     *
     * @return Plugin configuration
     */
    @NotNull
    public FileConfiguration getConfig();

    /**
     * Gets an embedded resource in this plugin
     *
     * @param filename Filename of the resource
     * @return File if found, otherwise null
     */
    @Nullable
    public InputStream getResource(@NotNull String filename);

    /**
     * Saves the {@link FileConfiguration} retrievable by {@link #getConfig()}.
     */
    public void saveConfig();

    /**
     * Saves the raw contents of the default config.yml file to the location
     * retrievable by {@link #getConfig()}.
     * <p>
     * This should fail silently if the config.yml already exists.
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
    public void saveResource(@NotNull String resourcePath, boolean replace);

    /**
     * Discards any data in {@link #getConfig()} and reloads from disk.
     */
    public void reloadConfig();

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    @Deprecated(forRemoval = true) // Paper - The PluginLoader system will not function in the near future
    @NotNull
    public PluginLoader getPluginLoader();

    /**
     * Returns the Server instance currently running this plugin
     *
     * @return Server running this plugin
     */
    @NotNull
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
     * When multiple plugins are loaded, the onLoad() for all plugins is
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
     * Gets a {@link ChunkGenerator} for use in a default world, as specified
     * in the server configuration
     *
     * @param worldName Name of the world that this will be applied to
     * @param id Unique ID, if any, that was specified to indicate which
     *     generator was requested
     * @return ChunkGenerator for use in the default world generation
     */
    @Nullable
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id);

    /**
     * Gets a {@link BiomeProvider} for use in a default world, as specified
     * in the server configuration
     *
     * @param worldName Name of the world that this will be applied to
     * @param id Unique ID, if any, that was specified to indicate which
     *     biome provider was requested
     * @return BiomeProvider for use in the default world generation
     */
    @Nullable
    public BiomeProvider getDefaultBiomeProvider(@NotNull String worldName, @Nullable String id);

    /**
     * Returns the plugin logger associated with this server's logger. The
     * returned logger automatically tags all log messages with the plugin's
     * name.
     *
     * @return Logger associated with this plugin
     */
    @NotNull
    public Logger getLogger();

    // Paper start - Adventure component logger
    @NotNull
    default net.kyori.adventure.text.logger.slf4j.ComponentLogger getComponentLogger() {
        return net.kyori.adventure.text.logger.slf4j.ComponentLogger.logger(getLogger().getName());
    }
    // Paper end

    // Paper start - Add SLF4J/Log4J loggers
    @NotNull
    default org.slf4j.Logger getSLF4JLogger() {
        return org.slf4j.LoggerFactory.getLogger(getLogger().getName());
    }

    /**
     * @deprecated use {@link #getSLF4JLogger()}
     */
    @Deprecated
    @NotNull
    default org.apache.logging.log4j.Logger getLog4JLogger() {
        return org.apache.logging.log4j.LogManager.getLogger(getLogger().getName());
    }
    // Paper end

    /**
     * Returns the name of the plugin.
     * <p>
     * This should return the bare name of the plugin and should be used for
     * comparison.
     *
     * @return name of the plugin
     */
    @NotNull
    public String getName();

    // Paper start - lifecycle events
    /**
     * Get the lifecycle event manager for registering handlers
     * for lifecycle events allowed on the {@link Plugin}.
     *
     * @return the lifecycle event manager
     */
    io.papermc.paper.plugin.lifecycle.event.@NotNull LifecycleEventManager<Plugin> getLifecycleManager();
    // Paper end - lifecycle events
}
