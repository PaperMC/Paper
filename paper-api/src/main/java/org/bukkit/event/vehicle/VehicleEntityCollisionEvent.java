package org.bukkit.event.vehicle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityEvent;

/**
 * Raised when a vehicle collides with an entity.
 */
public interface VehicleEntityCollisionEvent extends VehicleCollisionEvent, EntityEvent, Cancellable { // todo javadocs?

    @Deprecated(forRemoval = true)
    boolean isPickupCancelled();

    @Deprecated(forRemoval = true)
    void setPickupCancelled(boolean cancel);

    @Deprecated(forRemoval = true)
    boolean isCollisionCancelled();

    @Deprecated(forRemoval = true)
    void setCollisionCancelled(boolean cancel);
}
