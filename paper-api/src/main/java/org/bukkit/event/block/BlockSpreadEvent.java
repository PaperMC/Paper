package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

/**
 * Called when a block spreads based on world conditions.
 * Use {@link BlockFormEvent} to catch blocks that "randomly" form instead of actually spread.
 * <p />
 * Examples:
 * <ul>
 * <li>Mushrooms spreading.</li>
 * <li>Fire spreading.</li>
 * </ul>
 * <p />
 * If a Block Spread event is cancelled, the block will not spread.
 *
 * @see BlockFormEvent
 */
@SuppressWarnings("serial")
public class BlockSpreadEvent extends BlockFormEvent {
    private Block source;

    public BlockSpreadEvent(Block block, Block source, BlockState newState) {
        super(Type.BLOCK_SPREAD, block, newState);
        this.source = source;
    }

    /**
     * Gets the source block involved in this event.
     *
     * @return the Block for the source block involved in this event.
     */
    public Block getSource() {
        return source;
    }
}
