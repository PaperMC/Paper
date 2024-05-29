package org.bukkit.inventory.meta;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a compass that can track a specific location.
 */
public interface CompassMeta extends ItemMeta {

    /**
     * Checks if this compass has been paired to a lodestone.
     *
     * @return paired status
     */
    boolean hasLodestone();

    /**
     * Gets the location that this compass will point to.
     *
     * Check {@link #hasLodestone()} first!
     *
     * @return lodestone location
     */
    @Nullable
    Location getLodestone();

    /**
     * Sets the location this lodestone compass will point to.
     *
     * @param lodestone new location or null to clear the targeted location
     * @see #clearLodestone() to reset the compass to a normal compass
     */
    void setLodestone(@Nullable Location lodestone);

    /**
     * Gets if this compass is tracking a specific lodestone.
     *
     * If true the compass will only work if there is a lodestone at the tracked
     * location.
     *
     * @return lodestone tracked
     */
    boolean isLodestoneTracked();

    /**
     * Sets if this compass is tracking a specific lodestone.
     *
     * If true the compass will only work if there is a lodestone at the tracked
     * location.
     *
     * @param tracked new tracked status
     * @see #clearLodestone() to reset the compass to a normal compass
     */
    void setLodestoneTracked(boolean tracked);

    // Paper start - Add more lodestone compass methods
    /**
     * Checks if this compass is considered a lodestone compass.
     * @see #hasLodestone() to check if a position is being tracked
     * @see #isLodestoneTracked() to check if it verifies the position is a lodestone
     */
    boolean isLodestoneCompass();

    /**
     * Reset this compass to a normal compass, removing any tracked
     * location.
     */
    void clearLodestone();
    // Paper end - Add more lodestone compass methods

    @Override
    CompassMeta clone();
}
