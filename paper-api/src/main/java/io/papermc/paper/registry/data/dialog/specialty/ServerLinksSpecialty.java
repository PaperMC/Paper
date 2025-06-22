package io.papermc.paper.registry.data.dialog.specialty;


import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jspecify.annotations.Nullable;

/**
 * Represents a server links dialog specialty, which is a specific type of dialog that displays links to servers.
 */
public sealed interface ServerLinksSpecialty extends DialogSpecialty permits ServerLinksSpecialtyImpl {

    /**
     * Returns the action button to exit the dialog, or null if there is no exit action.
     *
     * @return the exit action button, or null
     */
    @Nullable ActionButton exitAction();

    /**
     * Returns the number of columns to display in the server links dialog.
     *
     * @return the number of columns
     */
    int columns();

    /**
     * Returns the width of each button in the server links dialog.
     *
     * @return the width of the buttons
     */
    int buttonWidth();
}
