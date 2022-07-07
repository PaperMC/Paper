package io.papermc.paper.plugin.provider.classloader;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A plugin classloader group represents a group of classloaders that a plugins classloader may access.
 * <p>
 * An example of this would be a classloader group that holds all direct and transitive dependencies a plugin declared,
 * allowing a plugins classloader to access classes included in these dependencies via this group.
 */
@NullMarked
@ApiStatus.Internal
public interface PluginClassLoaderGroup {

    /**
     * Attempts to find/load a class from this plugin class loader group using the passed fully qualified name
     * in any of the classloaders that are part of this group.
     * <p>
     * The lookup order across the contained loaders is not defined on the API level and depends purely on the
     * implementation.
     *
     * @param name      the fully qualified name of the class to load
     * @param resolve   whether the class should be resolved if needed or not
     * @param requester plugin classloader that is requesting the class from this loader group
     * @return the class found at the fully qualified class name passed. If the class could not be found, {@code null}
     * will be returned.
     * @see ConfiguredPluginClassLoader#loadClass(String, boolean, boolean, boolean)
     */
    @Nullable Class<?> getClassByName(String name, boolean resolve, ConfiguredPluginClassLoader requester);

    /**
     * Removes a configured plugin classloader from this class loader group.
     * If the classloader is not currently in the list, this method will simply do nothing.
     *
     * @param configuredPluginClassLoader the plugin classloader to remove from the group
     */
    @Contract(mutates = "this")
    void remove(ConfiguredPluginClassLoader configuredPluginClassLoader);

    /**
     * Adds the passed plugin classloader to this group, allowing this group to use it during
     * {@link #getClassByName(String, boolean, ConfiguredPluginClassLoader)} lookups.
     * <p>
     * This method does <b>not</b> query the {@link ClassLoaderAccess} (exposed via {@link #getAccess()}) to ensure
     * if this group has access to the class loader passed.
     *
     * @param configuredPluginClassLoader the plugin classloader to add to this group.
     */
    @Contract(mutates = "this")
    void add(ConfiguredPluginClassLoader configuredPluginClassLoader);

    /**
     * Provides the class loader access that guards and defines the content of this classloader group.
     * While not guaranteed contractually (see {@link #add(ConfiguredPluginClassLoader)}), the access generally is
     * responsible for defining which {@link ConfiguredPluginClassLoader}s should be part of this group and which ones
     * should not.
     *
     * @return the classloader access governing which classloaders should be part of this group and which ones should
     * not.
     */
    ClassLoaderAccess getAccess();

}
