package org.bukkit.craftbukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPhysicsEvent;

public class CraftBlockPhysicsEvent extends CraftBlockEvent implements BlockPhysicsEvent {

    private final BlockData changed;
    private final Block sourceBlock;

    private boolean cancelled;

    @Deprecated(forRemoval = true)
    public CraftBlockPhysicsEvent(final Block block, final BlockData changed, final int sourceX, final int sourceY, final int sourceZ) {
        this(block, changed, block.getWorld().getBlockAt(sourceX, sourceY, sourceZ));
    }

    public CraftBlockPhysicsEvent(final Block block, final BlockData changed) {
        this(block, changed, block);
    }

    public CraftBlockPhysicsEvent(final Block block, final BlockData changed, final Block sourceBlock) {
        super(block);
        this.changed = changed;
        this.sourceBlock = sourceBlock;
    }

    @Override
    public Block getSourceBlock() {
        return this.sourceBlock;
    }

    @Override
    public Material getChangedType() {
        return this.changed.getMaterial();
    }

    @Override
    public BlockData getChangedBlockData() {
        return this.changed.clone();
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
        return BlockPhysicsEvent.getHandlerList();
    }
}
