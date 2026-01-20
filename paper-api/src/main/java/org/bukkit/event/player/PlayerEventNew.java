package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Represents a player related event
 */
public interface PlayerEventNew extends Event { // todo rename

    Player getPlayer();
}
