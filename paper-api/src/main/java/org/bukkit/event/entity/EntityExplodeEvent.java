package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Called when an entity explodes
 */
public class EntityExplodeEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final Location location;
    private final List<Block> blocks;
    private float yield;

    public EntityExplodeEvent(final Entity what, final Location location, final List<Block> blocks, final float yield) {
        super(what);
        this.location = location;
        this.blocks = blocks;
        this.yield = yield;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Returns the list of blocks that would have been removed or were
     * removed from the explosion event.
     *
     * @return All blown-up blocks
     */
    public List<Block> blockList() {
        return blocks;
    }

    /**
     * Returns the location where the explosion happened.
     * It is not possible to get this value from the Entity as
     * the Entity no longer exists in the world.
     *
     * @return The location of the explosion
     */
    public Location getLocation() {
        return location;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
