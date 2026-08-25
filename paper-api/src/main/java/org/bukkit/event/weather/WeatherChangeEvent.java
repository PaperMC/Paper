package org.bukkit.event.weather;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Stores data for weather changing in a world
 */
public interface WeatherChangeEvent extends WeatherEvent, Cancellable {

    /**
     * Gets the state of weather that the world is being set to
     *
     * @return {@code true} if the weather is being set to raining, {@code false} otherwise
     */
    boolean toWeatherState();

    /**
     * Gets the cause of the weather change.
     *
     * @return the weather change cause
     */
    Cause getCause();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Cause {
        COMMAND,
        NATURAL,
        SLEEP,
        PLUGIN,
        UNKNOWN
    }
}
