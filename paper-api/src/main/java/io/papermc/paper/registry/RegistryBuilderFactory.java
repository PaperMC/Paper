package io.papermc.paper.registry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/// A factory to create a [RegistryBuilder] for a given [TypedKey]. For
/// each instance of this class, once either [#empty()] or [#copyFrom(TypedKey)]
/// is called once, any future calls to either method will throw an [IllegalStateException].
///
/// @param <T> The type of the registry
/// @param <B> The type of the registry builder
@NullMarked
@ApiStatus.NonExtendable
public interface RegistryBuilderFactory<T, B extends RegistryBuilder<T>> {

    /// Creates a new empty [RegistryBuilder].
    ///
    /// @return A new empty [RegistryBuilder]
    /// @throws IllegalStateException if this method or [#copyFrom(TypedKey)]) has already been called once
    @Contract("-> new")
    B empty();

    /// Creates a new [RegistryBuilder] with the same properties as the given [TypedKey].
    ///
    /// @param key The key to copy properties from
    /// @return A new [RegistryBuilder] with the same properties as the given key
    /// @throws IllegalStateException if this method or [#empty()] has already been called once
    /// @throws IllegalArgumentException if key doesn't exist
    @Contract("_ -> new")
    B copyFrom(TypedKey<T> key);
}
