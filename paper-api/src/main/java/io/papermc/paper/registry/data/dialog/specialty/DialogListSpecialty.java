package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

/**
 * Represents a dialog specialty that displays a list of dialogs.
 */
public sealed interface DialogListSpecialty extends DialogSpecialty permits DialogListSpecialtyImpl {

    /**
     * Returns the set of dialogs to display in the dialog list.
     *
     * @return the set of dialogs
     */
    @Contract(pure = true)
    RegistrySet<Dialog> dialogs();

    /**
     * Returns the action button to exit the dialog, or null if there is no exit action.
     *
     * @return the exit action button, or null
     */
    @Contract(pure = true)
    @Nullable ActionButton exitAction();

    /**
     * Returns the number of columns to display in the dialog list.
     *
     * @return the number of columns
     */
    @Contract(pure = true)
    @Range(from = 1, to = Integer.MAX_VALUE) int columns();

    /**
     * Returns the width of each button in the dialog list.
     *
     * @return the width of the buttons
     */
    @Contract(pure = true)
    @Range(from = 1, to = 1024) int buttonWidth();

    /**
     * A builder for creating a dialog list specialty.
     */
    sealed interface Builder permits DialogListSpecialtyImpl.BuilderImpl {

        /**
         * Sets the action button to exit the dialog, or null if there is no exit action.
         *
         * @param exitAction the exit action button, or null
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder exitAction(final @Nullable ActionButton exitAction);

        /**
         * Sets the number of columns to display in the dialog list.
         *
         * @param columns the number of columns
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder columns(final int columns);

        /**
         * Sets the width of each button in the dialog list.
         *
         * @param buttonWidth the width of the buttons
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder buttonWidth(final int buttonWidth);

        /**
         * Builds the dialog list specialty.
         *
         * @return the built dialog list specialty
         */
        @Contract(value = "-> new", pure = true)
        DialogListSpecialty build();
    }
}
