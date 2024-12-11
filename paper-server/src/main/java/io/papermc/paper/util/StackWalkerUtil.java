package io.papermc.paper.util;

import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class StackWalkerUtil {

    @Nullable
    public static JavaPlugin getFirstPluginCaller() {
        Optional<JavaPlugin> foundFrame = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(stream -> stream
                .map((frame) -> {
                    ClassLoader classLoader =  frame.getDeclaringClass().getClassLoader();
                    JavaPlugin plugin;
                    if (classLoader instanceof ConfiguredPluginClassLoader configuredPluginClassLoader) {
                        plugin = configuredPluginClassLoader.getPlugin();
                    } else {
                        plugin = null;
                    }

                    return plugin;
                })
                .filter(Objects::nonNull)
                .findFirst());

        return foundFrame.orElse(null);
    }
}
