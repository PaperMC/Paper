package org.bukkit.event.vehicle;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Raised when a living entity exits a vehicle.
 */
public interface VehicleExitEvent extends VehicleEventNew, Cancellable { // todo javadocs?

    /**
     * Get the living entity that exited the vehicle.
     *
     * @return The entity.
     */
    LivingEntity getExited();

    boolean isCancellable();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
