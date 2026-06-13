package io.papermc.paper.registry.event;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface RegistryRetriever {

    /**
     * Gets or creates a tag for the given tag key. This tag
     * is then required to be filled either from the built-in or
     * custom datapack.
     *
     * @param tagKey the tag key
     * @param <V> the tag value type
     * @return the tag
     */
    <V extends Keyed> Tag<V> getOrCreateTag(TagKey<V> tagKey); // TODO remove Keyed

    /**
     * Gets or creates an empty instance of the given {@link TypedKey}.
     * <br>
     * Can only create instances if the applicable registry still supports adding
     * new references. If the registry doesn't, this method will throw an exception.
     *
     * @param key the key to get or create an instance for
     * @param <V> the type of the value associated with the key
     * @return an instance of the given key, or a new instance if it does not exist
     * @throws IllegalStateException if this registry does not support adding new references and the key does not exist
     */
    <V extends Keyed> V getOrCreate(TypedKey<V> key); // TODO remove Keyed
}
