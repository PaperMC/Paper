package org.bukkit.block.data;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;


/**
 * A marker interface for block data that is both {@link FaceAttachable} and {@link Directional}.
 * <p>
 * This represents blocks that are mounted on a specific face of another block (e.g., buttons, levers)
 * and also have a facing direction.
 */
public interface AttachableDirectional extends FaceAttachable, Directional {

    /**
     * Gets the {@link BlockFace} of the block that this block is physically attached to.
     * This is determined by the attached face.
     * @return the block face to which this block is attached
     * */
    @NotNull
    default BlockFace getAttachedBlockFace() {
        return switch (getAttachedFace()) {
            case FLOOR -> BlockFace.DOWN;
            case CEILING -> BlockFace.UP;
            case WALL -> {
                BlockFace attached = getFacing();
                yield attached.getOppositeFace();
            }
        };
    };
}
