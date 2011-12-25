package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;

/**
 * Stores data for weather changing in a world
 */
@SuppressWarnings("serial")
public class WeatherChangeEvent extends WeatherEvent implements Cancellable {

    private boolean canceled;
    private boolean to;

    public WeatherChangeEvent(World world, boolean to) {
        super(Type.WEATHER_CHANGE, world);
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
}
