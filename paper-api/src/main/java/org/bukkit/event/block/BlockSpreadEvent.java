package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;

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
public class BlockSpreadEvent extends BlockFormEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Block source;

    public BlockSpreadEvent(final Block block, final Block source, final BlockState newState) {
        super(block, newState);
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
