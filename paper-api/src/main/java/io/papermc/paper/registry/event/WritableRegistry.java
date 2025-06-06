package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.TypedKey;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;

/**
 * A registry which supports registering new objects.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 */
@ApiStatus.NonExtendable
public interface WritableRegistry<T, B extends RegistryBuilder<T>> {

    /**
     * Register a new value with the specified key. This will
     * fire a {@link RegistryEntryAddEvent} for the new entry.
     *
     * @param key the entry's key (must be unique from others)
     * @param value a consumer for the entry's builder
     */
    default void register(final TypedKey<T> key, final Consumer<? super B> value) {
        this.registerWith(key, factory -> value.accept(factory.empty()));
    }

    /**
     * Register a new value with the specified key. This will
     * fire a {@link RegistryEntryAddEvent} for the new entry. The
     * {@link RegistryBuilderFactory} lets you pre-fill a builder with
     * an already-existing entry's properties.
     *
     * @param key the entry's key (must be unique from others)
     * @param value a consumer of a builder factory
     */
    void registerWith(TypedKey<T> key, Consumer<RegistryBuilderFactory<T, B>> value);
}
