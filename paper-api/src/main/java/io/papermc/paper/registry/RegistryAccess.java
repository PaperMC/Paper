package io.papermc.paper.registry;

import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Used for accessing different {@link Registry} instances
 * by a {@link RegistryKey}. Get the main instance of {@link RegistryAccess}
 * with {@link RegistryAccess#registryAccess()}.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface RegistryAccess {

    /**
     * Get the {@link RegistryAccess} instance for the server.
     *
     * @return the RegistryAccess instance
     */
    static RegistryAccess registryAccess() {
        return RegistryAccessHolder.INSTANCE.orElseThrow(() -> new IllegalStateException("No RegistryAccess implementation found"));
    }

    /**
     * Gets the registry based on the type.
     *
     * @param type the type
     * @return the registry or null if none found
     * @param <T> the type
     * @deprecated use {@link #getRegistry(RegistryKey)} with keys from {@link RegistryKey}
     */
    @Deprecated(since = "1.20.6", forRemoval = true)
    <T extends Keyed> @Nullable Registry<T> getRegistry(Class<T> type);

    /**
     * Gets the registry with the specified key.
     *
     * @param registryKey the key
     * @return the registry
     * @param <T> the type
     * @throws java.util.NoSuchElementException if no registry with the key is found
     * @throws IllegalArgumentException if the registry is not available yet
     */
    // Future note: We should have no trouble removing this generic qualifier when
    // registry types no longer have to be "keyed" as it shouldn't break ABI or API.
    <T extends Keyed> Registry<T> getRegistry(RegistryKey<T> registryKey);
}
