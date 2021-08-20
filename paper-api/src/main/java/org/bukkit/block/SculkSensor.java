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
    // Paper start
    /**
     * Gets the range this sensor listens to events at.
     *
     * @return the range (defaults to 8)
     */
    int getListenerRange();

    /**
     * Sets the range this sensor will listen to events from.
     *
     * @param range the range (must be greater than 0)
     */
    void setListenerRange(int range);
    // Paper end
}
