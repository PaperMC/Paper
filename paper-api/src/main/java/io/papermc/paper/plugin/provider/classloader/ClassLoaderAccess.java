package io.papermc.paper.plugin.provider.classloader;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * The class loader access interface is an <b>internal</b> representation of a class accesses' ability to see types
 * from other {@link ConfiguredPluginClassLoader}.
 * <p>
 * An example of this would be a class loader access representing a plugin. The class loader access in that case would
 * only return {@code true} on calls for {@link #canAccess(ConfiguredPluginClassLoader)} if the passed class loader
 * is owned by a direct or transitive dependency of the plugin, preventing the plugin for accidentally discovering and
 * using class types that are supplied by plugins/libraries the plugin did not actively define as a dependency.
 */
@NullMarked
@ApiStatus.Internal
public interface ClassLoaderAccess {

    /**
     * Evaluates if this class loader access is allowed to access types provided by the passed {@link
     * ConfiguredPluginClassLoader}.
     * <p>
     * This interface method does not offer any further contracts on the interface level, as the logic to determine
     * what class loaders this class loader access is allowed to retrieve types from depends heavily on the type of
     * access.
     * Legacy spigot types for example may access any class loader available on the server, while modern paper plugins
     * are properly limited to their dependency tree.
     *
     * @param classLoader the class loader for which access should be evaluated
     * @return a plain boolean flag, {@code true} indicating that this class loader access is allowed to access types
     * from the passed configured plugin class loader, {@code false} indicating otherwise.
     */
    boolean canAccess(ConfiguredPluginClassLoader classLoader);

}
