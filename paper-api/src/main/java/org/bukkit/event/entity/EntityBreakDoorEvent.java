package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * Called when an {@link Entity} breaks a door.
 * <p>
 * Cancelling the event will cause the event to be delayed.
 */
public interface EntityBreakDoorEvent extends EntityChangeBlockEvent {

    @Override
    LivingEntity getEntity();
}
