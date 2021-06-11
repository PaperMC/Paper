package org.bukkit.block.data.type;

import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.NotNull;

/**
 * 'sculk_sensor_phase' indicates the current operational phase of the sensor.
 */
public interface SculkSensor extends AnaloguePowerable, Waterlogged {

    /**
     * Gets the value of the 'sculk_sensor_phase' property.
     *
     * @return the 'sculk_sensor_phase' value
     */
    @NotNull
    Phase getPhase();

    /**
     * Sets the value of the 'sculk_sensor_phase' property.
     *
     * @param phase the new 'sculk_sensor_phase' value
     */
    void setPhase(@NotNull Phase phase);

    /**
     * The Phase of the sensor.
     */
    public enum Phase {

        /**
         * The sensor is inactive.
         */
        INACTIVE,
        /**
         * The sensor is active.
         */
        ACTIVE,
        /**
         * The sensor is cooling down.
         */
        COOLDOWN;
    }
}
