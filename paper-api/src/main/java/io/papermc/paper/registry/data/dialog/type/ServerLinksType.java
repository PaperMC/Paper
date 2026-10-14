package io.papermc.paper.registry.data.dialog.type;


import io.papermc.paper.registry.data.dialog.ActionButton;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

/**
 * Represents a server links dialog that displays links.
 * @see DialogType#serverLinks(ActionButton, int, int)
 */
@ApiStatus.NonExtendable
public non-sealed interface ServerLinksType extends DialogType {

    /**
     * Returns the action button to exit the dialog, or null if there is no exit action.
     *
     * @return the exit action button, or null
     */
    @Contract(pure = true)
    @Nullable ActionButton exitAction();

    /**
     * Returns the number of columns to display in the server links dialog.
     *
     * @return the number of columns
     */
    @Contract(pure = true)
    @Positive int columns();

    /**
     * Returns the width of each button in the server links dialog.
     *
     * @return the width of the buttons
     */
    @Contract(pure = true)
    @Range(from = 1, to = 1024) int buttonWidth();
}
