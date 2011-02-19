
package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

/**
 *
 * @author SpeaKeasY
 */
public class EntityExplodeEvent extends EntityEvent implements Cancellable {
    private boolean cancel;
    private Location location;
    private List blocks;
    private float yield = 0.3F;

    public EntityExplodeEvent (Type type, Entity what, Location location, List<Block> blocks) {
        super(type.ENTITY_EXPLODE, what);
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
     */
    public List<Block> blockList() {
        return blocks;
    }

    /**
     * Returns the location where the explosion happened.
     * It is not possible to get this value from the Entity as
     * the Entity no longer exists in the world.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the percentage of blocks to drop from this explosion
     * @return 
     */
    public float getYield() {
        return yield;
    }
    
    /**
     * Sets the percentage of blocks to drop from this explosion
     */
    public void setYield(float yield) {
        this.yield = yield;
    }
}
