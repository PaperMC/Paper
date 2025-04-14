package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a block fades, melts or disappears based on world conditions
 * <p>
 * Examples:
 * <ul>
 * <li>Snow melting due to being near a light source.
 * <li>Ice melting due to being near a light source.
 * <li>Fire burning out after time, without destroying fuel block.
 * <li>Coral fading to dead coral due to lack of water</li>
 * <li>Turtle Egg bursting when a turtle hatches</li>
 * </ul>
 * <p>
 * If this event is cancelled, the block will not fade, melt or
 * disappear.
 */
public class BlockFadeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BlockState newState;
    private boolean cancelled;

    @ApiStatus.Internal
    public BlockFadeEvent(@NotNull final Block block, @NotNull final BlockState newState) {
        super(block);
        this.newState = newState;
    }

    /**
     * Gets the state of the new block that will replace the block
     * fading, melting or disappearing.
     *
     * @return The block state of the new block that replaces the block
     *     fading, melting or disappearing
     */
    @NotNull
    public BlockState getNewState() {
        return this.newState;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
