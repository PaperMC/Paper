package io.papermc.paper.world.biome;

import org.bukkit.Location;

/**
 * Represents the climate settings of a biome, which includes temperature,
 * precipitation, and other climate-related properties.
 */
public interface BiomeClimate {

    /**
     * Determines if the biome has precipitation.
     *
     * @return true if the biome has precipitation.
     */
    boolean hasPrecipitation();

    /**
     * Controls gameplay features like grass and foliage color,
     * and a height adjusted temperature.
     *
     * @return the temperature of the biome.
     */
    float temperature();

    /**
     * Controls grass and foliage color.
     *
     * @return the downfall of the biome.
     */
    float downfall();

    /**
     * Calculates the temperature based on climate and height.
     *
     * @param location the position to adjust the temperature for.
     * @return the adjusted temperature.
     * @throws IllegalArgumentException if the location has a null world.
     */
    float adjustedTemperature(Location location);
}
