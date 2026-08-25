package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Event;

/**
 * Represents a Weather-related event
 */
public interface WeatherEvent extends Event {

    /**
     * Returns the World where this event is occurring
     *
     * @return World this event is occurring in
     */
    World getWorld();
}
