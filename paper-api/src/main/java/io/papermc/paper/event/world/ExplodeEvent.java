package io.papermc.paper.event.world;

import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.List;

/**
 * Called when an explodes happen. this includes explosion who not affect blocks and are just visual.
 */
@ApiStatus.Experimental
@NullMarked
public class ExplodeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location location;
    private final List<Block> blocks;
    private final ExplosionResult result;
    private @Nullable BlockState blockState;
    private @Nullable Entity entity;
    private float yield;
    private boolean cancelled;

    @ApiStatus.Internal
    public ExplodeEvent(final Location location, final List<Block> blocks, final float yield, final ExplosionResult result) {
        this.location = location;
        this.blocks = blocks;
        this.yield = yield;
        this.result = result;
        this.cancelled = false;
    }

    @ApiStatus.Internal
    public ExplodeEvent(final Entity entityExploded, final Location location, final List<Block> blocks, final float yield, final ExplosionResult result) {
        this(location, blocks, yield, result);
        this.entity = entityExploded;
    }

    @ApiStatus.Internal
    public ExplodeEvent(final BlockState blockStateExploded, final Location location, final List<Block> blocks, final float yield, final ExplosionResult result) {
        this(location, blocks, yield, result);
        this.blockState = blockStateExploded;
    }

    /**
     * Returns the result of the explosion if it is not cancelled.
     *
     * @return the result of the explosion
     */
    public ExplosionResult getExplosionResult() {
        return result;
    }

    /**
     * Returns the location where the explosion happened.
     * <p>
     * It is not possible to get this value from the Entity as the Entity no
     * longer exists in the world.
     *
     * @return The location of the explosion
     */
    public Location getLocation() {
        return this.location.clone();
    }

    /**
     * Returns the entity that exploded, if exists.
     *
     * @return the entity, if is the source of explosion. {@code null} if it has not.
     */
    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Returns the captured BlockState of the block that exploded.
     *
     * @return the block state, if is the source of explosion. {@code null} if it has not.
     */
    @Nullable
    public BlockState getBlockState() {
        return this.blockState;
    }

    /**
     * Returns the list of blocks that would have been caught in the explosion.
     * <br>
     * <b>Note:</b> the behaviours with these blocks depends on {@link #getExplosionResult()}
     *
     * @return All blocks caught in the explosion
     */
    public List<Block> blockList() {
        return this.blocks;
    }

    /**
     * Returns the percentage of blocks to drop from this explosion.
     * <br>
     * <b>Note:</b> this behaviour depends on {@link #getExplosionResult()}
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
     *
     * @see #getYield()
     */
    public void setYield(float yield) {
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
