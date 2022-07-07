package io.papermc.paper.plugin.loader;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A plugin loader is responsible for creating certain aspects of a plugin before it is created.
 * <p>
 * The goal of the plugin loader is the creation of an expected/dynamic environment for the plugin to load into.
 * This, as of right now, only applies to creating the expected classpath for the plugin, e.g. supplying external
 * libraries to the plugin.
 * <p>
 * It should be noted that this class will be called from a different classloader, this will cause any static values
 * set in this class/any other classes loaded not to persist when the plugin loads.
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.OverrideOnly
public interface PluginLoader {

    /**
     * Called by the server to allows plugins to configure the runtime classpath that the plugin is run on.
     * This allows plugin loaders to configure dependencies for the plugin where jars can be downloaded or
     * provided during runtime.
     *
     * @param classpathBuilder a mutable classpath builder that may be used to register custom runtime dependencies
     *                         for the plugin the loader was registered for.
     */
    void classloader(PluginClasspathBuilder classpathBuilder);

}
