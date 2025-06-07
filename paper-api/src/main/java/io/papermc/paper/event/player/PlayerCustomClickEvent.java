package io.papermc.paper.event.player;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This event is fired for any custom click events.
 * @see net.kyori.adventure.text.event.ClickEvent#custom(Key, BinaryTagHolder)
 * @see io.papermc.paper.registry.data.dialog.action.DialogAction#customClick(Key, DialogActionCallback, ClickCallback.Options)
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
@NullMarked
public abstract class PlayerCustomClickEvent extends Event {

    private final Key identifier;
    private final Player player;

    @ApiStatus.Internal
    protected PlayerCustomClickEvent(final Key identifier, final Player player) {
        this.identifier = identifier;
        this.player = player;
    }

    /**
     * The identifier of the custom click event.
     *
     * @return the identifier
     */
    public final Key getIdentifier() {
        return this.identifier;
    }

    /**
     * The tag payload of the custom click event.
     *
     * @return the tag (if any)
     */
    public abstract @Nullable BinaryTagHolder getTag();

    /**
     * The dialog response view of the custom click event.
     *
     * @return the dialog response view
     */
    public abstract @Nullable DialogResponseView getDialogResponseView();

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        // this will be how handler lists will work on interfaces
        return PlayerCustomClickEvent.getHandlerList();
    }

    public static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
