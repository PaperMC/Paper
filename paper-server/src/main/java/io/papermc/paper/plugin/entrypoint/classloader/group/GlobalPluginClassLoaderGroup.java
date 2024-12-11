package io.papermc.paper.plugin.entrypoint.classloader.group;

import io.papermc.paper.plugin.provider.classloader.ClassLoaderAccess;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class GlobalPluginClassLoaderGroup extends SimpleListPluginClassLoaderGroup {

    @Override
    public ClassLoaderAccess getAccess() {
        return (v) -> true;
    }

    @Override
    public String toString() {
        return "GLOBAL:" + super.toString();
    }
}
