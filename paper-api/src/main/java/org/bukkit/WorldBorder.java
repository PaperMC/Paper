package org.bukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.util.Tick;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public interface WorldBorder {

    /**
     * Get the {@link World} in which the border resides.
     *
     * @return the associated world, or null if this world border is not associated
     * with any specific world, such as those created via {@link Server#createWorldBorder()}
     */
    @Nullable World getWorld();

    /**
     * Resets the border to default values.
     */
    void reset();

    /**
     * Gets the current side length of the border.
     *
     * @return The current side length of the border.
     */
    double getSize();

    /**
     * Sets the border to a square region with the specified side length in blocks.
     *
     * @param newSize The new size of the border.
     *
     * @throws IllegalArgumentException if newSize is less than 1.0D or greater than {@link #getMaxSize()}
     * @see #changeSize(double, long)
     */
    void setSize(double newSize);

    /**
     * Sets the border to a square region with the specified side length in blocks.
     *
     * @param newSize The new side length of the border.
     * @param ticks The time in ticks in which the border grows or shrinks from the previous size to that being set.
     *
     * @throws IllegalArgumentException if newSize is less than 1.0D or greater than {@link #getMaxSize()}
     * @throws IllegalArgumentException if ticks is out of range
     * @see #setSize(double)
     */
    void changeSize(double newSize, @Range(from = 0, to = Integer.MAX_VALUE) long ticks);

    /**
     * Sets the border to a square region with the specified side length in blocks.
     *
     * @param newSize The new side length of the border.
     * @param seconds The time in seconds in which the border grows or shrinks from the previous size to that being set.
     *
     * @throws IllegalArgumentException if seconds is out of range once converted in ticks
     * @deprecated Use {@link #changeSize(double, long)} instead
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    default void setSize(double newSize, long seconds) {
        this.changeSize(Math.clamp(newSize, 1.0D, this.getMaxSize()), Tick.tick().fromDuration(Duration.ofSeconds(seconds)));
    }

    /**
     * Sets the border to a square region with the specified side length in blocks.
     *
     * @param newSize The new side length of the border.
     * @param unit The time unit.
     * @param time The time in which the border grows or shrinks from the previous size to that being set.
     *
     * @throws IllegalArgumentException if newSize is less than 1.0D or greater than {@link #getMaxSize()}
     * @throws IllegalArgumentException if time is out of range once converted in ticks
     * @deprecated Use {@link #changeSize(double, long)} instead
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    default void setSize(double newSize, @NotNull TimeUnit unit, @NonNegative long time) {
        Preconditions.checkArgument(unit != null, "TimeUnit cannot be null.");
        this.changeSize(newSize, Tick.tick().fromDuration(Duration.of(time, unit.toChronoUnit())));
    }

    /**
     * Gets the current border center.
     *
     * @return The current border center.
     */
    @NotNull Location getCenter();

    /**
     * Sets the new border center.
     *
     * @param x The new center x-coordinate.
     * @param z The new center z-coordinate.
     *
     * @throws IllegalArgumentException if the absolute value of x or z is higher than {@link #getMaxCenterCoordinate()}
     */
    void setCenter(double x, double z);

    /**
     * Sets the new border center.
     *
     * @param location The new location of the border center. (Only x/z used)
     *
     * @throws IllegalArgumentException if location is <code>null</code> or the absolute value of {@link Location#getX()} or {@link Location#getZ()} is higher than {@link #getMaxCenterCoordinate()}
     */
    void setCenter(@NotNull Location location);

    /**
     * Gets the current border damage buffer.
     *
     * @return The current border damage buffer.
     */
    double getDamageBuffer();

    /**
     * Sets the amount of blocks a player may safely be outside the border before taking damage.
     *
     * @param blocks The amount of blocks. (The default is 5 blocks.)
     */
    void setDamageBuffer(double blocks);

    /**
     * Gets the current border damage amount.
     *
     * @return The current border damage amount.
     */
    double getDamageAmount();

    /**
     * Sets the amount of damage a player takes when outside the border plus the border buffer.
     *
     * @param damage The amount of damage. (The default is 0.2 damage per second per block.)
     */
    void setDamageAmount(double damage);

    /**
     * Gets the current border warning time in seconds.
     *
     * @return The current border warning time in seconds.
     * @deprecated Use {@link #getWarningTimeTicks()} instead
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    default @NonNegative int getWarningTime() {
        return (int) Tick.of(this.getWarningTimeTicks()).toSeconds();
    }

    /**
     * Sets the warning time that causes the screen to be tinted red when a contracting border will reach the player within the specified time.
     *
     * @param seconds The amount of time in seconds.
     * @deprecated Use {@link #setWarningTimeTicks(int)} instead
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    default void setWarningTime(@NonNegative int seconds) {
        Preconditions.checkArgument(seconds >= 0, "seconds cannot be lower than 0");

        this.setWarningTimeTicks(Tick.tick().fromDuration(Duration.ofSeconds(seconds)));
    }

    /**
     * Gets the current border warning time in ticks.
     *
     * @return The current border warning time in ticks.
     */
    @NonNegative int getWarningTimeTicks();

    /**
     * Sets the warning time that causes the screen to be tinted red when a contracting border will reach the player within the specified time.
     *
     * @param ticks The number of ticks.
     */
    void setWarningTimeTicks(@NonNegative int ticks);

    /**
     * Gets the current border warning distance.
     *
     * @return The current border warning distance.
     */
    int getWarningDistance();

    /**
     * Sets the warning distance that causes the screen to be tinted red when the player is within the specified number of blocks from the border.
     *
     * @param distance The distance in blocks. (The default is 5 blocks.)
     */
    void setWarningDistance(int distance);

    /**
     * Check if the specified location is inside this border.
     *
     * @param location the location to check
     * @return if this location is inside the border or not
     */
    boolean isInside(@NotNull Location location);

    /**
     * Gets the maximum possible size of a WorldBorder.
     *
     * @return The maximum size the WorldBorder
     */
    double getMaxSize();

    /**
     * Gets the absolute value of the maximum x/z center coordinate of a
     * WorldBorder.
     *
     * @return The absolute maximum center coordinate of the WorldBorder
     */
    double getMaxCenterCoordinate();
}
