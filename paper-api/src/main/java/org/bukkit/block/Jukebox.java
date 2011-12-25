package org.bukkit.block;

import org.bukkit.Material;

/**
 * Represents a Jukebox
 */
public interface Jukebox extends BlockState {
    /**
     * Get the record currently playing
     *
     * @return The record Material, or AIR if none is playing
     */
    public Material getPlaying();

    /**
     * Set the record currently playing
     *
     * @param record The record Material, or null/AIR to stop playing
     */
    public void setPlaying(Material record);

    /**
     * Check if the jukebox is currently playing a record
     *
     * @return True if there is a record playing
     */
    public boolean isPlaying();

    /**
     * Stop the jukebox playing and eject the current record
     *
     * @return True if a record was ejected; false if there was none playing
     */
    public boolean eject();
}
