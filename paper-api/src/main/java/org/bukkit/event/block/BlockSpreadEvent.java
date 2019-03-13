package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a block spreads based on world conditions.
 * <p>
 * Use {@link BlockFormEvent} to catch blocks that "randomly" form instead of
 * actually spread.
 * <p>
 * Examples:
 * <ul>
 * <li>Mushrooms spreading.
 * <li>Fire spreading.
 * </ul>
 * <p>
 * If a Block Spread event is cancelled, the block will not spread.
 *
 * @see BlockFormEvent
 */
public class BlockSpreadEvent extends BlockFormEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Block source;

    public BlockSpreadEvent(@NotNull final Block block, @NotNull final Block source, @NotNull final BlockState newState) {
        super(block, newState);
        this.source = source;
    }

    /**
     * Gets the source block involved in this event.
     *
     * @return the Block for the source block involved in this event.
     */
    @NotNull
    public Block getSource() {
        return source;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
