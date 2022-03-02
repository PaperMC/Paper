package io.papermc.paper.registry;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a reference to a server-backed registry value that may
 * change.
 *
 * @param <T> type of the value
 */
@Deprecated(forRemoval = true, since = "1.20.6")
public interface Reference<T extends Keyed> extends Keyed {

    /**
     * Gets the value from the registry with the key.
     *
     * @return the value
     * @throws java.util.NoSuchElementException if there is no value with this key
     */
    @Deprecated(forRemoval = true, since = "1.20.6")
    @NotNull T value();

    /**
     * Gets the value from the registry with the key.
     *
     * @return the value or null if it doesn't exist
     */
    @Deprecated(forRemoval = true, since = "1.20.6")
    @Nullable T valueOrNull();

    /**
     * Creates a reference to a registered value.
     *
     * @param registry the registry the value is located in
     * @param key the key to the value
     * @param <T> the type of the value
     * @return a reference
     */
    @Deprecated(forRemoval = true, since = "1.20.6")
    static <T extends Keyed> @NotNull Reference<T> create(@NotNull Registry<T> registry, @NotNull NamespacedKey key) {
        return new ReferenceImpl<>(registry, key);
    }
}
