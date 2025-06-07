package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jetbrains.annotations.Contract;

/**
 * Represents a notice dialog.
 * @see DialogType#notice(ActionButton)
 */
public sealed interface NoticeType extends DialogType permits NoticeTypeImpl {

    /**
     * Returns the action button associated with this notice type.
     *
     * @return the action button
     */
    @Contract(pure = true)
    ActionButton action();
}
