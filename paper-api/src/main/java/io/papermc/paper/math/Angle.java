package io.papermc.paper.math;

import org.jspecify.annotations.NullMarked;

/**
 * Represents an angle that can be relative or absolute.
 */
@NullMarked
public interface Angle {

    /**
     * Creates a new absolute angle with the specified value. Absolute
     * angles are regardless of the current orientation.
     *
     * @param degrees the angle value, measured in degrees
     * @return a new {@code Angle} instance with the specified value
     */
    static Angle absolute(float degrees) {
        return new AngleImpl(degrees, false);
    }

    /**
     * Creates a new relative angle with the specified value. Relative
     * angles are relative to the current orientation.
     *
     * @param degrees the angle value, measured in degrees
     * @return a new {@code Angle} instance with the specified value
     */
    static Angle relative(float degrees) {
        return new AngleImpl(degrees, true);
    }

    /**
     * Retrieves the value of the angle, measured in degrees.
     *
     * @return the angle value in degrees
     */
    float degrees();


    /**
     * Determines whether the angle is relative or absolute.
     *
     * @return {@code true} if the angle is relative, {@code false} if it is absolute
     */
    boolean relative();
}
