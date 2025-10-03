package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the fluid level of a block changes due to changes in adjacent
 * blocks.
 */
public class FluidLevelChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private BlockData newData;
    private boolean cancelled;

    @ApiStatus.Internal
    public FluidLevelChangeEvent(@NotNull Block fluid, @NotNull BlockData newData) {
        super(fluid);
        this.newData = newData;
    }

    /**
     * Gets the new data of the changed block.
     *
     * @return new data
     */
    @NotNull
    public BlockData getNewData() {
        return this.newData;
    }

    /**
     * Sets the new data of the changed block. Must be of the same Material as
     * the old one.
     *
     * @param newData the new data
     */
    public void setNewData(@NotNull BlockData newData) {
        Preconditions.checkArgument(newData != null, "newData null");
        Preconditions.checkArgument(this.newData.getMaterial().equals(newData.getMaterial()), "Cannot change fluid type");

        this.newData = newData.clone();
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
