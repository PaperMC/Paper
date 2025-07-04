package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Represents a confirmation dialog.
 * This interface defines the structure for a confirmation dialog with "confirm" and "deny" buttons.
 * @see DialogType#confirmation(ActionButton, ActionButton)
 */
@ApiStatus.NonExtendable
public non-sealed interface ConfirmationType extends DialogType {

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
