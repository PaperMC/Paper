package org.bukkit.event.vehicle;

import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Raised when a vehicle moves.
 */
public class VehicleMoveEvent extends VehicleEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location from;
    private final Location to;

    @ApiStatus.Internal
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
        return this.from.clone();
    }

    /**
     * Get the next position.
     *
     * @return New position.
     */
    @NotNull
    public Location getTo() {
        return this.to.clone();
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
