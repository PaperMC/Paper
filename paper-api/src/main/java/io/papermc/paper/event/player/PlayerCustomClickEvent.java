package io.papermc.paper.event.player;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * This event is fired for any custom click events.
 *
 * @see net.kyori.adventure.text.event.ClickEvent#custom(Key, BinaryTagHolder)
 * @see io.papermc.paper.registry.data.dialog.action.DialogAction#customClick(DialogActionCallback, ClickCallback.Options)
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface PlayerCustomClickEvent extends Event {

    /**
     * The identifier of the custom click event.
     *
     * @return the identifier
     */
    Key getIdentifier();

    /**
     * The tag payload of the custom click event.
     *
     * @return the tag (if any)
     */
    @Nullable BinaryTagHolder getTag();

    /**
     * The dialog response view of the custom click event.
     *
     * @return the dialog response view
     */
    @Nullable DialogResponseView getDialogResponseView();

    /**
     * The common connection of the player.
     *
     * @return the common connection
     */
    PlayerCommonConnection getCommonConnection();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
