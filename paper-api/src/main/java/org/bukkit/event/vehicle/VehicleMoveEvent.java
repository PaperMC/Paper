package org.bukkit.event.vehicle;

import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle moves.
 *
 * @since 1.0.0
 */
public class VehicleMoveEvent extends VehicleEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Location from;
    private final Location to;

    public VehicleMoveEvent(@NotNull final Vehicle vehicle, @NotNull final Location from, @NotNull final Location to) {
        super(vehicle);

        this.from = from;
        this.to = to;
    }

    /**
     * Get the previous position.
     *
     * @return Old position.
     */
    @NotNull
    public Location getFrom() {
        return from.clone(); // Paper - clone to avoid changes
    }

    /**
     * Get the next position.
     *
     * @return New position.
     */
    @NotNull
    public Location getTo() {
        return to.clone(); // Paper - clone to avoid changes
    }

    /**
     * @since 1.1.0
     */

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
