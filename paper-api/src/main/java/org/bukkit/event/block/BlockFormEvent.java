package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;

/**
 * Called when a block is formed or spreads based on world conditions.
 * Use {@link BlockSpreadEvent} to catch blocks that actually spread and don't just "randomly" form.
 * <p />
 * Examples:
 * <ul>
 * <li>Snow forming due to a snow storm.</li>
 * <li>Ice forming in a snowy Biome like Taiga or Tundra.</li>
 * </ul>
 * <p />
 * If a Block Form event is cancelled, the block will not be formed.
 *
 * @see BlockSpreadEvent
 */
@SuppressWarnings("serial")
public class BlockFormEvent extends BlockEvent implements Cancellable {
    private boolean cancelled;
    private BlockState newState;

    public BlockFormEvent(Block block, BlockState newState) {
        super(Type.BLOCK_FORM, block);
        this.block = block;
        this.newState = newState;
        this.cancelled = false;
    }

    public BlockFormEvent(Type type, Block block, BlockState newState) {
        super(type, block);
        this.block = block;
        this.newState = newState;
        this.cancelled = false;
    }

    /**
     * Gets the state of the block where it will form or spread to.
     *
     * @return The block state of the block where it will form or spread to
     */
    public BlockState getNewState() {
        return newState;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
