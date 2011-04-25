package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Event;

/**
 * Represents an Weather-related event
 */
public class WeatherEvent extends Event {
    protected World world;

    public WeatherEvent(final Event.Type type, final World where) {
        super(type);
        world = where;
    }

    /**
     * Returns the World where this event is occuring
     * @return World this event is occuring in
     */
    public final World getWorld() {
        return world;
    }
}
