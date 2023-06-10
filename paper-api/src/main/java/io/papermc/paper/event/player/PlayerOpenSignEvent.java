package io.papermc.paper.event.player;

import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player begins editing a sign's text.
 * <p>
 * Cancelling this event stops the sign editing menu from opening.
 */
@NullMarked
public class PlayerOpenSignEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Sign sign;
    private final Side side;
    private final Cause cause;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerOpenSignEvent(final Player editor, final Sign sign, final Side side, final Cause cause) {
        super(editor);
        this.sign = sign;
        this.side = side;
        this.cause = cause;
    }

    /**
     * Gets the sign that was clicked.
     *
     * @return {@link Sign} that was clicked
     */
    public Sign getSign() {
        return this.sign;
    }

    /**
     * Gets which side of the sign was clicked.
     *
     * @return {@link Side} that was clicked
     * @see Sign#getSide(Side)
     */
    public Side getSide() {
        return this.side;
    }

    /**
     * The cause of this sign open.
     *
     * @return the cause
     */
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

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

    /**
     * The cause of the {@link PlayerOpenSignEvent}.
     */
    public enum Cause {
        /**
         * The event was triggered by the placement of a sign.
         */
        PLACE,
        /**
         * The event was triggered by an interaction with a sign.
         */
        INTERACT,
        /**
         * The event was triggered via a plugin with {@link HumanEntity#openSign(Sign, Side)}
         */
        PLUGIN,
        /**
         * Fallback cause for any unknown cause.
         */
        UNKNOWN,
    }
}
