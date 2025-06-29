package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Represents a dialog that allows multiple actions to be performed.
 * This dialog is used to create dialogs with multiple action buttons, allowing users to choose from several options.
 * @see DialogType#multiAction(List, ActionButton, int)
 */
public sealed interface MultiActionType extends DialogType permits MultiActionTypeImpl {

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
    @Range(from = 1, to = Integer.MAX_VALUE) int columns();
}
