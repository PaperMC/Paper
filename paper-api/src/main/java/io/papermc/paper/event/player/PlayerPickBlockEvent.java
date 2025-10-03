package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Event that is fired when a player uses the pick item functionality on a block
 * (middle-clicking a block to get the appropriate item).
 * After the handling of this event, the contents of the source and the target slot will be swapped,
 * and the currently selected hotbar slot of the player will be set to the target slot.
 */
@NullMarked
public class PlayerPickBlockEvent extends PlayerPickItemEvent {

    private final Block block;

    @ApiStatus.Internal
    public PlayerPickBlockEvent(final Player player, final Block block, final boolean includeData, final int targetSlot, final int sourceSlot) {
        super(player, includeData, targetSlot, sourceSlot);
        this.block = block;
    }

    /**
     * Retrieves the block associated with this event.
     *
     * @return the block involved in the event
     */
    public Block getBlock() {
        return this.block;
    }
}
