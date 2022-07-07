package io.papermc.paper.plugin.loader;

import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.loader.library.ClassPathLibrary;
import io.papermc.paper.plugin.loader.library.LibraryStore;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * A mutable builder that may be used to collect and register all {@link ClassPathLibrary} instances a
 * {@link PluginLoader} aims to provide to its plugin at runtime.
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface PluginClasspathBuilder {

    /**
     * Adds a new classpath library to this classpath builder.
     * <p>
     * As a builder, this method does not invoke {@link ClassPathLibrary#register(LibraryStore)} and
     * may hence be run without invoking potential IO performed by a {@link ClassPathLibrary} during resolution.
     * <p>
     * The paper api provides pre implemented {@link ClassPathLibrary} types that allow easy inclusion of existing
     * libraries on disk or on remote maven repositories.
     *
     * @param classPathLibrary the library instance to add to this builder
     * @return self
     * @see io.papermc.paper.plugin.loader.library.impl.JarLibrary
     * @see io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
     */
    @Contract("_ -> this")
    PluginClasspathBuilder addLibrary(ClassPathLibrary classPathLibrary);

    PluginProviderContext getContext();
}
