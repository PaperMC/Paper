package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

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
public class BlockFormEvent extends BlockGrowEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public BlockFormEvent(final Block block, final BlockState newState) {
        super(block, newState);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
