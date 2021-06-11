package org.bukkit.block;

/**
 * Represents a captured state of a sculk sensor
 */
public interface SculkSensor extends TileState {

    /**
     * Gets the last vibration frequency of this sensor.
     *
     * Different activities detected by the sensor will produce different
     * frequencies and dictate the output of connected comparators.
     *
     * @return frequency between 0-15.
     */
    int getLastVibrationFrequency();

    /**
     * Sets the last vibration frequency of this sensor.
     *
     * Different activities detected by the sensor will produce different
     * frequencies and dictate the output of connected comparators.
     *
     * @param lastVibrationFrequency frequency between 0-15.
     */
    void setLastVibrationFrequency(int lastVibrationFrequency);
}
