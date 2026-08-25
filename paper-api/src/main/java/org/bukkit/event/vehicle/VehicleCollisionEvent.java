package org.bukkit.event.vehicle;

import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle collides.
 */
public interface VehicleCollisionEvent extends VehicleEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
