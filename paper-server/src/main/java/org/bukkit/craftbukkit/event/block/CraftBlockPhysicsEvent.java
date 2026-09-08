package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPhysicsEvent;

public class CraftBlockPhysicsEvent extends CraftBlockEvent implements BlockPhysicsEvent {

    private final BlockData changed;
    private final Block sourceBlock;

    private boolean cancelled;

    public CraftBlockPhysicsEvent(final Block block, final BlockData changed, final Block sourceBlock) {
        super(block);
        this.changed = changed;
        this.sourceBlock = sourceBlock;
    }

    public CraftBlockPhysicsEvent(final Level level, final BlockPos pos) {
        final Block block = CraftBlock.at(level, pos);
        this(block, block.getBlockData(), block);
    }

    public CraftBlockPhysicsEvent(final Level level, final BlockPos pos, final BlockState changed) {
        final Block block = CraftBlock.at(level, pos);
        this(block, changed.asBlockData(), block);
    }

    public CraftBlockPhysicsEvent(final Level level, final BlockPos pos, final BlockState changed, final BlockPos sourcePos) {
        this(CraftBlock.at(level, pos), changed.asBlockData(), CraftBlock.at(level, sourcePos));
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
