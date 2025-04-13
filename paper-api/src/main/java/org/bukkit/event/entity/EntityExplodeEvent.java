package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity explodes interacting with blocks. The
 * event isn't called if the {@link org.bukkit.GameRule#MOB_GRIEFING}
 * is disabled as no block interaction will occur.
 */
public class EntityExplodeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location location;
    private final List<Block> blocks;
    private float yield;
    private final ExplosionResult result;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityExplodeEvent(@NotNull final Entity entity, @NotNull final Location location, @NotNull final List<Block> blocks, final float yield, @NotNull final ExplosionResult result) {
        super(entity);
        this.location = location;
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
     * Returns the location where the explosion happened.
     * <p>
     * It is not possible to get this value from the Entity as the Entity no
     * longer exists in the world.
     *
     * @return The location of the explosion
     */
    @NotNull
    public Location getLocation() {
        return this.location.clone();
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
