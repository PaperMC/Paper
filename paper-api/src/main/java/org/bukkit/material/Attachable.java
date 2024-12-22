package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

/**
 * Indicates that a block can be attached to another block
 *
 * @since 1.0.0 R1
 */
public interface Attachable extends Directional {

    /**
     * Gets the face that this block is attached on
     *
     * @return BlockFace attached to
     */
    @NotNull
    public BlockFace getAttachedFace();
}
