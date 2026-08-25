package org.bukkit.event.hanging;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;

/**
 * Triggered when a hanging entity is removed by an entity
 */
public interface HangingBreakByEntityEvent extends HangingBreakEvent {

    /**
     * Gets the entity that removed the hanging entity.
     *
     * @return the entity that removed the hanging entity
     */
    Entity getRemover();

    /**
     * Gets the {@link DamageSource} that caused the hanging entity to be removed.
     *
     * @return the damage source
     */
    DamageSource getDamageSource();
}
