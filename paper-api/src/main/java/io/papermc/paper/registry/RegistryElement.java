package io.papermc.paper.registry;

import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.jspecify.annotations.NullMarked;

/**
 * An element of a registry which might or might not be registered.
 * <p>
 * For unregistered element, the equality methods exposed here will return {@code false}
 * as there's no way to identify them with a key.
 *
 * @see RegistryKey
 * @see org.bukkit.Registry
 * @see org.bukkit.Registry#getKey(Keyed)
 */
@NullMarked
public interface RegistryElement<T> {

    /**
     * Checks whether this element is identified by the
     * given key extracted from this keyed.
     *
     * @param type the keyed to extract the key from
     * @return the result
     */
    default boolean is(Keyed type) {
        return this.is(type.key());
    }

    /**
     * Checks whether this element is identified by the given key.
     *
     * @param type the key
     * @return the result
     */
    boolean is(Key type);

    /**
     * Checks whether this element is contained in the
     * tag identified by the given tag key.
     *
     * @param type the tag key
     * @return the result
     */
    boolean is(TagKey<T> type);
}
