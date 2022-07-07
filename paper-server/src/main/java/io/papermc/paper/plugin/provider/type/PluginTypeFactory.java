package io.papermc.paper.plugin.provider.type;

import io.papermc.paper.plugin.configuration.PluginMeta;

import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A plugin type factory is responsible for building an object
 * and config for a certain plugin type.
 *
 * @param <T> plugin provider type (may not be a plugin provider)
 * @param <C> config type
 */
public interface PluginTypeFactory<T, C extends PluginMeta> {

    T build(JarFile file, C configuration, Path source) throws Exception;

    C create(JarFile file, JarEntry config) throws Exception;
}
