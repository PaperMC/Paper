package io.papermc.paper.registry.set;

import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.Tag;
import org.bukkit.Keyed;
import org.jetbrains.annotations.Contract;

/// Represents a collection tied to a registry.
///
/// There are 3 types of registry sets:
///
///   - [Tag] which is a tag from vanilla or a datapack.
///     These are obtained via [org.bukkit.Registry#getTag(io.papermc.paper.registry.tag.TagKey)].
///   - [RegistryKeySet] which is a set of keys linked to values that are present in the registry. These are
///     created via [#keySet(RegistryKey, Iterable)] or [#keySetFromValues(RegistryKey, Iterable)].
///   - [RegistryValueSet] which is a set of values which are anonymous (don't have keys in the registry). These are
///     created via [#valueSet(RegistryKey, Iterable)].
///
/// @param <T> registry value type
public sealed interface RegistrySet<T> permits RegistryKeySet, RegistryValueSet {

    /// Creates a [RegistryValueSet] from anonymous values.
    ///
    /// All values provided **must not** have keys in the given registry.
    ///
    /// @param registryKey the registry key for the type of these values
    /// @param values the values
    /// @return a new registry set
    /// @param <T> the type of the values
    @Contract(value = "_, _ -> new", pure = true)
    static <T> RegistryValueSet<T> valueSet(final RegistryKey<T> registryKey, final Iterable<? extends T> values) {
        return RegistryValueSetImpl.create(registryKey, values);
    }

    /// Creates a [RegistryKeySet] from registry-backed values.
    ///
    /// All values provided **must** have keys in the given registry.
    ///
    /// If references to actual objects are not available yet, use [#keySet(RegistryKey, Iterable)] to
    /// create an equivalent [RegistryKeySet] using just [`TypedKeys`][TypedKey].
    ///
    /// @param registryKey the registry key for the owner of these values
    /// @param values the values
    /// @return a new registry set
    /// @param <T> the type of the values
    /// @throws IllegalArgumentException if the registry isn't available yet or if any value doesn't have a key in that registry
    @Contract(value = "_, _ -> new", pure = true)
    static <T extends Keyed> RegistryKeySet<T> keySetFromValues(final RegistryKey<T> registryKey, final Iterable<? extends T> values) { // TODO remove Keyed
        return RegistryKeySetImpl.create(registryKey, values);
    }

    /// Creates a direct [RegistrySet] from [`TypedKeys`][TypedKey].
    ///
    /// @param registryKey the registry key for the owner of these keys
    /// @param keys the keys for the values
    /// @return a new registry set
    /// @param <T> the type of the values
    @SafeVarargs
    static <T extends Keyed> RegistryKeySet<T> keySet(final RegistryKey<T> registryKey, final TypedKey<T>... keys) { // TODO remove Keyed
        return keySet(registryKey, Lists.newArrayList(keys));
    }

    /// Creates a direct [RegistrySet] from [`TypedKeys`][TypedKey].
    ///
    /// @param registryKey the registry key for the owner of these keys
    /// @param keys the keys for the values
    /// @return a new registry set
    /// @param <T> the type of the values
    @SuppressWarnings("BoundedWildcard")
    @Contract(value = "_, _ -> new", pure = true)
    static <T extends Keyed> RegistryKeySet<T> keySet(final RegistryKey<T> registryKey, final Iterable<TypedKey<T>> keys) { // TODO remove Keyed
        return new RegistryKeySetImpl<>(registryKey, Lists.newArrayList(keys));
    }

    /// Get the registry key for this set.
    ///
    /// @return the registry key
    RegistryKey<T> registryKey();

    /// Get the size of this set.
    ///
    /// @return the size
    int size();

    /// Checks if the registry set is empty.
    ///
    /// @return true, if empty
    default boolean isEmpty() {
        return this.size() == 0;
    }
}
