package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.dialog.specialty.DialogSpecialty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link io.papermc.paper.dialog.Dialog} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DialogRegistryEntry {

    DialogBase dialogBase();

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
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DialogRegistryEntry, RegistryBuilder<Dialog> {

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
