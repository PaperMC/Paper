package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;

/**
 * Raised when a vehicle is destroyed
 */
public class VehicleDestroyEvent extends VehicleEvent implements Cancellable {
    private Entity attacker;
    private boolean cancelled;

    public VehicleDestroyEvent(Vehicle vehicle, Entity attacker) {
        super(Type.VEHICLE_DESTROY, vehicle);
        this.attacker = attacker;
    }

    /**
     * Gets the Entity that has destroyed the vehicle
     *
     * @return the Entity that has destroyed the vehicle
     */
    public Entity getAttacker() {
        return attacker;
    }

        /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
