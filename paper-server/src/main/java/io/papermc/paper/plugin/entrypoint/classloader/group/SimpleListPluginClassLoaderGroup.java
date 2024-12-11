package io.papermc.paper.plugin.entrypoint.classloader.group;

import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApiStatus.Internal
public abstract class SimpleListPluginClassLoaderGroup implements PluginClassLoaderGroup {

    private static final boolean DISABLE_CLASS_PRIORITIZATION = Boolean.getBoolean("Paper.DisableClassPrioritization");

    protected final List<ConfiguredPluginClassLoader> classloaders;

    protected SimpleListPluginClassLoaderGroup() {
        this(new CopyOnWriteArrayList<>());
    }

    protected SimpleListPluginClassLoaderGroup(List<ConfiguredPluginClassLoader> classloaders) {
        this.classloaders = classloaders;
    }

    @Override
    public @Nullable Class<?> getClassByName(String name, boolean resolve, ConfiguredPluginClassLoader requester) {
        if (!DISABLE_CLASS_PRIORITIZATION) {
            try {
                return this.lookupClass(name, false, requester); // First check the requester
            } catch (ClassNotFoundException ignored) {
            }
        }

        for (ConfiguredPluginClassLoader loader : this.classloaders) {
            try {
                return this.lookupClass(name, resolve, loader);
            } catch (ClassNotFoundException ignored) {
            }
        }

        return null;
    }

    protected Class<?> lookupClass(String name, boolean resolve, ConfiguredPluginClassLoader current) throws ClassNotFoundException {
        return current.loadClass(name, resolve, false, true);
    }

    @Override
    public void remove(ConfiguredPluginClassLoader configuredPluginClassLoader) {
        this.classloaders.remove(configuredPluginClassLoader);
    }

    @Override
    public void add(ConfiguredPluginClassLoader configuredPluginClassLoader) {
        this.classloaders.add(configuredPluginClassLoader);
    }

    public List<ConfiguredPluginClassLoader> getClassLoaders() {
        return classloaders;
    }

    @Override
    public String toString() {
        return "SimpleListPluginClassLoaderGroup{" +
            "classloaders=" + this.classloaders +
            '}';
    }
}
