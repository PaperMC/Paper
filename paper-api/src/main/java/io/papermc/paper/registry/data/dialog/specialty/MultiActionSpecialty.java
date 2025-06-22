package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Represents a dialog specialty that allows multiple actions to be performed.
 * This specialty is used to create dialogs with multiple action buttons, allowing users to choose from several options.
 */
public sealed interface MultiActionSpecialty extends DialogSpecialty permits MultiActionSpecialtyImpl {

    /**
     * Returns the list of action buttons available in this multi-action specialty.
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
    @Nullable ActionButton exitAction();

    /**
     * Returns the number of columns to display in the dialog.
     *
     * @return the number of columns
     */
    int columns();
}
