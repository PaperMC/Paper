package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle collides.
 *
 * @since 1.0.0
 */
public abstract class VehicleCollisionEvent extends VehicleEvent {
    private static final org.bukkit.event.HandlerList HANDLER_LIST = new org.bukkit.event.HandlerList(); // Paper
    public VehicleCollisionEvent(@NotNull final Vehicle vehicle) {
        super(vehicle);
    }
    /**
     * @since 1.18.1
     */
    // Paper start
    @Override
    public org.bukkit.event.@org.jetbrains.annotations.NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * @since 1.18.1
     */
    public static org.bukkit.event.@NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    // Paper end
}
