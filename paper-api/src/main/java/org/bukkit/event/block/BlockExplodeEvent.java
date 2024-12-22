package org.bukkit.event.block;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
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
 *
 * @since 1.8.8
 */
public class BlockExplodeEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final BlockState blockState;
    private final List<Block> blocks;
    private float yield;
    private final ExplosionResult result;

    public BlockExplodeEvent(@NotNull final Block what, @NotNull final BlockState blockState, @NotNull final List<Block> blocks, final float yield, @NotNull final ExplosionResult result) {
        super(what);
        this.blockState = blockState;
        this.blocks = blocks;
        this.yield = yield;
        this.cancel = false;
        this.result = result;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Returns the result of the explosion if it is not cancelled.
     *
     * @return the result of the explosion
     * @since 1.21
     */
    @NotNull
    public ExplosionResult getExplosionResult() {
        return result;
    }

    /**
     * Returns the captured BlockState of the block that exploded.
     *
     * @return the block state
     * @since 1.19.3
     */
    @NotNull
    public BlockState getExplodedBlockState() {
        return blockState;
    }

    /**
     * Returns the list of blocks that would have been removed or were removed
     * from the explosion event.
     *
     * @return All blown-up blocks
     */
    @NotNull
    public List<Block> blockList() {
        return blocks;
    }

    /**
     * Returns the percentage of blocks to drop from this explosion
     *
     * @return The yield.
     */
    public float getYield() {
        return yield;
    }

    /**
     * Sets the percentage of blocks to drop from this explosion
     *
     * @param yield The new yield percentage
     */
    public void setYield(float yield) {
        this.yield = yield;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
