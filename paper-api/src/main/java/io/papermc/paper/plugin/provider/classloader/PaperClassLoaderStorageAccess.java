package io.papermc.paper.plugin.provider.classloader;

import net.kyori.adventure.util.Services;

/// The paper classloader storage access acts as the holder for the server provided implementation of the
/// [PaperClassLoaderStorage] interface.
class PaperClassLoaderStorageAccess {

    /// The shared instance of the [PaperClassLoaderStorage], supplied through the [java.util.ServiceLoader]
    /// by the server.
    static final PaperClassLoaderStorage INSTANCE = Services.service(PaperClassLoaderStorage.class).orElseThrow();

}
