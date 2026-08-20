package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;

/**
 * Event that is fired when a player uses the pick item functionality on an entity
 * (middle-clicking an entity to get the appropriate item).
 * After the handling of this event, the contents of the source and the target slot will be swapped,
 * and the currently selected hotbar slot of the player will be set to the target slot.
 */
public interface PlayerPickEntityEvent extends PlayerPickItemEvent {

    /**
     * Retrieves the entity associated with this event.
     *
     * @return the entity involved in the event
     */
    Entity getEntity();
}
