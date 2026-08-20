package io.papermc.paper.event.entity;

import io.papermc.paper.event.block.CompostItemEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an item is about to be composted by an entity.
 */
@NullMarked
public interface EntityCompostItemEvent extends CompostItemEvent, Cancellable {

    /**
     * Gets the entity that interacted with the composter.
     *
     * @return the entity that composted an item.
     */
    Entity getEntity();
}
