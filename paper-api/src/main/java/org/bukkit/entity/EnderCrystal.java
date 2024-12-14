package org.bukkit.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * A crystal that heals nearby EnderDragons
 *
 * @since 1.0.0 R1
 */
public interface EnderCrystal extends Entity {

    /**
     * Return whether or not this end crystal is showing the
     * bedrock slate underneath it.
     *
     * @return true if the bottom is being shown
     * @since 1.9.4
     */
    boolean isShowingBottom();

    /**
     * Sets whether or not this end crystal is showing the
     * bedrock slate underneath it.
     *
     * @param showing whether the bedrock slate should be shown
     * @since 1.9.4
     */
    void setShowingBottom(boolean showing);

    /**
     * Gets the location that this end crystal is pointing its beam to.
     *
     * @return the location that the beam is pointed to, or null if the beam is not shown
     * @since 1.9.4
     */
    @Nullable
    Location getBeamTarget();

    /**
     * Sets the location that this end crystal is pointing to. Passing a null
     * value will remove the current beam.
     *
     * @param location the location to point the beam to
     * @throws IllegalArgumentException for differing worlds
     * @since 1.9.4
     */
    void setBeamTarget(@Nullable Location location);
}
