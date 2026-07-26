package org.bukkit.event.vehicle;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a living entity exits a vehicle.
 */
public class VehicleExitEvent extends VehicleEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity exited;
    private final boolean isCancellable;

    private boolean cancelled;

    @ApiStatus.Internal
    public VehicleExitEvent(@NotNull final Vehicle vehicle, @NotNull final LivingEntity exited, boolean isCancellable) {
        super(vehicle);
        this.exited = exited;
        this.isCancellable = isCancellable;
    }

    @ApiStatus.Internal
    public VehicleExitEvent(@NotNull final Vehicle vehicle, @NotNull final LivingEntity exited) {
        this(vehicle, exited, true);
    }

    /**
     * Get the living entity that exited the vehicle.
     *
     * @return The entity.
     */
    @NotNull
    public LivingEntity getExited() {
        return this.exited;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        if (cancel && !this.isCancellable) {
            return;
        }
        this.cancelled = cancel;
    }

    public boolean isCancellable() {
        return this.isCancellable;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
