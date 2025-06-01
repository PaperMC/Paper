package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;

/**
 * Represents a dried ghast block data.
 */
public interface DriedGhast extends Directional, Waterlogged {

    /**
     * Gets the hydration level of this dried ghast.
     *
     * @return the hydration level, reaching from 0 to {@link #getMaximumHydration()}
     */
    int getHydration();

    /**
     * Sets the hydration level of this dried ghast.
     *
     * @return the hydration level, reaching from 0 to {@link #getMaximumHydration()}
     */
    void setHydration(final int hydration);

    /**
     * Provides the maximum hydration level this dried ghast can reach.
     *
     * @return the maximum level.
     */
    int getMaximumHydration();

}
