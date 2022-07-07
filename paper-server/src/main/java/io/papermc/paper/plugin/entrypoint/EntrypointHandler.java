package io.papermc.paper.plugin.entrypoint;

import io.papermc.paper.plugin.provider.PluginProvider;

/**
 * Represents a register that will register providers at a certain {@link Entrypoint},
 * where then when the given {@link Entrypoint} is registered those will be loaded.
 */
public interface EntrypointHandler {

    <T> void register(Entrypoint<T> entrypoint, PluginProvider<T> provider);

    void enter(Entrypoint<?> entrypoint);
}
