
package org.bukkit;

/**
 * Represents a 3-dimensional position in a world
 */
public class Location {
    private World world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public Location(final World world, final double x, final double y, final double z) {
        this(world, x, y, z, 0, 0);
    }

    public Location(final World world, final double x, final double y, final double z, final float pitch, final float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Sets the world that this location resides in
     *
     * @param world New world that this location resides in
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Gets the world that this location resides in
     *
     * @return World that contains this location
     */
    public World getWorld() {
        return world;
    }

    /**
     * Sets the x-coordinate of this location
     *
     * @param x X-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the x-coordinate of this location
     *
     * @return x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the y-coordinate of this location
     *
     * @param y y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the y-coordinate of this location
     *
     * @return y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the z-coordinate of this location
     *
     * @param z z-coordinate
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Gets the z-coordinate of this location
     *
     * @return z-coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the yaw of this location
     *
     * @param yaw New yaw
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * Gets the yaw of this location
     *
     * @return Yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Sets the pitch of this location
     *
     * @param pitch New pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Gets the pitch of this location
     *
     * @return Pitch
     */
    public float getPitch() {
        return pitch;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (this.world != other.world && (this.world == null || !this.world.equals(other.world))) {
            return false;
        }
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
            return false;
        }
        if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.world != null ? this.world.hashCode() : 0);
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        hash = 19 * hash + Float.floatToIntBits(this.pitch);
        hash = 19 * hash + Float.floatToIntBits(this.yaw);
        return hash;
    }

    @Override
    public String toString() {
        return "Location{" + "world=" + world + "x=" + x + "y=" + y + "z=" + z + "pitch=" + pitch + "yaw=" + yaw + '}';
    }
}
