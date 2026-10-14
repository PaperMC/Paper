package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

/**
 * Represents a dialog that displays a list of dialogs.
 * @see DialogType#dialogList(RegistrySet, ActionButton, int, int)
 */
@ApiStatus.NonExtendable
public non-sealed interface DialogListType extends DialogType {

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
    @Positive int columns();

    /**
     * Returns the width of each button in the dialog list.
     *
     * @return the width of the buttons
     */
    @Contract(pure = true)
    @Range(from = 1, to = 1024) int buttonWidth();

    /**
     * A builder for creating a dialog list type.
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
         * Sets the number of columns to display in the dialog list.
         *
         * @param columns the number of columns
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder columns(final @Positive int columns);

        /**
         * Sets the width of each button in the dialog list.
         *
         * @param buttonWidth the width of the buttons
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder buttonWidth(final @Range(from = 1, to = 1024) int buttonWidth);

        /**
         * Builds the dialog list type.
         *
         * @return the built dialog list type
         */
        @Contract(value = "-> new", pure = true)
        DialogListType build();
    }
}
