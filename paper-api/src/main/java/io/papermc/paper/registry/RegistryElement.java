package io.papermc.paper.registry;

import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * An element tied to a registry.
 * <p>
 * For unregistered element, the equality methods exposed here will always return {@code false}
 * as there's no way to identify them with a key.
 *
 * @see RegistryKey
 * @see org.bukkit.Registry
 * @see org.bukkit.Registry#getKey(org.bukkit.Keyed)
 */
@NullMarked
@ApiStatus.Experimental
public interface RegistryElement<T> {

    /**
     * Checks whether this element is identified by the given key.
     *
     * @param type the key
     * @return whether this element is identified by the given key.
     */
    boolean is(final TypedKey<T> type);

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

    /**
     * Marker interface for registered types that link an API type to its
     * registry entry and builder types.
     *
     * @param <T> the API type
     * @param <E> the registry entry type
     * @param <B> the builder type
     */
    @ApiStatus.Experimental
    interface Buildable<T, E, B extends RegistryBuilder<T>> extends RegistryElement<T> {
    }

    /**
     * Marker interface for registered types that are valid to be constructed inline.
     *
     * @param <T> the API type
     * @param <E> the registry entry type
     * @param <B> the builder type
     */
    @ApiStatus.Experimental
    interface Inlineable<T, E, B extends RegistryBuilder<T>> extends RegistryElement.Buildable<T, E, B> {
    }
}
