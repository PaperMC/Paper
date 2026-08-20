package org.bukkit.craftbukkit.event.block;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExplodeEvent;

public class CraftBlockExplodeEvent extends CraftBlockEvent implements BlockExplodeEvent {

    private final BlockState blockState;
    private final List<Block> blocks;
    private float yield;
    private final ExplosionResult result;

    private boolean cancelled;

    public CraftBlockExplodeEvent(final Block block, final BlockState blockState, final List<Block> blocks, final float yield, final ExplosionResult result) {
        super(block);
        this.blockState = blockState;
        this.blocks = blocks;
        this.yield = yield;
        this.result = result;
    }

    @Override
    public ExplosionResult getExplosionResult() {
        return this.result;
    }

    @Override
    public BlockState getExplodedBlockState() {
        return this.blockState;
    }

    @Override
    public List<Block> blockList() {
        return this.blocks;
    }

    @Override
    public float getYield() {
        return this.yield;
    }

    @Override
    public void setYield(final float yield) {
        this.yield = yield;
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
        return BlockExplodeEvent.getHandlerList();
    }
}
