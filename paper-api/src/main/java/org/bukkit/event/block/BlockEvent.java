package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Event;

/**
 * Represents a block related event.
 */
public interface BlockEvent extends Event {

    /**
     * Gets the block involved in this event.
     *
     * @return The Block which block is involved in this event
     */
    Block getBlock();
}
