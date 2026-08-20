package io.papermc.paper.event.player;

import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called when a player begins editing a sign's text.
 * <p>
 * Cancelling this event stops the sign editing menu from opening.
 */
public interface PlayerOpenSignEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the sign that was clicked.
     *
     * @return {@link Sign} that was clicked
     */
    Sign getSign();

    /**
     * Gets which side of the sign was clicked.
     *
     * @return {@link Side} that was clicked
     * @see Sign#getSide(Side)
     */
    Side getSide();

    /**
     * The cause of this sign open.
     *
     * @return the cause
     */
    Cause getCause();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * The cause of the {@link PlayerOpenSignEvent}.
     */
    enum Cause {
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
