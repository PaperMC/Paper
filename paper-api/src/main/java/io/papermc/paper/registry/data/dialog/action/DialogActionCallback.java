package io.papermc.paper.registry.data.dialog.action;

import io.papermc.paper.dialog.DialogResponseView;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.ApiStatus;

/**
 * A callback for a dialog action.
 */
@FunctionalInterface
public interface DialogActionCallback {

    /**
     * Handles a dialog action.
     *
     * @param response the response to the action
     * @param audience the audience to send the response to
     */
    @ApiStatus.OverrideOnly
    void accept(DialogResponseView response, Audience audience);
}
