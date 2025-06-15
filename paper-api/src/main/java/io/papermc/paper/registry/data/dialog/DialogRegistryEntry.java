package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.dialog.specialty.DialogSpecialty;
import io.papermc.paper.registry.set.RegistryValueSetBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link io.papermc.paper.dialog.Dialog} type.
 */
@ApiStatus.NonExtendable
public interface DialogRegistryEntry {

    @Contract(pure = true)
    DialogBase dialogBase();

    @Contract(pure = true)
    DialogSpecialty dialogSpecialty();

    /**
     * A mutable builder for the {@link DialogRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #dialogBase(DialogBase)}</li>
     *     <li>{@link #dialogSpecialty(DialogSpecialty)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends DialogRegistryEntry, RegistryBuilder<Dialog> {

        /**
         * Provides a builder for dialog {@link io.papermc.paper.registry.set.RegistryValueSet} which
         * can be used inside {@link io.papermc.paper.registry.data.dialog.specialty.DialogListSpecialty}.
         * <p>Not a part of the registry entry.</p>
         * @return a new registry value set builder
         */
        @Contract(value = "-> new", pure = true)
        RegistryValueSetBuilder<Dialog, DialogRegistryEntry.Builder> registryValueSetBuilder();

        /**
         * Sets the base dialog for this entry.
         *
         * @param dialogBase the base dialog
         * @return this builder instance
         * @see DialogRegistryEntry#dialogBase()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder dialogBase(DialogBase dialogBase);

        /**
         * Sets the specialty dialog for this entry.
         *
         * @param dialogSpecialty the specialty dialog
         * @return this builder instance
         * @see DialogRegistryEntry#dialogSpecialty()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder dialogSpecialty(DialogSpecialty dialogSpecialty);
    }
}
