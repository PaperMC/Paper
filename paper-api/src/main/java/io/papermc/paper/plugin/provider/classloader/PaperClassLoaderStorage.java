package io.papermc.paper.plugin.provider.classloader;

import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.ApiStatus;

/**
 * The plugin classloader storage is an <b>internal</b> type that is used to manage existing classloaders on the server.
 * <p>
 * The paper classloader storage is also responsible for storing added {@link ConfiguredPluginClassLoader}s into
 * {@link PluginClassLoaderGroup}s, via {@link #registerOpenGroup(ConfiguredPluginClassLoader)},
 * {@link #registerSpigotGroup(PluginClassLoader)} and {@link
 * #registerAccessBackedGroup(ConfiguredPluginClassLoader, ClassLoaderAccess)}.
 * <p>
 * Groups are differentiated into the global group or plugin owned groups.
 * <ul>
 * <li>The global group holds all registered class loaders and merely exists to maintain backwards compatibility with
 * spigots legacy classloader handling.</li>
 * <li>The plugin groups only contains the classloaders that each plugin has access to and hence serves to properly
 * separates unrelated classloaders.</li>
 * </ul>
 */
@ApiStatus.Internal
public interface PaperClassLoaderStorage {

    /**
     * Access to the shared instance of the {@link PaperClassLoaderStorageAccess}.
     *
     * @return the singleton instance of the {@link PaperClassLoaderStorage} used throughout the server
     */
    static PaperClassLoaderStorage instance() {
        return PaperClassLoaderStorageAccess.INSTANCE;
    }

    /**
     * Registers a legacy spigot {@link PluginClassLoader} into the loader storage, creating a group wrapping
     * the single plugin class loader with transitive access to the global group.
     *
     * @param pluginClassLoader the legacy spigot plugin class loader to register
     * @return the group the plugin class loader was placed into
     */
    PluginClassLoaderGroup registerSpigotGroup(PluginClassLoader pluginClassLoader);

    /**
     * Registers a paper configured plugin classloader into a new open group, with full access to the global
     * plugin class loader group.
     * <p>
     * This method hence allows the configured plugin class loader to access all other class loaders registered in this
     * storage.
     *
     * @param classLoader the configured plugin class loader to register
     * @return the group the plugin class loader was placed into
     */
    PluginClassLoaderGroup registerOpenGroup(ConfiguredPluginClassLoader classLoader);

    /**
     * Registers a paper configured classloader into a new, access backed group.
     * The access backed classloader group, different from an open group, only has access to the classloaders
     * the passed {@link ClassLoaderAccess} grants access to.
     *
     * @param classLoader the configured plugin class loader to register
     * @param access      the class loader access that defines what other classloaders the passed plugin class loader
     *                    should be granted access to.
     * @return the group the plugin class loader was placed into.
     */
    PluginClassLoaderGroup registerAccessBackedGroup(ConfiguredPluginClassLoader classLoader, ClassLoaderAccess access);

    /**
     * Unregisters a configured class loader from this storage.
     * This removes the passed class loaders from any group it may have been a part of, including the global group.
     * <p>
     * Note: this method is <b>highly</b> discouraged from being used, as mutation of the classloaders at runtime
     * is not encouraged
     *
     * @param configuredPluginClassLoader the class loader to remove from this storage.
     */
    void unregisterClassloader(ConfiguredPluginClassLoader configuredPluginClassLoader);

    /**
     * Registers a configured plugin class loader directly into the global group without adding it to
     * any existing groups.
     * <p>
     * Note: this method unsafely injects the plugin classloader directly into the global group, which bypasses the
     * group structure paper's plugin API introduced. This method should hence be used with caution.
     *
     * @param pluginLoader the configured plugin classloader instance that should be registered directly into the global
     *                     group.
     * @return a simple boolean flag, {@code true} if the classloader was registered or {@code false} if the classloader
     * was already part of the global group.
     */
    boolean registerUnsafePlugin(ConfiguredPluginClassLoader pluginLoader);

}
