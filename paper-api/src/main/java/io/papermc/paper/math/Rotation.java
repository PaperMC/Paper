package io.papermc.paper.math;

import org.jspecify.annotations.NullMarked;

/**
 * Represents a rotation with specified pitch and yaw values.
 */
@NullMarked
public interface Rotation {
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
