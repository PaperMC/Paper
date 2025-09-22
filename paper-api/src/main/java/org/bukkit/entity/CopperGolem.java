package org.bukkit.entity;

import io.papermc.paper.entity.Shearable;
import io.papermc.paper.world.WeatheringCopperState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CopperGolem extends Golem, Shearable {

    WeatheringCopperState getWeatheringState();

    void setWeatheringState(WeatheringCopperState state);
}
