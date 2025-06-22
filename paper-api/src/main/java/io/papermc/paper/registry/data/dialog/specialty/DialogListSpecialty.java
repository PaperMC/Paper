package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import org.jetbrains.annotations.Contract;
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
    int columns();

    /**
     * Returns the width of each button in the dialog list.
     *
     * @return the width of the buttons
     */
    @Contract(pure = true)
    int buttonWidth();
}
