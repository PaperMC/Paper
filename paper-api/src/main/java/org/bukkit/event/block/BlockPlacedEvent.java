package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.Player;
import org.bukkit.event.Cancellable;

/**
 * Not implemented yet
 */
public class BlockPlacedEvent extends BlockEvent implements Cancellable {
    private boolean cancel;
    private Player player;

    public BlockPlacedEvent(Type type, Block theBlock) {
        super(type, theBlock);
        cancel = false;
    }

    /**
     * Gets the player who placed this block
     *
     * @return Player who placed the block
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
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
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
