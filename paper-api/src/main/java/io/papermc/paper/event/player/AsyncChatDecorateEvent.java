package io.papermc.paper.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEventNew;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * This event is fired when the server decorates a component for chat purposes. This is called
 * before {@link AsyncChatEvent} and the other chat events. It is recommended that you modify the
 * message here, and use the chat events for modifying receivers and later the chat type. If you
 * want to keep the message as "signed" for the clients who get it, be sure to include the entire
 * original message somewhere in the final message.
 * <br>
 * See {@link AsyncChatCommandDecorateEvent} for the decoration of messages sent via commands
 */
@ApiStatus.Experimental
public interface AsyncChatDecorateEvent extends ServerEventNew, Cancellable {

    /**
     * Gets the player (if available) associated with this event.
     * <p>
     * Certain commands request decorations without a player context
     * which is why this is possibly {@code null}.
     *
     * @return the player or {@code null}
     */
    @Nullable Player player();

    /**
     * Gets the original decoration input
     *
     * @return the input
     */
    Component originalMessage();

    /**
     * Gets the decoration result. This may already be different from
     * {@link #originalMessage()} if some other listener to this event
     * changed the result.
     *
     * @return the result
     */
    Component result();

    /**
     * Sets the resulting decorated component.
     *
     * @param result the result
     */
    void result(Component result);

    /**
     * A cancelled decorating event means that no changes to the result component
     * will have any effect. The decorated component will be equal to the original
     * component.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
