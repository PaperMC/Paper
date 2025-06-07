package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Event object for {@link RegistryEventProvider#entryAdd()}. This
 * event is fired right before a specific entry is registered in/added to registry.
 * It provides a way for plugins to modify parts of this entry.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 */
@ApiStatus.NonExtendable
public interface RegistryEntryAddEvent<T, B extends RegistryBuilder<T>> extends RegistryEvent<T> {

    /**
     * Gets the builder for the entry being added to the registry.
     *
     * @return the object builder
     */
    B builder();

    /**
     * Gets the key for this entry in the registry.
     *
     * @return the key
     */
    TypedKey<T> key();

    /**
     * Gets or creates a tag for the given tag key. This tag
     * is then required to be filled either from the built-in or
     * custom datapack.
     *
     * @param tagKey the tag key
     * @return the tag
     * @param <V> the tag value type
     * @deprecated use {@link #retriever()}
     */
    @Deprecated(forRemoval = true)
    default <V extends Keyed> Tag<V> getOrCreateTag(final TagKey<V> tagKey) {
        return this.retriever().getOrCreateTag(tagKey);
    }

    /**
     * Gets the registry retriever, which can be used to retrieve or create
     * entries in the registry.
     *
     * @return the registry retriever
     */
    @Contract(pure = true)
    RegistryRetriever retriever();
}
