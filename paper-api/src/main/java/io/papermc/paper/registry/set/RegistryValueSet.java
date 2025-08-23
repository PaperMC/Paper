package io.papermc.paper.registry.set;

import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import java.util.Collection;
import java.util.Iterator;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A collection of anonymous values relating to a registry. These
 * are values of the same type as the registry, but will not be found
 * in the registry, hence, anonymous. Created via {@link RegistrySet#valueSet(io.papermc.paper.registry.RegistryKey, Iterable)} or
 * in the context of a {@link io.papermc.paper.registry.RegistryBuilder},
 * there are methods to create them like {@link DialogRegistryEntry.Builder#registryValueSet()}.
 * @param <T> registry value type
 */
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
