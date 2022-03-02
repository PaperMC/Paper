package io.papermc.paper.registry.set;

import java.util.Collection;
import java.util.Iterator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * A collection of anonymous values relating to a registry. These
 * are values of the same type as the registry, but will not be found
 * in the registry, hence, anonymous.
 * @param <T> registry value type
 */
@ApiStatus.Experimental
@NullMarked
public sealed interface RegistryValueSet<T> extends Iterable<T>, RegistrySet<T> permits RegistryValueSetImpl {

    @Override
    default int size() {
        return this.values().size();
    }

    /**
     * Get the collection of values in this direct set.
     *
     * @return the values
     */
    @Unmodifiable Collection<T> values();

    @Override
    default Iterator<T> iterator() {
        return this.values().iterator();
    }
}
