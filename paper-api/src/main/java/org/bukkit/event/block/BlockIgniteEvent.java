package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * @author SpeaKeasY
 *
 * Represents a block ignite event.
 */
public class BlockIgniteEvent extends BlockEvent implements Cancellable {
    private IgniteCause cause;
    private boolean cancel;
    private Player thePlayer;

    /**
     * @param Block, IgniteCause, Player or null.
     */
    public BlockIgniteEvent(Block theBlock, IgniteCause cause, Player thePlayer) {
        super(Event.Type.BLOCK_IGNITE, theBlock);
        this.cause = cause;
        this.thePlayer = thePlayer;
        this.cancel = false;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * If an ignite event is cancelled, the block will not be ignited.
     * This will not fire an event.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * If an ignite event is cancelled, the block will not be ignited.
     * This will not fire an event.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the cause of block ignite.
     * @return An IgniteCause value detailing the cause of block ignition.
     */
    public IgniteCause getCause()
    {
        return cause;
    }

    /**
     * Gets the player who ignited this block
     *
     * @return Player who placed the block, if not ignited by player returns null.
     */
    public Player getPlayer() {
        return thePlayer;
    }

    /**
     * An enum to specify the cause of the ignite
     */
    public enum IgniteCause {
        /**
         * Block ignition caused by lava.
         */
        LAVA,
        /**
         * Block ignition caused by player using flint-and-steel.
         */
        FLINT_AND_STEEL,
        /**
         * Block ignition caused by dynamic spreading of fire.
         */
        SPREAD,
    }

}
