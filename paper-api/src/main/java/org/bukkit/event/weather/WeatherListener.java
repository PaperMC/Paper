package org.bukkit.event.weather;

import org.bukkit.event.Listener;

/**
 * Handles all events fired in relation to weather
 */
public class WeatherListener implements Listener {
    public WeatherListener() {}

    /**
     * Called when a weather change occurs
     *
     * @param event Relevant event details
     */
    public void onWeatherChange(WeatherChangeEvent event) {}

    /**
     * Called when the state of thunder changes
     *
     * @param event Relevant event details
     */
    public void onThunderChange(ThunderChangeEvent event) {}

    /**
     * Called when lightning strikes
     *
     * @param event Relevant event details
     */
    public void onLightningStrike(LightningStrikeEvent event) {}
}
