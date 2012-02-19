package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Stores data for weather changing in a world
 */
public class WeatherChangeEvent extends WeatherEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final boolean to;

    public WeatherChangeEvent(final World world, final boolean to) {
        super(world);
        this.to = to;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the state of weather that the world is being set to
     *
     * @return true if the weather is being set to raining, false otherwise
     */
    public boolean toWeatherState() {
        return to;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
