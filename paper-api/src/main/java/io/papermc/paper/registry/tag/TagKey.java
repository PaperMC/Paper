package io.papermc.paper.registry.tag;

import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface TagKey<T> extends Keyed permits TagKeyImpl {

    /**
     * Creates a new tag key for a registry.
     *
     * @param registryKey the registry for the tag
     * @param key         the specific key for the tag
     * @param <T>         the registry value type
     * @return a new tag key
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <T> TagKey<T> create(final RegistryKey<T> registryKey, final Key key) {
        return new TagKeyImpl<>(registryKey, key);
    }

    /**
     * Creates a new tag key for a registry.
     *
     * @param registryKey the registry for the tag
     * @param key         the string version of a {@link Key} that will be passed to {@link Key#key(String)} for parsing.
     * @param <T>         the registry value type
     * @return a new tag key
     * @see Key#key(String)
     */
    static <T> TagKey<T> create(final RegistryKey<T> registryKey, @KeyPattern final String key) {
        return create(registryKey, Key.key(key));
    }

    /**
     * Get the registry key for this tag key.
     *
     * @return the registry key
     */
    RegistryKey<T> registryKey();
}
