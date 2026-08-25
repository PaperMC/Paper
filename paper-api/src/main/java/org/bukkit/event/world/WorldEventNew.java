package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.Event;

/**
 * Represents events within a world
 */
public interface WorldEventNew extends Event {

    /**
     * Gets the world primarily involved with this event
     *
     * @return World which caused this event
     */
    World getWorld();
}
