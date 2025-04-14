package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a blocks {@link BlockData} gets changed.
 * <p>
 * Examples include placing a block or modifying one using commands such as the setblock command.
 */
@NullMarked
public class BlockDataChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private BlockData newData;

    private boolean cancelled;

    @ApiStatus.Internal
    public BlockDataChangeEvent(final Block block, final BlockData newData) {
        super(block);
        this.newData = newData;
    }

    /**
     * Gets the new {@link BlockData} of the block.
     *
     * @return The new {@link BlockData}
     */
    public BlockData getNewData() {
        return newData;
    }

    /**
     * Sets the new {@link BlockData} of the block.
     *
     * @param newData The new {@link BlockData}
     */
    public void setNewData(final BlockData newData) {
        this.newData = newData;
    }

    /**
     * Gets whether the changing of the block data should be cancelled or not.
     *
     * @return Whether the changing of the block data should be cancelled or not.
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets whether the changing of the block data should be cancelled or not.
     *
     * @param cancel whether the changing of the block data should be cancelled or not.
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
