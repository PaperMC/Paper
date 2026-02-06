package org.bukkit.event.player;

import org.bukkit.Warning;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * This event is fired when a sign is opened by the player.
 *
 * @deprecated use {@link io.papermc.paper.event.player.PlayerOpenSignEvent}
 */
@Warning
@Deprecated(forRemoval = true)
public interface PlayerSignOpenEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the sign that was opened.
     *
     * @return opened sign
     */
    Sign getSign();

    /**
     * Gets side of the sign opened.
     *
     * @return side of sign opened
     */
    Side getSide();

    /**
     * Gets the cause of the sign open.
     *
     * @return sign open cause
     */
    Cause getCause();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Cause {

        /**
         * Indicate the sign was opened because of an interaction.
         */
        INTERACT,
        /**
         * Indicate the sign was opened because the sign was placed.
         */
        PLACE,
        /**
         * Indicate the sign was opened because of a plugin.
         */
        PLUGIN,
        /**
         * Indicate the sign was opened for an unknown reason.
         */
        UNKNOWN
    }
}
