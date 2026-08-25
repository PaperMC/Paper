package org.bukkit.event.vehicle;

import org.bukkit.event.HandlerList;

/**
 * Called when a vehicle updates
 */
public interface VehicleUpdateEvent extends VehicleEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
