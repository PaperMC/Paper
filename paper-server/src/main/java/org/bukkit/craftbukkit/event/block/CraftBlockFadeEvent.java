package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFadeEvent;

public class CraftBlockFadeEvent extends CraftBlockEvent implements BlockFadeEvent {

    private final BlockState newState;
    private boolean cancelled;

    public CraftBlockFadeEvent(final Block block, final BlockState newState) {
        super(block);
        this.newState = newState;
    }

    public CraftBlockFadeEvent(final LevelAccessor level, final BlockPos pos, final net.minecraft.world.level.block.state.BlockState newState) {
        final org.bukkit.block.Block block = CraftBlock.at(level, pos);
        final CraftBlockState snapshot = CraftBlockStates.snapshotOfSimpleBlock(block, newState);

        this(block, snapshot);
    }

    @Override
    public BlockState getNewState() {
        return this.newState;
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
        return BlockFadeEvent.getHandlerList();
    }
}
