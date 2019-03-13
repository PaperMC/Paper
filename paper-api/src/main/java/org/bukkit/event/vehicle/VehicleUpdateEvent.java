package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a vehicle updates
 */
public class VehicleUpdateEvent extends VehicleEvent {
    private static final HandlerList handlers = new HandlerList();

    public VehicleUpdateEvent(@NotNull final Vehicle vehicle) {
        super(vehicle);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
