package io.papermc.paper.block;

import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MovingPiston extends TileState {

    /**
     * Gets the block that is being pushed
     *
     * @return the pushed block
     */
    BlockData getMovingBlock();

    /**
     * The direction that the current moving piston
     * is pushing/pulling a block in.
     *
     * @return the direction
     */
    BlockFace getDirection();

    /**
     * Gets if the piston is extending or not.
     * Returns false if the piston is retracting.
     *
     * @return is extending or not
     */
    boolean isExtending();

    /**
     * Returns if this moving piston represents the main piston head
     * from the original piston.
     *
     * @return is the piston head or not
     */
    boolean isPistonHead();

}
