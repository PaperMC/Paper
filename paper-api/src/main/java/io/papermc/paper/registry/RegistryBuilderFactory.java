package io.papermc.paper.registry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * A factory to create a {@link RegistryBuilder} for a given {@link TypedKey}. For
 * each instance of this class, once either {@link #empty()} or {@link #copyFrom(TypedKey)}
 * is called once, any future calls to either method will throw an {@link IllegalStateException}.
 *
 * @param <T> The type of the registry
 * @param <B> The type of the registry builder
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface RegistryBuilderFactory<T, B extends RegistryBuilder<T>> {

    /**
     * Creates a new empty {@link RegistryBuilder}.
     *
     * @return A new empty {@link RegistryBuilder}
     * @throws IllegalStateException if this method or {@link #copyFrom(TypedKey)}) has already been called once
     */
    @Contract("-> new")
    B empty();

    /**
     * Creates a new {@link RegistryBuilder} with the same properties as the given {@link TypedKey}.
     *
     * @param key The key to copy properties from
     * @return A new {@link RegistryBuilder} with the same properties as the given key
     * @throws IllegalStateException if this method or {@link #empty()} has already been called once
     * @throws IllegalArgumentException if key doesn't exist
     */
    @Contract("_ -> new")
    B copyFrom(TypedKey<T> key);
}
