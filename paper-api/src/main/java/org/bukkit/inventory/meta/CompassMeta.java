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
     * @param lodestone new location or null to clear
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
     */
    void setLodestoneTracked(boolean tracked);

    @Override
    CompassMeta clone();
}
