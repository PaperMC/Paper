package io.papermc.paper.registry;

import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * An element tied to a registry.
 * <p>
 * For unregistered element, the equality methods exposed here will return {@code false}
 * as there's no way to identify them with a key.
 *
 * @see RegistryKey
 * @see org.bukkit.Registry
 * @see org.bukkit.Registry#getKey(org.bukkit.Keyed)
 */
@NullMarked
public interface RegistryElement<T> {

    /**
     * Checks whether this element is identified by the given key.
     *
     * @param type the key
     * @return whether this element is identified by the given key.
     */
    default boolean is(final TypedKey<T> type) { // TypedKey shouldn't extend Key
        return this.is(type.key());
    }

    /**
     * Checks whether this element is identified by the given key.
     *
     * @param type the key
     * @return whether this element is identified by the given key
     */
    boolean is(Key type);

    /**
     * Checks whether this element is contained in the
     * tag identified by the given tag key.
     *
     * @param type the tag key
     * @return whether this element is contained in the tag
     */
    boolean is(TagKey<T> type);
}
