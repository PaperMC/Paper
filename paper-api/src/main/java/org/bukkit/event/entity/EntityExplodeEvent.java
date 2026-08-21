package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity explodes interacting with blocks. The
 * event isn't called if the {@link org.bukkit.GameRules#MOB_GRIEFING}
 * is disabled as no block interaction will occur.
 */
public interface EntityExplodeEvent extends EntityEventNew, Cancellable {

    /**
     * Returns the result of the explosion if it is not cancelled.
     *
     * @return the result of the explosion
     */
    ExplosionResult getExplosionResult();

    /**
     * Returns the list of blocks that would have been removed or were removed
     * from the explosion event.
     *
     * @return All blown-up blocks
     */
    List<Block> blockList();

    /**
     * Returns the location where the explosion happened.
     * <p>
     * It is not possible to get this value from the Entity as the Entity no
     * longer exists in the world.
     *
     * @return The location of the explosion
     */
    Location getLocation();

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
