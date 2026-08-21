package org.bukkit.event.entity;

import org.bukkit.entity.Entity;

/**
 * Called when an entity receives knockback from another entity.
 *
 * @deprecated use {@link com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent}
 */
@Deprecated(forRemoval = true)
public interface EntityKnockbackByEntityEvent extends EntityKnockbackEvent {

    /**
     * Get the entity that has caused knockback to the defender.
     *
     * @return entity that caused knockback
     */
    Entity getSourceEntity();
}
