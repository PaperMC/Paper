package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.event.Event;

/**
 * Holds information for events with a source block and a destination block
 */
public class BlockFromToEvent extends BlockEvent {
    protected Block from;

    public BlockFromToEvent(final Event.Type type, final Block from, final Block to) {
        super(type, to);
        this.from = from;
    }

    /**
     * Gets the location this player moved to
     *
     * @return Block the block is event originated from
     */
    public Block getFrom() {
        return from;
    }

    /**
     * Gets the target block of this event
     *
     * @return Block the block containing this event
     */
    public Block getTo() {
        return getBlock();
    }
}
