package io.papermc.paper.plugin.entrypoint.classloader.group;

import io.papermc.paper.plugin.provider.classloader.ClassLoaderAccess;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

/**
 * Spigot classloaders have the ability to see everything.
 * However, libraries are ONLY shared depending on their dependencies.
 */
@ApiStatus.Internal
public class SpigotPluginClassLoaderGroup extends SimpleListPluginClassLoaderGroup {

    private final Predicate<ConfiguredPluginClassLoader> libraryClassloaderPredicate;
    private final PluginClassLoader pluginClassLoader;

    public SpigotPluginClassLoaderGroup(GlobalPluginClassLoaderGroup globalPluginClassLoaderGroup, Predicate<ConfiguredPluginClassLoader> libraryClassloaderPredicate, PluginClassLoader pluginClassLoader) {
        super(globalPluginClassLoaderGroup.getClassLoaders());
        this.libraryClassloaderPredicate = libraryClassloaderPredicate;
        this.pluginClassLoader = pluginClassLoader;
    }

    // Mirrors global list
    @Override
    public void add(ConfiguredPluginClassLoader configuredPluginClassLoader) {
    }

    @Override
    public void remove(ConfiguredPluginClassLoader configuredPluginClassLoader) {
    }

    // Don't allow other plugins to access spigot dependencies, they should instead reference the global list
    @Override
    public ClassLoaderAccess getAccess() {
        return v -> false;
    }

    @Override
    protected Class<?> lookupClass(String name, boolean resolve, ConfiguredPluginClassLoader current) throws ClassNotFoundException {
        return current.loadClass(name, resolve, false, this.libraryClassloaderPredicate.test(current));
    }

    // DEBUG
    public PluginClassLoader getPluginClassLoader() {
        return pluginClassLoader;
    }

    @Override
    public String toString() {
        return "SpigotPluginClassLoaderGroup{" +
            "libraryClassloaderPredicate=" + this.libraryClassloaderPredicate +
            ", classloaders=" + this.classloaders +
            '}';
    }
}
