package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import io.papermc.paper.registry.set.RegistryHolderSetBuilder;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import java.util.function.Consumer;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * A factory for creating tags, holders, and holder sets while
 * reacting to registry events.
 */
@ApiStatus.Experimental
public interface RegistryFactory {

    /**
     * Gets or creates a tag for the given tag key. This tag
     * is then required to be filled either from the built-in or
     * custom datapack.
     *
     * @param tagKey the tag key
     * @return the tag
     * @param <V> the tag value type
     */
    <V extends Keyed> Tag<V> getOrCreateTag(TagKey<V> tagKey); // TODO remove Keyed

    /**
     * Gets or creates a reference holder for the given key. The referenced
     * value does not need to be present in the registry yet.
     * <p>
     * You can only create new reference holders for registries in the same registry layer. For example,
     * since {@link RegistryKey#SOUND_EVENT} is a built-in registry, you cannot create new
     * reference holders while in an event for {@link RegistryKey#SULFUR_CUBE_ARCHETYPE} as that
     * is in a later layer. Try a {@link #createInlinedHolder(RegistryKey, Consumer)}.
     *
     * @param key the key of the referenced value
     * @return the reference holder
     * @param <V> the API type
     * @param <E> the registry entry type
     * @param <B> the builder type
     * @see #createInlinedHolder(RegistryKey, Consumer)
     */
    <V extends Keyed & RegistryElement.Buildable<V, E, B>, E, B extends RegistryBuilder<V>> RegistryHolder.Reference<V, E> getOrCreateReferenceHolder(TypedKey<V> key); // TODO remove Keyed

    /**
     * Creates an inlined holder wrapping an anonymous value built from the
     * given builder consumer.
     *
     * @param registryKey the registry key for the value's type
     * @param value a consumer configuring the inlined value's builder
     * @return the inlined holder
     * @param <V> the API type
     * @param <E> the registry entry type
     * @param <B> the builder type
     * @see #getOrCreateReferenceHolder(TypedKey)
     */
    <V extends Keyed & RegistryElement.Inlineable<V, E, B>, E, B extends RegistryBuilder<V>> RegistryHolder.Inlined<V, E> createInlinedHolder(RegistryKey<V> registryKey, Consumer<RegistryBuilderFactory<V, ? extends B>> value); // TODO remove Keyed

    /**
     * Creates a new builder for a {@link RegistryHolderSetBuilder holder set} of the given registry.
     *
     * @param registryKey the registry key for the value's type
     * @return a new holder set builder
     * @param <V> the API type
     * @param <E> the registry entry type
     * @param <B> the builder type
     */
    <V extends Keyed & RegistryElement.Inlineable<V, E, B>, E, B extends RegistryBuilder<V>> RegistryHolderSetBuilder<V, E, B> createSetBuilder(RegistryKey<V> registryKey); // TODO remove Keyed
}
