package io.papermc.paper.plugin.loader.library;

import java.nio.file.Path;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a storage that stores library jars.
 * <p>
 * The library store api allows plugins to register specific dependencies into their runtime classloader when their
 * {@link io.papermc.paper.plugin.loader.PluginLoader} is processed.
 *
 * @see io.papermc.paper.plugin.loader.PluginLoader
 */
@ApiStatus.Internal
@NullMarked
public interface LibraryStore {

    /**
     * Adds the provided library path to this library store.
     *
     * @param library path to the libraries jar file on the disk
     */
    void addLibrary(Path library);

}
