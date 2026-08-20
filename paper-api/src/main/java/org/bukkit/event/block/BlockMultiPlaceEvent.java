package org.bukkit.event.block;

import java.util.List;
import org.bukkit.block.BlockState;

/**
 * Fired when a single block placement action of a player triggers the
 * creation of multiple blocks(e.g. placing a bed block). The block returned
 * by {@link #getBlockPlaced()} and its related methods is the block where
 * the placed block would exist if the placement only affected a single
 * block.
 */
public interface BlockMultiPlaceEvent extends BlockPlaceEvent {

    /**
     * Gets a list of block states for all blocks which were replaced by the
     * placement of the new blocks. Most of these blocks will just have a
     * Material type of AIR.
     *
     * @return immutable list of replaced BlockStates
     */
    List<BlockState> getReplacedBlockStates();
}
