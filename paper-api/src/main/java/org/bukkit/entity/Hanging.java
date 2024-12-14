package org.bukkit.entity;

import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Hanging entity
 *
 * @since 1.4.5 R1.0
 */
public interface Hanging extends Entity, Attachable {

    /**
     * Sets the direction of the hanging entity, potentially overriding rules
     * of placement. Note that if the result is not valid the object would
     * normally drop as an item.
     *
     * @param face The new direction.
     * @param force Whether to force it.
     * @return False if force was false and there was no block for it to
     *     attach to in order to face the given direction.
     */
    public boolean setFacingDirection(@NotNull BlockFace face, boolean force);
}
