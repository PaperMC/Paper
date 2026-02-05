package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

/**
 * Called when the velocity of a player changes.
 */
public interface PlayerVelocityEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the velocity vector that will be sent to the player
     *
     * @return Vector the player will get
     */
    Vector getVelocity();

    /**
     * Sets the velocity vector in meters per tick that will be sent to the player
     *
     * @param velocity The velocity vector that will be sent to the player
     */
    void setVelocity(Vector velocity);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
