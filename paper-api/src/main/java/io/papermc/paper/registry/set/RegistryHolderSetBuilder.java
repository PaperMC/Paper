package io.papermc.paper.registry.set;

import io.papermc.paper.registry.Registered;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.TypedKey;
import java.util.function.Consumer;

/**
 * A builder for a {@link RegistryHolderSet}.
 *
 * @param <T> the API type
 * @param <E> the registry entry type
 * @param <B> the builder type
 */
public interface RegistryHolderSetBuilder<T extends Registered.Inlineable<T, E, B>, E, B extends RegistryBuilder<T>> {

    /**
     * Adds a value to the set.
     *
     * @param value the value to add
     * @return this builder
     */
    RegistryHolderSetBuilder<T, E, B> add(T value);

    /**
     * Adds a value by its key to the set. The referenced value does not need
     * to be present in the registry yet.
     *
     * @param key the key of the value to add
     * @return this builder
     */
    RegistryHolderSetBuilder<T, E, B> add(TypedKey<T> key);

    /**
     * Adds an inlined, anonymous value built from the given builder consumer to the set.
     *
     * @param value a consumer configuring the inlined value's builder
     * @return this builder
     */
    RegistryHolderSetBuilder<T, E, B> add(Consumer<RegistryBuilderFactory<T, ? extends B>> value);

    /**
     * Builds the holder set.
     *
     * @return a new holder set
     */
    RegistryHolderSet<T, E> build();
}
