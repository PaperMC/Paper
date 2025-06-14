package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.TypedKey;
import org.jetbrains.annotations.ApiStatus;

/**
 * During the registry loading phase, some values can be references to values to-be-loaded
 * in the future, or inlined, anonymous values that already exist. This type (and subtypes) represent
 * that structure.
 * @param <API> the registry's type
 * @param <ENTRY> the type of the registry entry (for inlined values)
 */
@ApiStatus.Experimental
public sealed interface RegistryHolder<API, ENTRY> permits RegistryHolder.Reference, RegistryHolder.Inlined {

    /**
     * A holder that references a registry value by key, but does not have the entry itself.
     * This is used for entries that are only referenced by key and may not yet have any value associated with them.
     *
     * @param <API> the registry's type
     * @param <ENTRY> the type of the registry entry
     */
    @ApiStatus.NonExtendable
    non-sealed interface Reference<API, ENTRY> extends RegistryHolder<API, ENTRY> {

        /**
         * The key of the referenced value.
         *
         * @return the key of the value
         */
        TypedKey<API> key();
    }

    /**
     * A holder that contains an inlined registry entry, an anonymous value (does not have a key).
     *
     * @param <API> the registry's type
     * @param <ENTRY> the type of the registry entry
     */
    @ApiStatus.NonExtendable
    non-sealed interface Inlined<API, ENTRY> extends RegistryHolder<API, ENTRY> {

        /**
         * The inlined entry.
         *
         * @return the inlined entry
         */
        ENTRY entry();
    }
}
