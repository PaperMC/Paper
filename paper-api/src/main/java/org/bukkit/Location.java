
package org.bukkit;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 * Represents a 3-dimensional position in a world
 */
public class Location implements Cloneable {
    private World world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    /**
     * Constructs a new Location with the given coordinates
     *
     * @param world The world in which this location resides
     * @param x The x-coordinate of this new location
     * @param y The y-coordinate of this new location
     * @param z The z-coordinate of this new location
     */
    public Location(final World world, final double x, final double y, final double z) {
        this(world, x, y, z, 0, 0);
    }

    /**
     * Constructs a new Location with the given coordinates and direction
     *
     * @param world The world in which this location resides
     * @param x The x-coordinate of this new location
     * @param y The y-coordinate of this new location
     * @param z The z-coordinate of this new location
     * @param yaw The absolute rotation on the x-plane, in degrees
     * @param pitch The absolute rotation on the y-plane, in degrees
     */
    public Location(final World world, final double x, final double y, final double z, final float yaw, final float pitch) {
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
     * Gets the block at the represented location
     *
     * @return Block at the represented location
     */
    public Block getBlock() {
        return world.getBlockAt(this);
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
     * Gets the floored value of the X component, indicating the block that
     * this location is contained with.
     *
     * @return block X
     */
    public int getBlockX() {
        return locToBlock(x);
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
     * Gets the floored value of the Y component, indicating the block that
     * this location is contained with.
     *
     * @return block y
     */
    public int getBlockY() {
        return locToBlock(y);
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
     * Gets the floored value of the Z component, indicating the block that
     * this location is contained with.
     *
     * @return block z
     */
    public int getBlockZ() {
        return locToBlock(z);
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

    /**
     * Gets a Vector pointing in the direction that this Location is facing
     *
     * @return Vector
     */
    public Vector getDirection() {
        Vector vector = new Vector();

        double rotX = this.getYaw();
        double rotY = this.getPitch();

        vector.setY(-Math.sin(Math.toRadians(rotY)));

        double h = Math.cos(Math.toRadians(rotY));
        vector.setX(-h*Math.sin(Math.toRadians(rotX)));
        vector.setZ(h*Math.cos(Math.toRadians(rotX)));

        return vector;
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

    /**
     * Constructs a new {@link Vector} based on this Location
     *
     * @return New Vector containing the coordinates represented by this Location
     */
    public Vector toVector() {
        return new Vector(x, y, z);
    }

    @Override
    public Location clone() {
        try {
            Location l = (Location)super.clone();
            l.world = world;
            l.x = x;
            l.y = y;
            l.z = z;
            l.yaw = yaw;
            l.pitch = pitch;
            return l;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Safely converts a double (location coordinate) to an int (block coordinate)
     *
     * @param loc Precise coordinate
     * @return Block coordinate
     */
    public static int locToBlock(double loc) {
        return (int)Math.floor(loc);
    }
}
