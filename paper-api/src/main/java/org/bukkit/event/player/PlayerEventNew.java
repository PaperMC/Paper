package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Represents a player related event
 */
public interface PlayerEventNew extends Event { // todo rename

    /**
     * Returns the player involved in this event
     *
     * @return player who is involved in this event
     */
    Player getPlayer();
}
