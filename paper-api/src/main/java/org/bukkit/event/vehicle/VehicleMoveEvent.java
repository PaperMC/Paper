package org.bukkit.event.vehicle;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle moves.
 */
public interface VehicleMoveEvent extends VehicleEvent {

    /**
     * Get the previous position.
     *
     * @return Old position.
     */
    Location getFrom();

    /**
     * Get the next position.
     *
     * @return New position.
     */
    Location getTo();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
