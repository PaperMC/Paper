package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Event object for {@link RegistryEventProvider#freeze()}. This
 * event is fired right before a registry is frozen disallowing further changes.
 * It provides a way for plugins to add new objects to the registry.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface RegistryFreezeEvent<T, B extends RegistryBuilder<T>> extends RegistryEvent<T> {

    /**
     * Get the writable registry.
     *
     * @return a writable registry
     */
    WritableRegistry<T, B> registry();

    /**
     * Gets or creates a tag for the given tag key. This tag
     * is then required to be filled either from the built-in or
     * custom datapack.
     *
     * @param tagKey the tag key
     * @return the tag
     * @param <V> the tag value type
     */
    <V extends Keyed> Tag<V> getOrCreateTag(TagKey<V> tagKey);
}
