package io.papermc.paper.event.world;

import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.List;

/**
 * Called when an explodes happen including visual-only explosion.
 */
@NullMarked
public class ExplodeEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final DamageSource damageSource;
    private final Location location;
    private final List<Block> blocks;
    private final ExplosionResult result;
    private final boolean spreadFire;
    private @Nullable BlockState blockState;
    private @Nullable Entity entity;
    private float yield;
    private boolean cancelled;

    @ApiStatus.Internal
    private ExplodeEvent(final DamageSource damageSource, final Location location, final List<Block> blocks, final float yield, final boolean spreadFire, final ExplosionResult result) {
        super(location.getWorld());
        this.damageSource = damageSource;
        this.location = location;
        this.blocks = blocks;
        this.yield = yield;
        this.spreadFire = spreadFire;
        this.result = result;
    }

    @ApiStatus.Internal
    public ExplodeEvent(final DamageSource damageSource, final Entity entity, final List<Block> blocks, final float yield, final boolean spreadFire, final ExplosionResult result) {
        this(damageSource, entity.getLocation(), blocks, yield, spreadFire, result);
        this.entity = entity;
    }

    @ApiStatus.Internal
    public ExplodeEvent(final DamageSource damageSource, final BlockState blockState, final Location location, final List<Block> blocks, final float yield, final boolean spreadFire, final ExplosionResult result) {
        this(damageSource, location, blocks, yield, spreadFire, result);
        this.blockState = blockState;
    }

    /**
     * Gets the source of damage.
     * <br>
     * <b>Note:</b> by default it's the explosion but datapacks using custom explode effect can bind other types of damages.
     *
     * @return a DamageSource detailing the source of the damage from the explosion
     */
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    /**
     * Returns the location where the explosion happened.
     *
     * @return the location of the explosion
     */
    public Location getLocation() {
        return this.location.clone();
    }

    /**
     * Returns the entity that exploded, if exists.
     *
     * @return the entity, if is the source of explosion. {@code null} if it has not
     */
    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Returns the captured BlockState of the block that exploded.
     *
     * @return the block state, if is the source of explosion. {@code null} if it has not
     */
    @Nullable
    public BlockState getBlockState() {
        return this.blockState;
    }

    /**
     * Returns a mutable list of blocks that would have been caught in the explosion.
     * <br>
     * <b>Note:</b> the behaviours with these blocks depends on {@link #getExplosionResult()}.
     *
     * @return all blocks caught in the explosion
     */
    @ApiStatus.Experimental
    public List<Block> getAffectedBlocks() {
        return this.blocks;
    }

    /**
     * Returns the percentage of blocks to drop from this explosion.
     * <br>
     * <b>Note:</b> this behaviour depends on {@link #getExplosionResult()}.
     *
     * @return the yield
     */
    public float getYield() {
        return this.yield;
    }

    /**
     * Sets the percentage of blocks to drop from this explosion.
     *
     * @param yield The new yield percentage
     *
     * @see #getYield()
     */
    public void setYield(float yield) {
        this.yield = yield;
    }

    /**
     * Checks if this explosion should spread fire
     * on the ground.
     *
     * @return if this explosion should spread fire
     */
    public boolean shouldSpreadFire() {
        return this.spreadFire;
    }

    /**
     * Returns the result of the explosion if it is not cancelled.
     *
     * @return the result of the explosion
     */
    public ExplosionResult getExplosionResult() {
        return this.result;
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
