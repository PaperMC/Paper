package org.bukkit.block.data.type;

import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.NotNull;

/// 'sculk\_sensor\_phase' indicates the current operational phase of the sensor.
public interface SculkSensor extends AnaloguePowerable, Waterlogged {

    /// Gets the value of the 'sculk\_sensor\_phase' property.
    ///
    /// @return the 'sculk\_sensor\_phase' value
    /// @deprecated bad name, use [#getSculkSensorPhase()]
    @NotNull
    @Deprecated
    default Phase getPhase() {
        return this.getSculkSensorPhase();
    }

    /// Sets the value of the 'sculk\_sensor\_phase' property.
    ///
    /// @param phase the new 'sculk\_sensor\_phase' value
    /// @deprecated bad name, use [#setSculkSensorPhase(Phase)]
    @Deprecated
    default void setPhase(@NotNull Phase phase) {
        this.setSculkSensorPhase(phase);
    }

    /// Gets the value of the 'sculk\_sensor\_phase' property.
    ///
    /// @return the 'sculk\_sensor\_phase' value
    @NotNull
    Phase getSculkSensorPhase();

    /// Sets the value of the 'sculk\_sensor\_phase' property.
    ///
    /// @param phase the new 'sculk\_sensor\_phase' value
    void setSculkSensorPhase(@NotNull Phase phase);

    /// The Phase of the sensor.
    public enum Phase {

        /// The sensor is inactive.
        INACTIVE,
        /// The sensor is active.
        ACTIVE,
        /// The sensor is cooling down.
        COOLDOWN;
    }
}
