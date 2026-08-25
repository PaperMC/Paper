package org.bukkit.craftbukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.weather.WeatherChangeEvent;

public class CraftWeatherChangeEvent extends CraftWeatherEvent implements WeatherChangeEvent {

    private final boolean newWeatherState;
    private final Cause cause;

    private boolean cancelled;

    public CraftWeatherChangeEvent(final World world, final boolean newWeatherState, final Cause cause) {
        super(world);
        this.newWeatherState = newWeatherState;
        this.cause = cause;
    }

    @Override
    public boolean toWeatherState() {
        return this.newWeatherState;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return WeatherChangeEvent.getHandlerList();
    }
}
