package io.papermc.paper.registry.set;

import io.papermc.paper.registry.TypedKey;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public non-sealed interface RegistryKeySet<T extends Keyed> extends Iterable<TypedKey<T>>, RegistrySet<T> { // TODO remove Keyed

    @Override
    default int size() {
        return this.values().size();
    }

    /**
     * Get the keys for the values in this set.
     *
     * @return the keys
     */
    @Unmodifiable Collection<TypedKey<T>> values();

    /**
     * Resolve this set into a collection of values. Prefer using
     * {@link #values()}.
     *
     * @param registry the registry to resolve the values from (must match {@link #registryKey()})
     * @return the resolved values
     * @see RegistryKeySet#values()
     */
    @Unmodifiable Collection<T> resolve(final Registry<T> registry);

    /**
     * Checks if this set contains the value with the given key.
     *
     * @param valueKey the key to check
     * @return true if the value is in this set
     */
    boolean contains(TypedKey<T> valueKey);

    @Override
    default Iterator<TypedKey<T>> iterator() {
        return this.values().iterator();
    }
}
