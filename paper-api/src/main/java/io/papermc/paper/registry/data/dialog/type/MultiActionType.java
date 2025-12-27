package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;
import java.util.List;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

/**
 * Represents a dialog that allows multiple actions to be performed.
 * This dialog is used to create dialogs with multiple action buttons, allowing users to choose from several options.
 * @see DialogType#multiAction(List, ActionButton, int)
 */
@ApiStatus.NonExtendable
public non-sealed interface MultiActionType extends DialogType {

    /**
     * Returns the list of action buttons available in this multi-action dialog.
     *
     * @return an unmodifiable list of action buttons
     */
    @Contract(pure = true)
    @Unmodifiable List<ActionButton> actions();

    /**
     * Returns the action button to exit the dialog, or null if there is no exit action.
     *
     * @return the exit action button, or null
     */
    @Contract(pure = true)
    @Nullable ActionButton exitAction();

    /**
     * Returns the number of columns to display in the dialog.
     *
     * @return the number of columns
     */
    @Contract(pure = true)
    @Positive int columns();

    /**
     * A builder for creating a multi-action dialog.
     */
    @ApiStatus.NonExtendable
    interface Builder {

        /**
         * Sets the action button to exit the dialog, or null if there is no exit action.
         *
         * @param exitAction the exit action button, or null
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder exitAction(final @Nullable ActionButton exitAction);

        /**
         * Sets the number of columns to display in the dialog.
         *
         * @param columns the number of columns
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder columns(final @Positive int columns);

        /**
         * Builds the multi-action dialog.
         *
         * @return a new instance
         */
        @Contract(value = "-> new", pure = true)
        MultiActionType build();
    }
}
