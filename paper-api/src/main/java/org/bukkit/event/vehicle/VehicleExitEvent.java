package org.bukkit.event.vehicle;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a living entity exits a vehicle.
 *
 * @since 1.0.0 R1
 */
public class VehicleExitEvent extends VehicleEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final LivingEntity exited;
    private final boolean isCancellable; // Paper

    public VehicleExitEvent(@NotNull final Vehicle vehicle, @NotNull final LivingEntity exited, boolean isCancellable) { // Paper
        super(vehicle);
        this.exited = exited;
        // Paper start
        this.isCancellable = isCancellable;
    }

    public VehicleExitEvent(@NotNull final Vehicle vehicle, @NotNull final LivingEntity exited) {
        this(vehicle, exited, true);
        // Paper end
    }

    /**
     * Get the living entity that exited the vehicle.
     *
     * @return The entity.
     */
    @NotNull
    public LivingEntity getExited() {
        return exited;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        // Paper start
        if (cancel && !isCancellable) {
            return;
        }
        this.cancelled = cancel;
    }

    /**
     * @since 1.13.2
     */
    public boolean isCancellable() {
        return isCancellable;
        // Paper end
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
