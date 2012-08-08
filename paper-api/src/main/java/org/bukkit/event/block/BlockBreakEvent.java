package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block is broken by a player.
 * <p />
 * If you wish to have the block drop experience, you must set the experience value above 0.
 * By default, experience will be set in the event if:
 * <ol>
 * <li />The player is not in creative or adventure mode
 * <li />The player can loot the block (ie: does not destroy it completely, by using the correct tool)
 * <li />The player does not have silk touch
 * <li />The block drops experience in vanilla MineCraft
 * </ol>
 * <p />
 * Note:
 * Plugins wanting to simulate a traditional block drop should set the block to air and utilize their own methods for determining
 *   what the default drop for the block being broken is and what to do about it, if anything.
 * <p />
 * If a Block Break event is cancelled, the block will not break and experience will not drop.
 */
public class BlockBreakEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private boolean cancel;
    private int exp;

    public BlockBreakEvent(final Block theBlock, final Player player) {
        super(theBlock);
        this.player = player;
    }

    /**
     * Gets the Player that is breaking the block involved in this event.
     *
     * @return The Player that is breaking the block involved in this event
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the experience to drop after the block is broken.
     *
     * @return The experience to drop
     */
    public int getExpToDrop() {
        return exp;
    }

    /**
     * Set the amount of experience to drop after the block is broken.
     *
     * @param exp 1 or higher to drop experience, or else nothing will drop
     */
    public void setExpToDrop(int exp) {
        this.exp = exp;
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
