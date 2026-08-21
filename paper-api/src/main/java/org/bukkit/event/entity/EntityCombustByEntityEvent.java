package org.bukkit.event.entity;

import org.bukkit.entity.Entity;

/**
 * Called when an entity causes another entity to combust.
 */
public interface EntityCombustByEntityEvent extends EntityCombustEvent {

    /**
     * Get the entity that caused the combustion event.
     *
     * @return the entity that set the combustee alight.
     */
    Entity getCombuster();
}
