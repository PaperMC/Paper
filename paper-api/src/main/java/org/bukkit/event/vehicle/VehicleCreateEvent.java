package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle is created.
 *
 * @since 1.0.0 R1
 */
public class VehicleCreateEvent extends VehicleEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    public VehicleCreateEvent(@NotNull final Vehicle vehicle) {
        super(vehicle);
    }

    /**
     * @since 1.11.2
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @since 1.11.2
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
