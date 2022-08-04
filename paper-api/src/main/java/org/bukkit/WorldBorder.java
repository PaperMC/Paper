package org.bukkit;

import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WorldBorder {

    /**
     * Get the {@link World} in which the border resides.
     *
     * @return the associated world, or null if this world border is not associated
     * with any specific world, such as those created via {@link Server#createWorldBorder()}
     */
    @Nullable
    public World getWorld();

    /**
     * Resets the border to default values.
     */
    public void reset();

    /**
     * Gets the current side length of the border.
     *
     * @return The current side length of the border.
     */
    public double getSize();

    /**
     * Sets the border to a square region with the specified side length in blocks.
     *
     * @param newSize The new size of the border.
     *
     * @throws IllegalArgumentException if newSize is less than 1.0D or greater than {@link #getMaxSize()}
     */
    public void setSize(double newSize);

    /**
     * Sets the border to a square region with the specified side length in blocks.
     *
     * @param newSize The new side length of the border.
     * @param seconds The time in seconds in which the border grows or shrinks from the previous size to that being set.
     *
     * @throws IllegalArgumentException if newSize is less than 1.0D or greater than {@link #getMaxSize()}
     */
    public void setSize(double newSize, long seconds);

    /**
     * Sets the border to a square region with the specified side length in blocks.
     *
     * @param newSize The new side length of the border.
     * @param unit The time unit.
     * @param time The time in which the border grows or shrinks from the previous size to that being set.
     *
     * @throws IllegalArgumentException if unit is <code>null</code> or newSize is less than 1.0D or greater than {@link #getMaxSize()}
     */
    public void setSize(double newSize, @NotNull TimeUnit unit, long time);

    /**
     * Gets the current border center.
     *
     * @return The current border center.
     */
    @NotNull
    public Location getCenter();

    /**
     * Sets the new border center.
     *
     * @param x The new center x-coordinate.
     * @param z The new center z-coordinate.
     *
     * @throws IllegalArgumentException if the absolute value of x or z is higher than {@link #getMaxCenterCoordinate()}
     */
    public void setCenter(double x, double z);

    /**
     * Sets the new border center.
     *
     * @param location The new location of the border center. (Only x/z used)
     *
     * @throws IllegalArgumentException if location is <code>null</code> or the absolute value of {@link Location#getX()} or {@link Location#getZ()} is higher than {@link #getMaxCenterCoordinate()}
     */
    public void setCenter(@NotNull Location location);

    /**
     * Gets the current border damage buffer.
     *
     * @return The current border damage buffer.
     */
    public double getDamageBuffer();

    /**
     * Sets the amount of blocks a player may safely be outside the border before taking damage.
     *
     * @param blocks The amount of blocks. (The default is 5 blocks.)
     */
    public void setDamageBuffer(double blocks);

    /**
     * Gets the current border damage amount.
     *
     * @return The current border damage amount.
     */
    public double getDamageAmount();

    /**
     * Sets the amount of damage a player takes when outside the border plus the border buffer.
     *
     * @param damage The amount of damage. (The default is 0.2 damage per second per block.)
     */
    public void setDamageAmount(double damage);

    /**
     * Gets the current border warning time in seconds.
     *
     * @return The current border warning time in seconds.
     */
    public int getWarningTime();

    /**
     * Sets the warning time that causes the screen to be tinted red when a contracting border will reach the player within the specified time.
     *
     * @param seconds The amount of time in seconds. (The default is 15 seconds.)
     */
    public void setWarningTime(int seconds);

    /**
     * Gets the current border warning distance.
     *
     * @return The current border warning distance.
     */
    public int getWarningDistance();

    /**
     * Sets the warning distance that causes the screen to be tinted red when the player is within the specified number of blocks from the border.
     *
     * @param distance The distance in blocks. (The default is 5 blocks.)
     */
    public void setWarningDistance(int distance);

    /**
     * Check if the specified location is inside this border.
     *
     * @param location the location to check
     * @return if this location is inside the border or not
     */
    public boolean isInside(@NotNull Location location);

    /**
     * Gets the maximum possible size of a WorldBorder.
     *
     * @return The maximum size the WorldBorder
     */
    public double getMaxSize();

    /**
     * Gets the absolute value of the maximum x/z center coordinate of a
     * WorldBorder.
     *
     * @return The absolute maximum center coordinate of the WorldBorder
     */
    public double getMaxCenterCoordinate();
}
