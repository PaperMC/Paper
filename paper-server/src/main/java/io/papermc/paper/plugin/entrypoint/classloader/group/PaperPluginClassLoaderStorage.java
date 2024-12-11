package io.papermc.paper.plugin.entrypoint.classloader.group;

import io.papermc.paper.plugin.provider.classloader.ClassLoaderAccess;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import io.papermc.paper.plugin.provider.classloader.PaperClassLoaderStorage;
import io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is used for connecting multiple classloaders.
 */
public final class PaperPluginClassLoaderStorage implements PaperClassLoaderStorage {

    private final GlobalPluginClassLoaderGroup globalGroup = new GlobalPluginClassLoaderGroup();
    private final List<PluginClassLoaderGroup> groups = new CopyOnWriteArrayList<>();

    public PaperPluginClassLoaderStorage() {
        this.groups.add(this.globalGroup);
    }

    @Override
    public PluginClassLoaderGroup registerSpigotGroup(PluginClassLoader pluginClassLoader) {
        return this.registerGroup(pluginClassLoader, new SpigotPluginClassLoaderGroup(this.globalGroup, (library) -> {
            return pluginClassLoader.dependencyContext.isTransitiveDependency(pluginClassLoader.getConfiguration(), library.getConfiguration());
        }, pluginClassLoader));
    }

    @Override
    public PluginClassLoaderGroup registerOpenGroup(ConfiguredPluginClassLoader classLoader) {
        return this.registerGroup(classLoader, this.globalGroup);
    }

    @Override
    public PluginClassLoaderGroup registerAccessBackedGroup(ConfiguredPluginClassLoader classLoader, ClassLoaderAccess access) {
        List<ConfiguredPluginClassLoader> allowedLoaders = new ArrayList<>();
        for (ConfiguredPluginClassLoader configuredPluginClassLoader : this.globalGroup.getClassLoaders()) {
            if (access.canAccess(configuredPluginClassLoader)) {
                allowedLoaders.add(configuredPluginClassLoader);
            }
        }

        return this.registerGroup(classLoader, new StaticPluginClassLoaderGroup(allowedLoaders, access, classLoader));
    }

    private PluginClassLoaderGroup registerGroup(ConfiguredPluginClassLoader classLoader, PluginClassLoaderGroup group) {
        // Now add this classloader to any groups that allows it (includes global)
        for (PluginClassLoaderGroup loaderGroup : this.groups) {
            if (loaderGroup.getAccess().canAccess(classLoader)) {
                loaderGroup.add(classLoader);
            }
        }

        group = new LockingClassLoaderGroup(group);
        this.groups.add(group);
        return group;
    }

    @Override
    public void unregisterClassloader(ConfiguredPluginClassLoader configuredPluginClassLoader) {
        this.globalGroup.remove(configuredPluginClassLoader);
        this.groups.remove(configuredPluginClassLoader.getGroup());
        for (PluginClassLoaderGroup group : this.groups) {
            group.remove(configuredPluginClassLoader);
        }
    }

    @Override
    public boolean registerUnsafePlugin(ConfiguredPluginClassLoader pluginLoader) {
        if (this.globalGroup.getClassLoaders().contains(pluginLoader)) {
            return false;
        } else {
            this.globalGroup.add(pluginLoader);
            return true;
        }
    }

    // Debug only
    @ApiStatus.Internal
    public GlobalPluginClassLoaderGroup getGlobalGroup() {
        return this.globalGroup;
    }

    // Debug only
    @ApiStatus.Internal
    public List<PluginClassLoaderGroup> getGroups() {
        return this.groups;
    }
}
