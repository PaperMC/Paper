package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a block related event.
 */
public abstract class BlockEvent extends Event {
    protected Block block;

    public BlockEvent(@NotNull final Block theBlock) {
        block = theBlock;
    }

    /**
     * Gets the block involved in this event.
     *
     * @return The Block which block is involved in this event
     */
    @NotNull
    public final Block getBlock() {
        return block;
    }
}
