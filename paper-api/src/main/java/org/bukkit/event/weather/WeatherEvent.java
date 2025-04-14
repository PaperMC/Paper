package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Weather-related event
 */
public abstract class WeatherEvent extends Event {

    protected World world;

    protected WeatherEvent(@NotNull final World where) {
        this.world = where;
    }

    /**
     * Returns the World where this event is occurring
     *
     * @return World this event is occurring in
     */
    @NotNull
    public final World getWorld() {
        return this.world;
    }
}
