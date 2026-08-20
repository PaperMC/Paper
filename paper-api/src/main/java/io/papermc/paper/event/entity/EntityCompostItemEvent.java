package io.papermc.paper.event.entity;

import io.papermc.paper.event.block.CompostItemEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Called when an item is about to be composted by an entity.
 */
public interface EntityCompostItemEvent extends CompostItemEvent, Cancellable {

    /**
     * Gets the entity that interacted with the composter.
     *
     * @return the entity that composted an item.
     */
    Entity getEntity();
}
