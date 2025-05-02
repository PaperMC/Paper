package io.papermc.paper.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
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
@NullMarked
public class AsyncChatDecorateEvent extends ServerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable Player player;
    private final Component originalMessage;
    private Component result;

    private boolean cancelled;

    @ApiStatus.Internal
    public AsyncChatDecorateEvent(final @Nullable Player player, final Component originalMessage) {
        super(true);
        this.player = player;
        this.originalMessage = originalMessage;
        this.result = originalMessage;
    }

    /**
     * Gets the player (if available) associated with this event.
     * <p>
     * Certain commands request decorations without a player context
     * which is why this is possibly {@code null}.
     *
     * @return the player or {@code null}
     */
    public @Nullable Player player() {
        return this.player;
    }

    /**
     * Gets the original decoration input
     *
     * @return the input
     */
    public Component originalMessage() {
        return this.originalMessage;
    }

    /**
     * Gets the decoration result. This may already be different from
     * {@link #originalMessage()} if some other listener to this event
     * changed the result.
     *
     * @return the result
     */
    public Component result() {
        return this.result;
    }

    /**
     * Sets the resulting decorated component.
     *
     * @param result the result
     */
    public void result(final Component result) {
        this.result = result;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * A cancelled decorating event means that no changes to the result component
     * will have any effect. The decorated component will be equal to the original
     * component.
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
