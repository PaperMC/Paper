package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;

/**
 * Stores data for weather changing in a world
 */
public class WeatherChangeEvent extends WeatherEvent implements Cancellable {

    private boolean canceled;
    private boolean to;
    private World world;

    public WeatherChangeEvent(World world, boolean to) {
        super(Type.WEATHER_CHANGE, world);
        this.world = world;
        this.to = to;
    }

    /**
     * Gets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is canceled
     */
    public boolean isCancelled() {
        return canceled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
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

}
