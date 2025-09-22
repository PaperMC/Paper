package org.bukkit.entity;

import io.papermc.paper.entity.Shearable;
import io.papermc.paper.world.WeatheringCopperState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CopperGolem extends Golem, Shearable {

    /**
     * Get the current weathering state of the copper golem.
     *
     * @return the weathering state
     */
    WeatheringCopperState getWeatheringState();

    /**
     * Set the weathering state of the copper golem.
     *
     * @param state the new weathering state
     */
    void setWeatheringState(WeatheringCopperState state);
}
