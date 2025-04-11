package org.bukkit.event.block;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a block explodes.
 * <p>
 * Note that due to the nature of explosions, {@link #getBlock()} will always be
 * an air block. {@link #getExplodedBlockState()} should be used to get
 * information about the block state that exploded.
 * <p>
 * The event isn't called if the {@link org.bukkit.GameRule#MOB_GRIEFING}
 * is disabled as no block interaction will occur.
 */
public class BlockExplodeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BlockState blockState;
    private final List<Block> blocks;
    private float yield;
    private final ExplosionResult result;

    private boolean cancelled;

    @ApiStatus.Internal
    public BlockExplodeEvent(@NotNull final Block block, @NotNull final BlockState blockState, @NotNull final List<Block> blocks, final float yield, @NotNull final ExplosionResult result) {
        super(block);
        this.blockState = blockState;
        this.blocks = blocks;
        this.yield = yield;
        this.result = result;
    }

    /**
     * Returns the result of the explosion if it is not cancelled.
     *
     * @return the result of the explosion
     */
    @NotNull
    public ExplosionResult getExplosionResult() {
        return this.result;
    }

    /**
     * Returns the captured BlockState of the block that exploded.
     *
     * @return the block state
     */
    @NotNull
    public BlockState getExplodedBlockState() {
        return this.blockState;
    }

    /**
     * Returns the list of blocks that would have been removed or were removed
     * from the explosion event.
     *
     * @return All blown-up blocks
     */
    @NotNull
    public List<Block> blockList() {
        return this.blocks;
    }

    /**
     * Returns the percentage of blocks to drop from this explosion
     *
     * @return The yield.
     */
    public float getYield() {
        return this.yield;
    }

    /**
     * Sets the percentage of blocks to drop from this explosion
     *
     * @param yield The new yield percentage
     */
    public void setYield(float yield) {
        this.yield = yield;
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
