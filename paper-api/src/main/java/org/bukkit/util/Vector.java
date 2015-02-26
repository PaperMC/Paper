package org.bukkit.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import static org.bukkit.util.NumberConversions.checkFinite;

/**
 * Represents a mutable vector. Because the components of Vectors are mutable,
 * storing Vectors long term may be dangerous if passing code modifies the
 * Vector later. If you want to keep around a Vector, it may be wise to call
 * <code>clone()</code> in order to get a copy.
 */
@SerializableAs("Vector")
public class Vector implements Cloneable, ConfigurationSerializable {
    private static final long serialVersionUID = -2657651106777219169L;

    private static Random random = new Random();

    /**
     * Threshold for fuzzy equals().
     */
    private static final double epsilon = 0.000001;

    protected double x;
    protected double y;
    protected double z;

    /**
     * Construct the vector with all components as 0.
     */
    public Vector() {
        this(0, 0, 0);
    }

    /**
     * Construct the vector with provided integer components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    public Vector(int x, int y, int z) {
        this((double) x, (double) y, (double) z);
    }

    /**
     * Construct the vector with provided double components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    public Vector(double x, double y, double z) {
        // use setters for range checks
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * Construct the vector with provided float components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    public Vector(float x, float y, float z) {
        this((double) x, (double) y, (double) z);
    }

    /**
     * Adds a vector to this one
     *
     * @param vec The other vector
     * @return the same vector
     */
    public Vector add(Vector vec) {
        setX(getX() + vec.getX());
        setY(getY() + vec.getY());
        setZ(getZ() + vec.getZ());
        return this;
    }

    /**
     * Subtracts a vector from this one.
     *
     * @param vec The other vector
     * @return the same vector
     */
    public Vector subtract(Vector vec) {
        setX(getX() - vec.getX());
        setY(getY() - vec.getY());
        setZ(getZ() - vec.getZ());
        return this;
    }

    /**
     * Multiplies the vector by another.
     *
     * @param vec The other vector
     * @return the same vector
     */
    public Vector multiply(Vector vec) {
        setX(getX() * vec.getX());
        setY(getY() * vec.getY());
        setZ(getZ() * vec.getZ());
        return this;
    }

    /**
     * Divides the vector by another.
     *
     * @param vec The other vector
     * @return the same vector
     */
    public Vector divide(Vector vec) {
        setX(getX() / vec.getX());
        setY(getY() / vec.getY());
        setZ(getZ() / vec.getZ());
        return this;
    }

    /**
     * Copies another vector
     *
     * @param vec The other vector
     * @return the same vector
     */
    public Vector copy(Vector vec) {
        setX(vec.getX());
        setY(vec.getY());
        setZ(vec.getZ());
        return this;
    }

    /**
     * Gets the magnitude of the vector, defined as sqrt(x^2+y^2+z^2). The
     * value of this method is not cached and uses a costly square-root
     * function, so do not repeatedly call this method to get the vector's
     * magnitude. NaN will be returned if the inner result of the sqrt()
     * function overflows, which will be caused if the length is too long.
     *
     * @return the magnitude
     */
    public double length() {
        return Math.sqrt(NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z));
    }

    /**
     * Gets the magnitude of the vector squared.
     *
     * @return the magnitude
     */
    public double lengthSquared() {
        return NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z);
    }

    /**
     * Get the distance between this vector and another. The value of this
     * method is not cached and uses a costly square-root function, so do not
     * repeatedly call this method to get the vector's magnitude. NaN will be
     * returned if the inner result of the sqrt() function overflows, which
     * will be caused if the distance is too long.
     *
     * @param o The other vector
     * @return the distance
     */
    public double distance(Vector o) {
        return Math.sqrt(NumberConversions.square(x - o.x) + NumberConversions.square(y - o.y) + NumberConversions.square(z - o.z));
    }

    /**
     * Get the squared distance between this vector and another.
     *
     * @param o The other vector
     * @return the distance
     */
    public double distanceSquared(Vector o) {
        return NumberConversions.square(x - o.x) + NumberConversions.square(y - o.y) + NumberConversions.square(z - o.z);
    }

    /**
     * Gets the angle between this vector and another in radians.
     *
     * @param other The other vector
     * @return angle in radians
     */
    public float angle(Vector other) {
        double dot = dot(other) / (length() * other.length());

        return (float) Math.acos(dot);
    }

    /**
     * Sets this vector to the midpoint between this vector and another.
     *
     * @param other The other vector
     * @return this same vector (now a midpoint)
     */
    public Vector midpoint(Vector other) {
        setX((getX() + other.getX()) / 2);
        setY((getY() + other.getY()) / 2);
        setZ((getZ() + other.getZ()) / 2);
        return this;
    }

    /**
     * Gets a new midpoint vector between this vector and another.
     *
     * @param other The other vector
     * @return a new midpoint vector
     */
    public Vector getMidpoint(Vector other) {
        double x = (this.x + other.x) / 2;
        double y = (this.y + other.y) / 2;
        double z = (this.z + other.z) / 2;
        return new Vector(x, y, z);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    public Vector multiply(int m) {
        return multiply((double) m);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    public Vector multiply(double m) {
        setX(getX() * m);
        setY(getY() * m);
        setZ(getZ() * m);
        return this;
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    public Vector multiply(float m) {
        return multiply((double) m);
    }

    /**
     * Calculates the dot product of this vector with another. The dot product
     * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
     *
     * @param other The other vector
     * @return dot product
     */
    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Calculates the cross product of this vector with another. The cross
     * product is defined as:
     * <ul>
     * <li>x = y1 * z2 - y2 * z1
     * <li>y = z1 * x2 - z2 * x1
     * <li>z = x1 * y2 - x2 * y1
     * </ul>
     *
     * @param o The other vector
     * @return the same vector
     */
    public Vector crossProduct(Vector o) {
        double newX = getY() * o.getZ() - o.getY() * getZ();
        double newY = getZ() * o.getX() - o.getZ() * getX();
        double newZ = getX() * o.getY() - o.getX() * getY();

        setX(newX);
        setY(newY);
        setZ(newZ);
        return this;
    }

    /**
     * Converts this vector to a unit vector (a vector with length of 1).
     *
     * @return the same vector
     */
    public Vector normalize() {
        double length = length();

        return multiply(1 / length);
    }

    /**
     * Zero this vector's components.
     *
     * @return the same vector
     */
    public Vector zero() {
        setX(0D);
        setY(0D);
        setZ(0D);
        return this;
    }

    /**
     * Returns whether this vector is in an axis-aligned bounding box.
     * <p>
     * The minimum and maximum vectors given must be truly the minimum and
     * maximum X, Y and Z components.
     *
     * @param min Minimum vector
     * @param max Maximum vector
     * @return whether this vector is in the AABB
     */
    public boolean isInAABB(Vector min, Vector max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z;
    }

    /**
     * Returns whether this vector is within a sphere.
     *
     * @param origin Sphere origin.
     * @param radius Sphere radius
     * @return whether this vector is in the sphere
     */
    public boolean isInSphere(Vector origin, double radius) {
        return (NumberConversions.square(origin.x - x) + NumberConversions.square(origin.y - y) + NumberConversions.square(origin.z - z)) <= NumberConversions.square(radius);
    }

    /**
     * Gets the X component.
     *
     * @return The X component.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the floored value of the X component, indicating the block that
     * this vector is contained with.
     *
     * @return block X
     */
    public int getBlockX() {
        return NumberConversions.floor(x);
    }

    /**
     * Gets the Y component.
     *
     * @return The Y component.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the floored value of the Y component, indicating the block that
     * this vector is contained with.
     *
     * @return block y
     */
    public int getBlockY() {
        return NumberConversions.floor(y);
    }

    /**
     * Gets the Z component.
     *
     * @return The Z component.
     */
    public double getZ() {
        return z;
    }

    /**
     * Gets the floored value of the Z component, indicating the block that
     * this vector is contained with.
     *
     * @return block z
     */
    public int getBlockZ() {
        return NumberConversions.floor(z);
    }

    /**
     * Set the X component.
     *
     * @param x The new X component.
     * @return This vector.
     */
    public Vector setX(int x) {
        return setX((double) x);
    }

    /**
     * Set the X component.
     *
     * @param x The new X component.
     * @return This vector.
     */
    public Vector setX(double x) {
        checkFinite(x, "x must be finite");
        this.x = x;
        return this;
    }

    /**
     * Set the X component.
     *
     * @param x The new X component.
     * @return This vector.
     */
    public Vector setX(float x) {
        return setX((double) x);
    }

    /**
     * Set the Y component.
     *
     * @param y The new Y component.
     * @return This vector.
     */
    public Vector setY(int y) {
        return setY((double) y);
    }

    /**
     * Set the Y component.
     *
     * @param y The new Y component.
     * @return This vector.
     */
    public Vector setY(double y) {
        checkFinite(y, "y must be finite");
        this.y = y;
        return this;
    }

    /**
     * Set the Y component.
     *
     * @param y The new Y component.
     * @return This vector.
     */
    public Vector setY(float y) {
        return setY((double) y);
    }

    /**
     * Set the Z component.
     *
     * @param z The new Z component.
     * @return This vector.
     */
    public Vector setZ(int z) {
        return setZ((double) z);
    }

    /**
     * Set the Z component.
     *
     * @param z The new Z component.
     * @return This vector.
     */
    public Vector setZ(double z) {
        checkFinite(z, "z must be finite");
        this.z = z;
        return this;
    }

    /**
     * Set the Z component.
     *
     * @param z The new Z component.
     * @return This vector.
     */
    public Vector setZ(float z) {
        return setZ((double) z);
    }

    /**
     * Checks to see if two objects are equal.
     * <p>
     * Only two Vectors can ever return true. This method uses a fuzzy match
     * to account for floating point errors. The epsilon can be retrieved
     * with epsilon.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }

        Vector other = (Vector) obj;

        return Math.abs(x - other.x) < epsilon && Math.abs(y - other.y) < epsilon && Math.abs(z - other.z) < epsilon && (this.getClass().equals(obj.getClass()));
    }

    /**
     * Returns a hash code for this vector
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;

        hash = 79 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }

    /**
     * Get a new vector.
     *
     * @return vector
     */
    @Override
    public Vector clone() {
        try {
            return (Vector) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    /**
     * Returns this vector's components as x,y,z.
     */
    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }

    /**
     * Gets a Location version of this vector with yaw and pitch being 0.
     *
     * @param world The world to link the location to.
     * @return the location
     */
    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }

    /**
     * Gets a Location version of this vector.
     *
     * @param world The world to link the location to.
     * @param yaw The desired yaw.
     * @param pitch The desired pitch.
     * @return the location
     */
    public Location toLocation(World world, float yaw, float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Get the block vector of this vector.
     *
     * @return A block vector.
     */
    public BlockVector toBlockVector() {
        return new BlockVector(x, y, z);
    }

    /**
     * Get the threshold used for equals().
     *
     * @return The epsilon.
     */
    public static double getEpsilon() {
        return epsilon;
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return minimum
     */
    public static Vector getMinimum(Vector v1, Vector v2) {
        return new Vector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return maximum
     */
    public static Vector getMaximum(Vector v1, Vector v2) {
        return new Vector(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }

    /**
     * Gets a random vector with components having a random value between 0
     * and 1.
     *
     * @return A random vector.
     */
    public static Vector getRandom() {
        return new Vector(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("x", getX());
        result.put("y", getY());
        result.put("z", getZ());

        return result;
    }

    public static Vector deserialize(Map<String, Object> args) {
        double x = 0;
        double y = 0;
        double z = 0;

        if (args.containsKey("x")) {
            x = (Double) args.get("x");
        }
        if (args.containsKey("y")) {
            y = (Double) args.get("y");
        }
        if (args.containsKey("z")) {
            z = (Double) args.get("z");
        }

        return new Vector(x, y, z);
    }
}
