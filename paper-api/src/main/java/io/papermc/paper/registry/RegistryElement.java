package io.papermc.paper.registry;

import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.jspecify.annotations.NullMarked;

/**
 * An element in a registry which might be tied into it.
 * <p>
 * For key-less element all the methods in this interface will return {@code false}
 */
@NullMarked
public interface RegistryElement<T> {

    /**
     * Checks whether this element is equivalent to the
     * given key extracted from this keyed type.
     *
     * @param type the keyed to extract the key from
     * @return the result
     */
    default boolean is(Keyed type) {
        return this.is(type.key());
    }

    /**
     * Checks whether this element is equivalent to the
     * given key type.
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
