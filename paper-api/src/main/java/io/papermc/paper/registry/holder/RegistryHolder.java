package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
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
public sealed interface RegistryHolder<T extends RegistryElement.Buildable<T, E, ?>, E> permits RegistryHolder.Reference, RegistryHolder.Inlined {

    /**
     * Gets the registry key of the registry this holder's value belongs to.
     *
     * @return the registry key
     */
    RegistryKey<T> registryKey();

    /**
     * Gets the value held by this holder.
     *
     * @return the value
     */
    T value();

    /**
     * Gets the registry entry held by this holder.
     *
     * @return the entry
     */
    E entry();

    /**
     * A holder that references a registry value by key, but does not have the entry itself.
     * This is used for entries that are only referenced by key and may not yet have any value associated with them.
     *
     * @param <T> the registry's type
     * @param <E> the type of the registry entry
     */
    @ApiStatus.NonExtendable
    non-sealed interface Reference<T extends RegistryElement.Buildable<T, E, ?>, E> extends RegistryHolder<T, E> {

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
    non-sealed interface Inlined<T extends RegistryElement.Buildable<T, E, ?>, E> extends RegistryHolder<T, E> {

        /**
         * The inlined entry.
         *
         * @return the inlined entry
         */
        @Override
        E entry();
    }
}
