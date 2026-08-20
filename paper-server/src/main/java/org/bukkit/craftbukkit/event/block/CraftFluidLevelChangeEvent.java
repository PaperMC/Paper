package org.bukkit.craftbukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.FluidLevelChangeEvent;

public class CraftFluidLevelChangeEvent extends CraftBlockEvent implements FluidLevelChangeEvent {

    private BlockData newData;
    private boolean cancelled;

    public CraftFluidLevelChangeEvent(final Block fluid, final BlockData newData) {
        super(fluid);
        this.newData = newData;
    }

    @Override
    public BlockData getNewData() {
        return this.newData;
    }

    @Override
    public void setNewData(final BlockData newData) {
        Preconditions.checkArgument(newData != null, "newData null");
        Preconditions.checkArgument(this.newData.getMaterial().equals(newData.getMaterial()), "Cannot change fluid type");

        this.newData = newData.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return FluidLevelChangeEvent.getHandlerList();
    }
}
