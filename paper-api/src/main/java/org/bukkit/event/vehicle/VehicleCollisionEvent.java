package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle collides.
 */
public abstract class VehicleCollisionEvent extends VehicleEvent {
    private static final org.bukkit.event.HandlerList HANDLER_LIST = new org.bukkit.event.HandlerList(); // Paper
    public VehicleCollisionEvent(@NotNull final Vehicle vehicle) {
        super(vehicle);
    }
    // Paper start
    @Override
    public org.bukkit.event.@org.jetbrains.annotations.NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static org.bukkit.event.@NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    // Paper end
}
