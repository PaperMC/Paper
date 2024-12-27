package io.papermc.paper.plugin.entrypoint;

import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.storage.BootstrapProviderStorage;
import io.papermc.paper.plugin.storage.ProviderStorage;
import io.papermc.paper.plugin.storage.ServerPluginProviderStorage;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

/**
 * Used by the server to register/load plugin bootstrappers and plugins.
 */
public class LaunchEntryPointHandler implements EntrypointHandler {

    public static final LaunchEntryPointHandler INSTANCE = new LaunchEntryPointHandler();
    private final Map<Entrypoint<?>, ProviderStorage<?>> storage = new HashMap<>();
    private final Object2BooleanMap<Entrypoint<?>> enteredMap = new Object2BooleanOpenHashMap<>();

    LaunchEntryPointHandler() {
        this.populateProviderStorage();
        this.enteredMap.defaultReturnValue(false);
    }

    // Utility
    public static void enterBootstrappers() {
        LaunchEntryPointHandler.INSTANCE.enter(Entrypoint.BOOTSTRAPPER);
    }

    @Override
    public void enter(Entrypoint<?> entrypoint) {
        ProviderStorage<?> storage = this.storage.get(entrypoint);
        if (storage == null) {
            throw new IllegalArgumentException("No storage registered for entrypoint %s.".formatted(entrypoint));
        }

        storage.enter();
        this.enteredMap.put(entrypoint, true);
    }

    @Override
    public <T> void register(Entrypoint<T> entrypoint, PluginProvider<T> provider) {
        ProviderStorage<T> storage = this.get(entrypoint);
        if (storage == null) {
            throw new IllegalArgumentException("No storage registered for entrypoint %s.".formatted(entrypoint));
        }

        storage.register(provider);
    }

    @SuppressWarnings("unchecked")
    public <T> ProviderStorage<T> get(Entrypoint<T> entrypoint) {
        return (ProviderStorage<T>) this.storage.get(entrypoint);
    }

    // Debug only
    @ApiStatus.Internal
    public Map<Entrypoint<?>, ProviderStorage<?>> getStorage() {
        return storage;
    }

    public boolean hasEntered(Entrypoint<?> entrypoint) {
        return this.enteredMap.getBoolean(entrypoint);
    }

    // Reload only
    public void populateProviderStorage() {
        this.storage.put(Entrypoint.BOOTSTRAPPER, new BootstrapProviderStorage());
        this.storage.put(Entrypoint.PLUGIN, new ServerPluginProviderStorage());
    }
}
