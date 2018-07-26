package io.papermc.paper.math;

import org.jspecify.annotations.NullMarked;

/**
 * Rotations is an immutable object that stores rotations
 * in degrees on each axis (X, Y, Z).
 */
@NullMarked
public interface Rotations {

    /**
     * Rotations instance with every axis set to 0
     */
    Rotations ZERO = ofDegrees(0, 0, 0);

    /**
     * Creates a new Rotations instance holding the provided rotations
     *
     * @param x the angle for the X axis in degrees
     * @param y the angle for the Y axis in degrees
     * @param z the angle for the Z axis in degrees
     * @return Rotations instance holding the provided rotations
     */
    static Rotations ofDegrees(final double x, final double y, final double z) {
        return new RotationsImpl(x, y, z);
    }

    /**
     * Returns the angle on the X axis in degrees
     *
     * @return the angle in degrees
     */
    double x();

    /**
     * Returns the angle on the Y axis in degrees
     *
     * @return the angle in degrees
     */
    double y();

    /**
     * Returns the angle on the Z axis in degrees
     *
     * @return the angle in degrees
     */
    double z();

    /**
     * Returns a new Rotations instance which is the result
     * of changing the X axis to the passed angle
     *
     * @param x the angle in degrees
     * @return the resultant Rotations
     */
    Rotations withX(double x);

    /**
     * Returns a new Rotations instance which is the result
     * of changing the Y axis to the passed angle
     *
     * @param y the angle in degrees
     * @return the resultant Rotations
     */
    Rotations withY(double y);

    /**
     * Returns a new Rotations instance which is the result
     * of changing the Z axis to the passed angle
     *
     * @param z the angle in degrees
     * @return the resultant Rotations
     */
    Rotations withZ(double z);

    /**
     * Returns a new Rotations instance which is the result of adding
     * the x, y, z components to this Rotations
     *
     * @param x the angle to add to the X axis in degrees
     * @param y the angle to add to the Y axis in degrees
     * @param z the angle to add to the Z axis in degrees
     * @return the resultant Rotations
     */
    Rotations add(double x, double y, double z);

    /**
     * Returns a new Rotations instance which is the result of subtracting
     * the x, y, z components from this Rotations
     *
     * @param x the angle to subtract from the X axis in degrees
     * @param y the angle to subtract from the Y axis in degrees
     * @param z the angle to subtract from the Z axis in degrees
     * @return the resultant Rotations
     */
    default Rotations subtract(final double x, final double y, final double z) {
        return this.add(-x, -y, -z);
    }

}
