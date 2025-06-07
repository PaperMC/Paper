package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jetbrains.annotations.Contract;

/**
 * Represents a notice dialog specialty.
 */
public sealed interface NoticeSpecialty extends DialogSpecialty permits NoticeSpecialtyImpl {

    /**
     * Returns the action button associated with this notice specialty.
     *
     * @return the action button
     */
    @Contract(pure = true)
    ActionButton action();
}
