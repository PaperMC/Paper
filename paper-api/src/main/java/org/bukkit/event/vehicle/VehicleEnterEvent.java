package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when an entity enters a vehicle.
 *
 * @since 1.0.0 R1
 */
public class VehicleEnterEvent extends VehicleEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Entity entered;

    public VehicleEnterEvent(@NotNull final Vehicle vehicle, @NotNull final Entity entered) {
        super(vehicle);
        this.entered = entered;
    }

    /**
     * Gets the Entity that entered the vehicle.
     *
     * @return the Entity that entered the vehicle
     */
    @NotNull
    public Entity getEntered() {
        return entered;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
