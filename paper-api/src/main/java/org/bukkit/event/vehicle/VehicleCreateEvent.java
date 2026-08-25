package org.bukkit.event.vehicle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle is created.
 */
public interface VehicleCreateEvent extends VehicleEvent, Cancellable {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
