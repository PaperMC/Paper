package org.bukkit.generator;

/**
 * Represents the biome noise parameters which may be passed to a world
 * generator.
 */
public interface BiomeParameterPoint {

    /**
     * Gets the temperature of the biome at this point that is suggested by the
     * NoiseGenerator.
     *
     * @return The temperature of the biome at this point
     */
    double getTemperature();

    /**
     * Gets the maximum temperature that is possible.
     *
     * @return The maximum temperature
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMaxTemperature() {
        return this.getTemperature();
    }

    /**
     * Gets the minimum temperature that is possible.
     *
     * @return The minimum temperature
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMinTemperature() {
        return this.getTemperature();
    }

    /**
     * Gets the humidity of the biome at this point that is suggested by the
     * NoiseGenerator.
     *
     * @return The humidity of the biome at this point
     */
    double getHumidity();

    /**
     * Gets the maximum humidity that is possible.
     *
     * @return The maximum humidity
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMaxHumidity() {
        return this.getHumidity();
    }

    /**
     * Gets the minimum humidity that is possible.
     *
     * @return The minimum humidity
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMinHumidity() {
        return this.getHumidity();
    }

    /**
     * Gets the continentalness of the biome at this point that is suggested by
     * the NoiseGenerator.
     *
     * @return The continentalness of the biome at this point
     */
    double getContinentalness();

    /**
     * Gets the maximum continentalness that is possible.
     *
     * @return The maximum continentalness
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMaxContinentalness() {
        return this.getContinentalness();
    }

    /**
     * Gets the minimum continentalness that is possible.
     *
     * @return The minimum continentalness
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMinContinentalness() {
        return this.getContinentalness();
    }

    /**
     * Gets the erosion of the biome at this point that is suggested by the
     * NoiseGenerator.
     *
     * @return The erosion of the biome at this point
     */
    double getErosion();

    /**
     * Gets the maximum erosion that is possible.
     *
     * @return The maximum erosion
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMaxErosion() {
        return this.getErosion();
    }

    /**
     * Gets the minimum erosion that is possible.
     *
     * @return The minimum erosion
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMinErosion() {
        return this.getErosion();
    }

    /**
     * Gets the depth of the biome at this point that is suggested by the
     * NoiseGenerator.
     *
     * @return The depth of the biome at this point
     */
    double getDepth();

    /**
     * Gets the maximum depth that is possible.
     *
     * @return The maximum depth
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMaxDepth() {
        return this.getDepth();
    }

    /**
     * Gets the minimum depth that is possible.
     *
     * @return The minimum depth
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMinDepth() {
        return this.getDepth();
    }

    /**
     * Gets the weirdness of the biome at this point that is suggested by the
     * NoiseGenerator.
     *
     * @return The weirdness of the biome at this point
     */
    double getWeirdness();

    /**
     * Gets the maximum weirdness that is possible.
     *
     * @return The maximum weirdness
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMaxWeirdness() {
        return this.getWeirdness();
    }

    /**
     * Gets the minimum weirdness that is possible.
     *
     * @return The minimum weirdness
     * @deprecated no longer available
     */
    @Deprecated(forRemoval = true, since = "26.3")
    default double getMinWeirdness() {
        return this.getWeirdness();
    }
}
