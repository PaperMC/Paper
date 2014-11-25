package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Represents an event that is called when a player right clicks an entity
 * with a location on the entity the was clicked.
 */
public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {
    private final Vector position;

    public PlayerInteractAtEntityEvent(Player who, Entity clickedEntity, Vector position) {
        super(who, clickedEntity);
        this.position = position;
    }
    
    public Vector getClickedPosition() {
        return position.clone();
    }
}
