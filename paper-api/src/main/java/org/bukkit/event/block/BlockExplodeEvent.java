package org.bukkit.event.block;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block explodes.
 * <p>
 * Note that due to the nature of explosions, {@link #getBlock()} will always be
 * an air block. {@link #getExplodedBlockState()} should be used to get
 * information about the block state that exploded.
 * <p>
 * The event isn't called if the {@link org.bukkit.GameRules#MOB_GRIEFING}
 * is disabled as no block interaction will occur.
 */
public interface BlockExplodeEvent extends BlockEvent, Cancellable {

    /**
     * Returns the result of the explosion if it is not cancelled.
     *
     * @return the result of the explosion
     */
    ExplosionResult getExplosionResult();

    /**
     * Returns the captured BlockState of the block that exploded.
     *
     * @return the block state
     */
    BlockState getExplodedBlockState();

    /**
     * Returns the list of blocks that would have been removed or were removed
     * from the explosion event.
     *
     * @return All blown-up blocks
     */
    List<Block> blockList();

    /**
     * Returns the percentage of blocks to drop from this explosion
     *
     * @return The yield.
     */
    float getYield();

    /**
     * Sets the percentage of blocks to drop from this explosion
     *
     * @param yield The new yield percentage
     */
    void setYield(float yield);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
