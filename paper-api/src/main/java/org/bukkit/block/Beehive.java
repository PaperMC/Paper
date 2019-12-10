package org.bukkit.block;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a bee hive.
 */
public interface Beehive extends TileState {

    /**
     * Get the hive's flower location.
     *
     * @return flower location or null
     */
    @Nullable
    Location getFlower();

    /**
     * Set the hive's flower location.
     *
     * @param location or null
     */
    void setFlower(@Nullable Location location);
}
