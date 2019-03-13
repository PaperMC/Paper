package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a block is formed or spreads based on world conditions.
 * <p>
 * Use {@link BlockSpreadEvent} to catch blocks that actually spread and don't
 * just "randomly" form.
 * <p>
 * Examples:
 * <ul>
 * <li>Snow forming due to a snow storm.
 * <li>Ice forming in a snowy Biome like Taiga or Tundra.
 * <li> Obsidian / Cobblestone forming due to contact with water.
 * <li> Concrete forming due to mixing of concrete powder and water.
 * </ul>
 * <p>
 * If a Block Form event is cancelled, the block will not be formed.
 *
 * @see BlockSpreadEvent
 */
public class BlockFormEvent extends BlockGrowEvent {
    private static final HandlerList handlers = new HandlerList();

    public BlockFormEvent(@NotNull final Block block, @NotNull final BlockState newState) {
        super(block, newState);
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
