package io.papermc.paper.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a key for a value in a specific registry.
 *
 * @param <T> the value type for the registry
 */
@NullMarked
public sealed interface TypedKey<T> extends Key permits TypedKeyImpl {

    /**
     * Gets the key for the value in the registry.
     *
     * @return the value's key
     */
    @Override
    Key key();

    /**
     * Gets the registry key for the value this key
     * represents.
     *
     * @return the registry key
     */
    RegistryKey<T> registryKey();

    /**
     * Create a typed key from a key and a registry key.
     *
     * @param registryKey the registry this key is for
     * @param key the key for the value in the registry
     * @param <T> value type
     * @return a new key for the value key and registry key
     */
    static <T> TypedKey<T> create(final RegistryKey<T> registryKey, final Key key) {
        return new TypedKeyImpl<>(key, registryKey);
    }

    /**
     * Create a typed key from a string and a registry key.
     *
     * @param registryKey the registry this key is for
     * @param key         the string version of a {@link Key} that will be passed to {@link Key#key(String)} for parsing.
     * @param <T>         value type
     * @return a new key for the value key and registry key
     * @see Key#key(String)
     */
    static <T> TypedKey<T> create(final RegistryKey<T> registryKey, @KeyPattern final String key) {
        return create(registryKey, Key.key(key));
    }
}
