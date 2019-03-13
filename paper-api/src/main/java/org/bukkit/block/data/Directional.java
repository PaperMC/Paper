package org.bukkit.block.data;

import java.util.Set;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

/**
 * 'facing' represents the face towards which the block is pointing.
 * <br>
 * Some blocks may not be able to face in all directions, use
 * {@link #getFaces()} to get all possible directions for this block.
 */
public interface Directional extends BlockData {

    /**
     * Gets the value of the 'facing' property.
     *
     * @return the 'facing' value
     */
    @NotNull
    BlockFace getFacing();

    /**
     * Sets the value of the 'facing' property.
     *
     * @param facing the new 'facing' value
     */
    void setFacing(@NotNull BlockFace facing);

    /**
     * Gets the faces which are applicable to this block.
     *
     * @return the allowed 'facing' values
     */
    @NotNull
    Set<BlockFace> getFaces();
}
