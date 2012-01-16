package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block is broken by a player.
 * <p />
 * Note:
 * Plugins wanting to simulate a traditional block drop should set the block to air and utilise their own methods for determining
 *   what the default drop for the block being broken is and what to do about it, if anything.
 * <p />
 * If a Block Break event is cancelled, the block will not break.
 */
@SuppressWarnings("serial")
public class BlockBreakEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private boolean cancel;

    public BlockBreakEvent(final Block theBlock, Player player) {
        super(Type.BLOCK_BREAK, theBlock);
        this.player = player;
        this.cancel = false;
    }

    /**
     * Gets the Player that is breaking the block involved in this event.
     *
     * @return The Player that is breaking the block involved in this event
     */
    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
