package org.bukkit.util;

/**
 * EulerAngle is used to represent 3 angles, one for each
 * axis (x, y, z). The angles are in radians
 */
public class EulerAngle {

    /**
     * A EulerAngle with every axis set to 0
     */
    public static final EulerAngle ZERO = new EulerAngle(0, 0, 0);

    private final double x;
    private final double y;
    private final double z;

    /**
     * Creates a EularAngle with each axis set to the
     * passed angle in radians
     *
     * @param x the angle for the x axis in radians
     * @param y the angle for the x axis in radians
     * @param z the angle for the x axis in radians
     */
    public EulerAngle(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the angle on the x axis in radians
     *
     * @return the angle in radians
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the angle on the y axis in radians
     *
     * @return the angle in radians
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the angle on the z axis in radians
     *
     * @return the angle in radians
     */
    public double getZ() {
        return z;
    }

    /**
     * Return a EulerAngle which is the result of changing
     * the x axis to the passed angle
     *
     * @param x the angle in radians
     * @return the resultant EulerAngle
     */
    public EulerAngle setX(double x) {
        return new EulerAngle(x, y, z);
    }

    /**
     * Return a EulerAngle which is the result of changing
     * the y axis to the passed angle
     *
     * @param y the angle in radians
     * @return the resultant EulerAngle
     */
    public EulerAngle setY(double y) {
        return new EulerAngle(x, y, z);
    }

    /**
     * Return a EulerAngle which is the result of changing
     * the z axis to the passed angle
     *
     * @param z the angle in radians
     * @return the resultant EulerAngle
     */
    public EulerAngle setZ(double z) {
        return new EulerAngle(x, y, z);
    }

    /**
     * Creates a new EulerAngle which is the result of adding
     * the x, y, z components to this EulerAngle
     *
     * @param x the angle to add to the x axis in radians
     * @param y the angle to add to the y axis in radians
     * @param z the angle to add to the z axis in radians
     * @return the resultant EulerAngle
     */
    public EulerAngle add(double x, double y, double z) {
        return new EulerAngle(
                this.x + x,
                this.y + y,
                this.z + z
        );
    }

    /**
     * Creates a new EulerAngle which is the result of subtracting
     * the x, y, z components to this EulerAngle
     *
     * @param x the angle to subtract to the x axis in radians
     * @param y the angle to subtract to the y axis in radians
     * @param z the angle to subtract to the z axis in radians
     * @return the resultant EulerAngle
     */
    public EulerAngle subtract(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EulerAngle that = (EulerAngle) o;

        return Double.compare(that.x, x) == 0
                && Double.compare(that.y, y) == 0
                && Double.compare(that.z, z) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
