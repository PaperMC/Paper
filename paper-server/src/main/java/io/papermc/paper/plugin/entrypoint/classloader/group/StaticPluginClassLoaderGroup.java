package io.papermc.paper.plugin.entrypoint.classloader.group;

import io.papermc.paper.plugin.provider.classloader.ClassLoaderAccess;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class StaticPluginClassLoaderGroup extends SimpleListPluginClassLoaderGroup {

    private final ClassLoaderAccess access;
    // Debug only
    private final ConfiguredPluginClassLoader mainClassloaderHolder;

    public StaticPluginClassLoaderGroup(List<ConfiguredPluginClassLoader> classloaders, ClassLoaderAccess access, ConfiguredPluginClassLoader mainClassloaderHolder) {
        super(classloaders);
        this.access = access;
        this.mainClassloaderHolder = mainClassloaderHolder;
    }

    @Override
    public ClassLoaderAccess getAccess() {
        return this.access;
    }

    // DEBUG
    @ApiStatus.Internal
    public ConfiguredPluginClassLoader getPluginClassloader() {
        return this.mainClassloaderHolder;
    }

    @Override
    public String toString() {
        return "StaticPluginClassLoaderGroup{" +
            "access=" + this.access +
            ", classloaders=" + this.classloaders +
            '}';
    }
}
