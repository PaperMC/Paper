package io.papermc.paper.plugin.manager;

import com.destroystokyo.paper.util.SneakyThrow;
import io.papermc.paper.plugin.entrypoint.Entrypoint;
import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.storage.ProviderStorage;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Used for loading plugins during runtime, only supporting providers that are plugins.
 * This is only used for the plugin manager, as it only allows plugins to be
 * registered to a provider storage.
 */
class RuntimePluginEntrypointHandler<T extends ProviderStorage<JavaPlugin>> implements EntrypointHandler {

    private final T providerStorage;

    RuntimePluginEntrypointHandler(T providerStorage) {
        this.providerStorage = providerStorage;
    }

    @Override
    public <T> void register(Entrypoint<T> entrypoint, PluginProvider<T> provider) {
        if (!entrypoint.equals(Entrypoint.PLUGIN)) {
            SneakyThrow.sneaky(new InvalidPluginException("Plugin cannot register entrypoints other than PLUGIN during runtime. Tried registering %s!".formatted(entrypoint)));
            // We have to throw an invalid plugin exception for legacy reasons
        }

        this.providerStorage.register((PluginProvider<JavaPlugin>) provider);
    }

    @Override
    public void enter(Entrypoint<?> entrypoint) {
        if (entrypoint != Entrypoint.PLUGIN) {
            throw new IllegalArgumentException("Only plugin entrypoint supported");
        }
        this.providerStorage.enter();
    }

    @NotNull
    public T getPluginProviderStorage() {
        return this.providerStorage;
    }
}
