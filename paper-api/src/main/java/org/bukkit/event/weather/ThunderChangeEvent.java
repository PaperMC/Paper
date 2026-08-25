package org.bukkit.event.weather;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Stores data for thunder state changing in a world
 */
public interface ThunderChangeEvent extends WeatherEvent, Cancellable {

    /**
     * Gets the state of thunder that the world is being set to
     *
     * @return {@code true} if the weather is being set to thundering, {@code false} otherwise
     */
    boolean toThunderState();

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
