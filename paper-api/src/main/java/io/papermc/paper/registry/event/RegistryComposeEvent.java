package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Event object for {@link RegistryEventProvider#compose()}. This
 * event is fired after a registry is loaded with its normal values.
 * It provides a way for plugins to add new objects to the registry.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 */
@ApiStatus.NonExtendable
public interface RegistryComposeEvent<T, B extends RegistryBuilder<T>> extends RegistryEvent<T> {

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
     * @deprecated use {@link #retriever()}
     */
    @Deprecated(forRemoval = true)
    default <V extends Keyed> Tag<V> getOrCreateTag(TagKey<V> tagKey) { // TODO remove Keyed
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
