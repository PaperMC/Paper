package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Called when an entity interacts with an object
 */
public class EntityInteractEvent extends EntityEvent implements Cancellable {
    protected Block block;

    private boolean cancelled;

    public EntityInteractEvent(Entity entity, Block block) {
        super(Type.ENTITY_INTERACT, entity);
        this.block = block;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you
     * want to prevent buckets from placing water and so forth
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Returns the involved block
     *
     * @return the block clicked with this item.
     */
    public Block getBlock() {
        return block;
    }
}
