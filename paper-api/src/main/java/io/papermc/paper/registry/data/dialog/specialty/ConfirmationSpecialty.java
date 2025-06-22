package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jetbrains.annotations.Contract;

/**
 * Represents a confirmation dialog specialty.
 * This interface defines the structure for a confirmation dialog with "confirm" and "deny" buttons.
 */
public sealed interface ConfirmationSpecialty extends DialogSpecialty permits ConfirmationSpecialtyImpl {

    /**
     * Gets the button for confirming the action.
     *
     * @return the confirmation button
     */
    @Contract(pure = true)
    ActionButton yesButton();

    /**
     * Gets the button for denying the action.
     *
     * @return the denial button
     */
    @Contract(pure = true)
    ActionButton noButton();
}
