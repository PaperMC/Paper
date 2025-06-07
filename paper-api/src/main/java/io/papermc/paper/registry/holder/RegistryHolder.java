package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.TypedKey;
import java.util.Optional;
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
     * Get an optional for the key of the holder. Will not have a key if the holder is an inlined value.
     *
     * @return an optional of the key, or empty if the holder is an inlined value
     */
    Optional<TypedKey<API>> optionalKey();

    /**
     * Get an optional for the entry of the holder. Will not have an entry if the holder is a reference.
     *
     * @return an optional of the entry, or empty if the holder is a reference
     */
    Optional<ENTRY> optionalEntry();

    /**
     * A holder that references a registry value by key, but does not have the entry itself.
     * This is used for entries that are only referenced by key and may not yet have any value associated with them.
     *
     * @param <API> the registry's type
     * @param <ENTRY> the type of the registry entry
     */
    sealed interface Reference<API, ENTRY> extends RegistryHolder<API, ENTRY> permits ReferenceRegistryHolderImpl {

        /**
         * The key of the referenced value.
         *
         * @return the key of the value
         */
        TypedKey<API> key();

        @Override
        default Optional<TypedKey<API>> optionalKey() {
            return Optional.of(this.key());
        }

        @Override
        default Optional<ENTRY> optionalEntry() {
            return Optional.empty();
        }
    }

    /**
     * A holder that contains an inlined registry entry, an anonymous value (does not have a key).
     *
     * @param <API> the registry's type
     * @param <ENTRY> the type of the registry entry
     */
    sealed interface Inlined<API, ENTRY> extends RegistryHolder<API, ENTRY> permits InlinedRegistryHolderImpl {

        /**
         * The inlined entry.
         *
         * @return the inlined entry
         */
        ENTRY entry();

        @Override
        default Optional<TypedKey<API>> optionalKey() {
            return Optional.empty();
        }

        @Override
        default Optional<ENTRY> optionalEntry() {
            return Optional.of(this.entry());
        }
    }
}
