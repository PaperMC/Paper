package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.TypedKey;
import org.jetbrains.annotations.ApiStatus;

/**
 * During the registry loading phase, some values can be references to values to-be-loaded
 * in the future, or inlined, anonymous values that already exist. This type (and subtypes) represent
 * that structure.
 *
 * @param <T> the registry's type
 * @param <E> the type of the registry entry (for inlined values)
 */
@ApiStatus.Experimental
public sealed interface RegistryHolder<T, E> permits RegistryHolder.Reference, RegistryHolder.Inlined {

    /**
     * A holder that references a registry value by key, but does not have the entry itself.
     * This is used for entries that are only referenced by key and may not yet have any value associated with them.
     *
     * @param <T> the registry's type
     * @param <E> the type of the registry entry
     */
    @ApiStatus.NonExtendable
    non-sealed interface Reference<T, E> extends RegistryHolder<T, E> {

        /**
         * The key of the referenced value.
         *
         * @return the key of the value
         */
        TypedKey<T> key();
    }

    /**
     * A holder that contains an inlined registry entry, an anonymous value (does not have a key).
     *
     * @param <T> the registry's type
     * @param <E> the type of the registry entry
     */
    @ApiStatus.NonExtendable
    non-sealed interface Inlined<T, E> extends RegistryHolder<T, E> {

        /**
         * The inlined entry.
         *
         * @return the inlined entry
         */
        E entry();
    }
}
