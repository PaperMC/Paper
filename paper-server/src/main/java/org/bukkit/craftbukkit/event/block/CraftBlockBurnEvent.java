package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBurnEvent;
import org.jspecify.annotations.Nullable;

public class CraftBlockBurnEvent extends CraftBlockEvent implements BlockBurnEvent {

    private final Block ignitingBlock;
    private boolean cancelled;

    public CraftBlockBurnEvent(final Block block, final @Nullable Block ignitingBlock) {
        super(block);
        this.ignitingBlock = ignitingBlock;
    }

    @Override
    public @Nullable Block getIgnitingBlock() {
        return this.ignitingBlock;
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
        return BlockBurnEvent.getHandlerList();
    }
}
