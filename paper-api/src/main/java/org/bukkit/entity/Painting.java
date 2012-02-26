package org.bukkit.entity;

import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.material.Attachable;

/**
 * Represents a Painting.
 */
public interface Painting extends Entity, Attachable {
    /**
     * Get the art on this painting
     *
     * @return The art
     */
    public Art getArt();

    /**
     * Set the art on this painting
     *
     * @param art The new art
     * @return False if the new art won't fit at the painting's current location
     */
    public boolean setArt(Art art);

    /**
     * Set the art on this painting
     *
     * @param art The new art
     * @param force If true, force the new art regardless of whether it fits at the current location
     *            Note that forcing it where it can't fit normally causes it to drop as an item unless you override
     *            this by catching the {@link PaintingBreakEvent}.
     * @return False if force was false and the new art won't fit at the painting's current location
     */
    public boolean setArt(Art art, boolean force);

    /**
     * Sets the direction of the painting, potentially overriding rules of placement. Note that if the result
     * is not valid the painting would normally drop as an item.
     *
     * @param face The new direction.
     * @param force Whether to force it.
     * @return False if force was false and there was no block for it to attach to in order to face the given direction.
     */
    public boolean setFacingDirection(BlockFace face, boolean force);
}
