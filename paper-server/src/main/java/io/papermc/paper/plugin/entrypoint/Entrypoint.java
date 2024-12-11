package io.papermc.paper.plugin.entrypoint;

import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Used to mark a certain place that {@link EntrypointHandler} will register {@link io.papermc.paper.plugin.provider.PluginProvider} under.
 * Used for loading only certain providers at a certain time.
 * @param <T> provider type
 */
public final class Entrypoint<T> {

    public static final Entrypoint<PluginBootstrap> BOOTSTRAPPER = new Entrypoint<>("bootstrapper");
    public static final Entrypoint<JavaPlugin> PLUGIN = new Entrypoint<>("plugin");

    private final String debugName;

    private Entrypoint(String debugName) {
        this.debugName = debugName;
    }

    public String getDebugName() {
        return debugName;
    }
}
