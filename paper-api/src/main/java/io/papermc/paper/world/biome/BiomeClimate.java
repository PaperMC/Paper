package io.papermc.paper.world.biome;

import io.papermc.paper.math.Position;

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
     * Modifies temperature before calculating the height adjusted temperature.
     *
     * @param position the position to adjust the temperature for.
     * @return the adjusted temperature.
     */
    float adjustedTemperature(Position position);
}
