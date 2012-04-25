package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block is ignited. If you want to catch when a Player places fire, you need to use {@link BlockPlaceEvent}.
 * <p />
 * If a Block Ignite event is cancelled, the block will not be ignited.
 */
public class BlockIgniteEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final IgniteCause cause;
    private boolean cancel;
    private final Player thePlayer;

    public BlockIgniteEvent(final Block theBlock, final IgniteCause cause, final Player thePlayer) {
        super(theBlock);
        this.cause = cause;
        this.thePlayer = thePlayer;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the cause of block ignite.
     *
     * @return An IgniteCause value detailing the cause of block ignition
     */
    public IgniteCause getCause() {
        return cause;
    }

    /**
     * Gets the player who ignited this block
     *
     * @return The Player who placed the fire block, if not ignited by a player returns null
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
         * Block ignition caused by a player using flint-and-steel.
         */
        FLINT_AND_STEEL,
        /**
         * Block ignition caused by dynamic spreading of fire.
         */
        SPREAD,
        /**
         * Block ignition caused by lightning.
         */
        LIGHTNING,
        /**
         * Block ignition caused by a player using a fireball.
         */
        FIREBALL,
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
