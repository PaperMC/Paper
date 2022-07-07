package io.papermc.paper.plugin.loader.library.impl;

import io.papermc.paper.plugin.loader.library.ClassPathLibrary;
import io.papermc.paper.plugin.loader.library.LibraryLoadingException;
import io.papermc.paper.plugin.loader.library.LibraryStore;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

/**
 * A simple jar library implementation of the {@link ClassPathLibrary} that allows {@link io.papermc.paper.plugin.loader.PluginLoader}s to
 * append a jar stored on the local file system into their runtime classloader.
 * <p>
 * An example creation of the jar library type may look like this:
 * <pre>{@code
 *   final JarLibrary customLibrary = new JarLibrary(Path.of("libs/custom-library-1.24.jar"));
 * }</pre>
 * resulting in a jar library that provides the jar at {@code libs/custom-library-1.24.jar} to the plugins classloader
 * at runtime.
 * <p>
 * The jar library implementation will error if the file does not exist at the specified path.
 */
@NullMarked
public class JarLibrary implements ClassPathLibrary {

    private final Path path;

    /**
     * Creates a new jar library that references the jar file found at the provided path.
     *
     * @param path the path, relative to the JVMs start directory.
     */
    public JarLibrary(final Path path) {
        this.path = path;
    }

    @Override
    public void register(final LibraryStore store) throws LibraryLoadingException {
        if (Files.notExists(this.path)) {
            throw new LibraryLoadingException("Could not find library at " + this.path);
        }

        store.addLibrary(this.path);
    }
}
