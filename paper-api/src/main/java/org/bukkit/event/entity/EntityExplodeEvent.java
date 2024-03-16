package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity explodes interacting with blocks. The
 * event isn't called if the {@link org.bukkit.GameRule#MOB_GRIEFING}
 * is disabled as no block interaction will occur.
 */
public class EntityExplodeEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final Location location;
    private final List<Block> blocks;
    private float yield;
    private final ExplosionResult result;

    public EntityExplodeEvent(@NotNull final Entity what, @NotNull final Location location, @NotNull final List<Block> blocks, final float yield, @NotNull final ExplosionResult result) {
        super(what);
        this.location = location;
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
     */
    @NotNull
    public ExplosionResult getExplosionResult() {
        return result;
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
     * Returns the location where the explosion happened.
     * <p>
     * It is not possible to get this value from the Entity as the Entity no
     * longer exists in the world.
     *
     * @return The location of the explosion
     */
    @NotNull
    public Location getLocation() {
        return location.clone(); // Paper - clone to avoid changes
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
