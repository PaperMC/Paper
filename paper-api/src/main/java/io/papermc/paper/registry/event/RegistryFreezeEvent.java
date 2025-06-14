package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * Event object for {@link RegistryEventProvider#freeze()}. This
 * event is fired right before a registry is frozen disallowing further changes.
 * It provides a way for plugins to add new objects to the registry.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 * @deprecated renamed to {@link RegistryComposeEvent}
 */
@ApiStatus.ScheduledForRemoval(inVersion = "1.21.7 or 1.22, whichever comes first")
@Deprecated(since = "1.21.6", forRemoval = true)
@ApiStatus.NonExtendable
public interface RegistryFreezeEvent<T, B extends RegistryBuilder<T>> extends RegistryComposeEvent<T, B> {

    /**
     * Gets or creates a tag for the given tag key. This tag
     * is then required to be filled either from the built-in or
     * custom datapack.
     *
     * @param tagKey the tag key
     * @param <V> the tag value type
     * @return the tag
     * @deprecated Use {@link #retriever()}
     */
    @Deprecated(forRemoval = true)
    default <V extends Keyed> Tag<V> getOrCreateTag(final TagKey<V> tagKey) {
        return this.retriever().getOrCreateTag(tagKey);
    }
}
