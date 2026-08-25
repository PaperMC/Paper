package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Raised when a vehicle collides with an entity.
 */
public interface VehicleEntityCollisionEvent extends VehicleCollisionEvent, Cancellable { // todo javadocs?

    Entity getEntity();

    @Deprecated(forRemoval = true)
    boolean isPickupCancelled();

    @Deprecated(forRemoval = true)
    void setPickupCancelled(boolean cancel);

    @Deprecated(forRemoval = true)
    boolean isCollisionCancelled();

    @Deprecated(forRemoval = true)
    void setCollisionCancelled(boolean cancel);
}
