package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Called when a block is broken by a player
 */
public class BlockBreakEvent extends BlockEvent implements Cancellable {

    private Player player;
    private boolean cancel;

    public BlockBreakEvent(final Block theBlock, Player player) {
        super(Type.BLOCK_BREAK, theBlock);
        this.player = player;
        this.cancel = false;
    }

    /**
     * Gets the Player that is breaking the block
     *
     * @return the Player that is breaking the block
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a block break event is cancelled, the block will not break.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a block break event is cancelled, the block will not break.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
