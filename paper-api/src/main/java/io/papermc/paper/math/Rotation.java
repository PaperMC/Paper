package io.papermc.paper.math;

import org.jspecify.annotations.NullMarked;

/**
 * Represents a rotation with specified pitch and yaw values.
 */
@NullMarked
public interface Rotation {
    /**
     * Creates a new rotation with the specified yaw and pitch values.
     *
     * @param yaw   the yaw component of the rotation, measured in degrees
     * @param pitch the pitch component of the rotation, measured in degrees
     * @return a new {@code Rotation} instance with the specified yaw and pitch
     */
    static Rotation rotation(float yaw, float pitch) {
        return new RotationImpl(yaw, pitch);
    }

    /**
     * Retrieves the pitch component of the rotation, measured in degrees.
     *
     * @return the pitch value in degrees
     */
    float pitch();

    /**
     * Retrieves the yaw component of the rotation, measured in degrees.
     *
     * @return the yaw value in degrees
     */
    float yaw();
}
