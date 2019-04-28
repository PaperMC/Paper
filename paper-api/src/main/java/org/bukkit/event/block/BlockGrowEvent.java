package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a block grows naturally in the world.
 * <p>
 * Examples:
 * <ul>
 * <li>Wheat
 * <li>Sugar Cane
 * <li>Cactus
 * <li>Watermelon
 * <li>Pumpkin
 * <li>Turtle Egg
 * </ul>
 * <p>
 * If a Block Grow event is cancelled, the block will not grow.
 */
public class BlockGrowEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final BlockState newState;
    private boolean cancelled = false;

    public BlockGrowEvent(@NotNull final Block block, @NotNull final BlockState newState) {
        super(block);
        this.newState = newState;
    }

    /**
     * Gets the state of the block where it will form or spread to.
     *
     * @return The block state for this events block
     */
    @NotNull
    public BlockState getNewState() {
        return newState;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
