package io.papermc.paper.event.player;

import org.bukkit.block.Block;

/**
 * Event that is fired when a player uses the pick item functionality on a block
 * (middle-clicking a block to get the appropriate item).
 * After the handling of this event, the contents of the source and the target slot will be swapped,
 * and the currently selected hotbar slot of the player will be set to the target slot.
 */
public interface PlayerPickBlockEvent extends PlayerPickItemEvent {

    /**
     * Retrieves the block associated with this event.
     *
     * @return the block involved in the event
     */
    Block getBlock();
}
