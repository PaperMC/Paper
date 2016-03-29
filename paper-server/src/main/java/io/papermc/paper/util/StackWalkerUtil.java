package io.papermc.paper.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StackWalkerUtil {

    @Nullable
    public static JavaPlugin getFirstPluginCaller() {
        Optional<JavaPlugin> foundFrame = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(stream -> stream
                .filter(frame -> frame.getDeclaringClass().getClassLoader() instanceof PluginClassLoader)
                .map((frame) -> {
                    PluginClassLoader classLoader = (PluginClassLoader) frame.getDeclaringClass().getClassLoader();
                    return classLoader.getPlugin();
                })
                .findFirst());

        return foundFrame.orElse(null);
    }
}
