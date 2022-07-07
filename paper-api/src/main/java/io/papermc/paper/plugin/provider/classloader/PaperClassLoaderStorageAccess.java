package io.papermc.paper.plugin.provider.classloader;

import net.kyori.adventure.util.Services;

/**
 * The paper classloader storage access acts as the holder for the server provided implementation of the
 * {@link PaperClassLoaderStorage} interface.
 */
class PaperClassLoaderStorageAccess {

    /**
     * The shared instance of the {@link PaperClassLoaderStorage}, supplied through the {@link java.util.ServiceLoader}
     * by the server.
     */
    static final PaperClassLoaderStorage INSTANCE = Services.service(PaperClassLoaderStorage.class).orElseThrow();

}
