package org.bukkit;

/**
 * Represents a mutable vector.
 * 
 * @author sk89q
 */
public class Vector implements Cloneable {
    private static final long serialVersionUID = -2657651106777219169L;
    
    /**
     * Threshold for fuzzy equals().
     */
    private static final double epsilon = 0.000001;
    
    protected double x;
    protected double y;
    protected double z;
    
    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds the vector by another.
     * 
     * @param vec
     * @return the same vector
     */
    public Vector add(Vector vec) {
        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    /**
     * Subtracts the vector by another.
     * 
     * @param vec
     * @return the same vector
     */
    public Vector subtract(Vector vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    /**
     * Multiplies the vector by another.
     * 
     * @param vec
     * @return the same vector
     */
    public Vector multiply(Vector vec) {
        x *= vec.x;
        y *= vec.y;
        z *= vec.z;
        return this;
    }
    
    /**
     * Divides the vector by another.
     * 
     * @param vec
     * @return the same vector
     */
    public Vector divide(Vector vec) {
        x /= vec.x;
        y /= vec.y;
        z /= vec.z;
        return this;
    }
    
    /**
     * Gets the magnitude of the vector, defined as sqrt(x^2+y^2+z^2). The value
     * of this method is not cached and uses a costly square-root function, so
     * do not repeatedly call this method to get the vector's magnitude. NaN
     * will be returned if the inner result of the sqrt() function overflows,
     * which will be caused if the length is too long.
     * 
     * @return the magnitude
     */
    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }
    
    /**
     * Get the distance between this vector and another.  The value
     * of this method is not cached and uses a costly square-root function, so
     * do not repeatedly call this method to get the vector's magnitude. NaN
     * will be returned if the inner result of the sqrt() function overflows,
     * which will be caused if the distance is too long.
     * 
     * @return the distance
     */
    public double distance(Vector o) {
        return Math.sqrt(Math.pow(x - o.x, 2) + Math.pow(y - o.y, 2)
                + Math.pow(z - o.z, 2));
    }
    
    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     * 
     * @param m
     * @return the same vector
     */
    public Vector multiply(int m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }
    
    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     * 
     * @param m
     * @return the same vector
     */
    public Vector multiply(double m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }
    
    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     * 
     * @param m
     * @return the same vector
     */
    public Vector multiply(float m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }
    
    /**
     * Calculates the dot product of this vector with another. The dot product
     * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
     * 
     * @param other
     * @return dot product
     */
    public double getDotProduct(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }
    
    /**
     * Calculates the cross product of this vector with another. The cross
     * product is defined as:
     * 
     * x = y1 * z2 - y2 * z1<br/>
     * y = z1 * x2 - z2 * x1<br/>
     * z = x1 * y2 - x2 * y1
     * 
     * @param o
     * @return the same vector
     */
    public Vector crossProduct(Vector o) {
        double newX = y * o.z - o.y * z;
        double newY = z * o.x - o.z * x;
        double newZ = x * o.y - o.x * y;
        x = newX;
        y = newY;
        z = newZ;
        return this;
    }
    
    /**
     * Converts this vector to a unit vector (a vector with length of 1).
     * 
     * @return the same vector
     */
    public Vector unitVector() {
        double length = length();
        
        x /= length;
        y /= length;
        z /= length;
        
        return this;
    }
    
    /**
     * Gets a unit vector of this vector. This vector will not be chagned.
     * 
     * @return a brand new vector
     */
    public Vector getUnitVector() {
        double length = length();
        return new Vector(x / length, y / length, z / length);
    }
    
    /**
     * Returns whether this vector is in a cuboid. The minimum and maximum
     * vectors given must be truly the minimum and maximum X, Y and Z
     * components.
     * 
     * @param min
     * @param max
     * @return whether this vector is in the cuboid
     */
    public boolean isInCuboid(Vector min, Vector max) {
        return x >= min.x && x <= max.x
                && y >= min.y && y <= max.y
                && z >= min.z && z <= max.z;
    }
    
    /**
     * Returns whether this vector is within a sphere.
     * 
     * @param origin
     * @param radius
     * @return whether this vector is in the sphere
     */
    public boolean isInSphere(Vector origin, double radius) {
        return (Math.pow(origin.x - x, 2)
                + Math.pow(origin.y - y, 2)
                + Math.pow(origin.z - z, 2))
                <= Math.pow(radius, 2);
    }
    
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
        return (int)Math.floor(x);
    }
    
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
        return (int)Math.floor(y);
    }
    
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
        return (int)Math.floor(z);
    }
    
    public Vector setX(int x) {
        this.x = x;
        return this;
    }
    
    public Vector setX(double x) {
        this.x = x;
        return this;
    }
    
    public Vector setX(float x) {
        this.x = x;
        return this;
    }
    
    public Vector setY(int y) {
        this.y = y;
        return this;
    }
    
    public Vector setY(double y) {
        this.y = y;
        return this;
    }
    
    public Vector setY(float y) {
        this.y = y;
        return this;
    }
    
    public Vector setZ(int z) {
        this.z = z;
        return this;
    }
    
    public Vector setZ(double z) {
        this.z = z;
        return this;
    }
    
    public Vector setZ(float z) {
        this.z = z;
        return this;
    }

    /**
     * Checks to see if two objects are equal. Only two Vectors can ever
     * return true
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        
        Vector other = (Vector)obj;
        
        return Math.abs(x - other.x) < epsilon
                && Math.abs(y - other.y) < epsilon
                && Math.abs(z - other.z) < epsilon;
    }

    @Override
    public int hashCode() {
        return ((int)Double.doubleToLongBits(x) >> 13) ^
                ((int)Double.doubleToLongBits(y) >> 7) ^
                (int)Double.doubleToLongBits(z);
    }

    @Override
    public Vector clone() {
        return new Vector(x, y, z);
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }
    
    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }
    
    public Location toLocation(World world, float yaw, float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    /**
     * Get the threshold used for equals().
     * 
     * @return
     */
    public static double getEpsilon() {
        return epsilon;
    }
    
    /**
     * Gets the minimum components of two vectors.
     * 
     * @param v1
     * @param v2
     * @return minimum
     */
    public static Vector getMinimum(Vector v1, Vector v2) {
        return new Vector(
                Math.min(v1.x, v2.x),
                Math.min(v1.y, v2.y),
                Math.min(v1.z, v2.z));
    }
    
    /**
     * Gets the maximum components of two vectors.
     * 
     * @param v1
     * @param v2
     * @return maximum
     */
    public static Vector getMaximum(Vector v1, Vector v2) {
        return new Vector(
                Math.max(v1.x, v2.x),
                Math.max(v1.y, v2.y),
                Math.max(v1.z, v2.z));
    }
}
