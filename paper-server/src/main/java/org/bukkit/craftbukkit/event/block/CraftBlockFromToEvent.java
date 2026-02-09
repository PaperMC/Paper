package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFromToEvent;
import org.jspecify.annotations.Nullable;

public class CraftBlockFromToEvent extends CraftBlockEvent implements BlockFromToEvent {

    protected @Nullable Block toBlock;
    protected BlockFace face;

    protected boolean cancelled;

    public CraftBlockFromToEvent(final Block block, final BlockFace face) {
        super(block);
        this.face = face;
    }

    public CraftBlockFromToEvent(final Block block, final Block toBlock) {
        this(block, BlockFace.SELF);
        this.toBlock = toBlock;
    }

    @Override
    public BlockFace getFace() {
        return this.face;
    }

    @Override
    public Block getToBlock() {
        if (this.toBlock == null) {
            this.toBlock = this.block.getRelative(this.face);
        }
        return this.toBlock;
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
        return BlockFromToEvent.getHandlerList();
    }
}
