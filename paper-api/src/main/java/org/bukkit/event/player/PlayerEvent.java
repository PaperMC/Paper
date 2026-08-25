package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Represents a player related event
 */
public interface PlayerEvent extends Event {

    /**
     * {@return the player involved in this event}
     */
    Player getPlayer();
}
