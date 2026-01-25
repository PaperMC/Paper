package org.bukkit.event.player;

import org.bukkit.entity.AbstractArrow;

/**
 * Thrown when a player picks up an arrow from the ground.
 */
public interface PlayerPickupArrowEvent extends PlayerPickupItemEvent {

    /**
     * Get the arrow being picked up by the player
     *
     * @return The arrow being picked up
     */
    AbstractArrow getArrow();
}
