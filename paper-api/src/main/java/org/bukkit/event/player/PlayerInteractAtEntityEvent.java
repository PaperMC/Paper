package org.bukkit.event.player;

import org.bukkit.util.Vector;

/**
 * Represents an event that is called when a player right clicks an entity that
 * also contains the location where the entity was clicked.
 */
public interface PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {

    // todo javadocs?
    Vector getClickedPosition();
}
