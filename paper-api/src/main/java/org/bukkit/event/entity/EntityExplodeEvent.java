package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

/**
 * Called when an entity explodes
 */
public class EntityExplodeEvent extends EntityEvent implements Cancellable {
    private boolean cancel;
    private Location location;
    private List<Block> blocks;
    private float yield = 0.3F;

    public EntityExplodeEvent(Entity what, Location location, List<Block> blocks) {
        super(Type.ENTITY_EXPLODE, what);
        this.location = location;
        this.cancel = false;
        this.blocks = blocks;
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
     * @return All blown-up blocks
     */
    public List<Block> blockList() {
        return blocks;
    }

    /**
     * Returns the location where the explosion happened.
     * It is not possible to get this value from the Entity as
     * the Entity no longer exists in the world.
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
     * @param yield The new yield percentage
     */
    public void setYield(float yield) {
        this.yield = yield;
    }
}
