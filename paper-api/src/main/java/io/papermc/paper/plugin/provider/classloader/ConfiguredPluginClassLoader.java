package io.papermc.paper.plugin.provider.classloader;

import io.papermc.paper.plugin.configuration.PluginMeta;
import java.io.Closeable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The configured plugin class loader represents an <b>internal</b> abstraction over the classloaders used by the server
 * to load and access a plugins classes during runtime.
 * <p>
 * It implements {@link Closeable} to define the ability to shutdown and close the classloader that implements this
 * interface.
 */
@NullMarked
@ApiStatus.Internal
public interface ConfiguredPluginClassLoader extends Closeable {

    /**
     * Provides the configuration of the plugin that this plugin classloader provides type access to.
     *
     * @return the plugin meta instance, holding all meta information about the plugin instance.
     */
    PluginMeta getConfiguration();

    /**
     * Attempts to load a class from this plugin class loader using the passed fully qualified name.
     * This lookup logic can be configured through the following parameters to define how wide or how narrow the
     * class lookup should be.
     *
     * @param name           the fully qualified name of the class to load
     * @param resolve        whether the class should be resolved if needed or not
     * @param checkGlobal    whether this lookup should check transitive dependencies, including either the legacy spigot
     *                       global class loader or the paper {@link PluginClassLoaderGroup}
     * @param checkLibraries whether the defined libraries should be checked for the class or not
     * @return the class found at the fully qualified class name passed under the passed restrictions
     * @throws ClassNotFoundException if the class could not be found considering the passed restrictions
     * @see ClassLoader#loadClass(String)
     * @see Class#forName(String, boolean, ClassLoader)
     */
    Class<?> loadClass(String name,
                       boolean resolve,
                       boolean checkGlobal,
                       boolean checkLibraries) throws ClassNotFoundException;

    /**
     * Initializes both this configured plugin class loader and the java plugin passed to link to each other.
     * This logic is to be called exactly once when the initial setup between the class loader and the instantiated
     * {@link JavaPlugin} is loaded.
     *
     * @param plugin the {@link JavaPlugin} that should be interlinked with this class loader.
     */
    void init(JavaPlugin plugin);

    /**
     * Gets the plugin held by this class loader.
     *
     * @return the plugin or null if it doesn't exist yet
     */
    @Nullable JavaPlugin getPlugin();

    /**
     * Get the plugin classloader group
     * that is used by the underlying classloader
     * @return classloader
     */
    @Nullable PluginClassLoaderGroup getGroup();
}
