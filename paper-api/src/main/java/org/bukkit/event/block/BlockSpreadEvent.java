package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
/**
 *Represents any event that spreads blocks
 */
public class BlockSpreadEvent extends BlockFormEvent {
    private Block source;

    public BlockSpreadEvent(Block block, Block source, BlockState newState) {
        super(Type.BLOCK_SPREAD, block, newState);
        this.source = source;
    }

    /**
     * Gets the source block
     *
     * @return the block state
     */
    public Block getSource() {
        return source;
    }
}
