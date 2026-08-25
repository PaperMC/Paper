package org.bukkit.craftbukkit.event.world.weather;

import org.bukkit.World;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.weather.WeatherEvent;

public abstract class CraftWeatherEvent extends CraftEvent implements WeatherEvent {

    protected World world;

    protected CraftWeatherEvent(final World world) {
        this.world = world;
    }

    @Override
    public World getWorld() {
        return this.world;
    }
}
