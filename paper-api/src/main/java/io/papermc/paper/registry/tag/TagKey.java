package io.papermc.paper.registry.tag;

import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Experimental
@NullMarked
public sealed interface TagKey<T> extends Keyed permits TagKeyImpl {

    /**
     * Creates a new tag key for a registry.
     *
     * @param registryKey the registry for the tag
     * @param key the specific key for the tag
     * @return a new tag key
     * @param <T> the registry value type
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <T> TagKey<T> create(final RegistryKey<T> registryKey, final Key key) {
        return new TagKeyImpl<>(registryKey, key);
    }

    /**
     * Get the registry key for this tag key.
     *
     * @return the registry key
     */
    RegistryKey<T> registryKey();
}
