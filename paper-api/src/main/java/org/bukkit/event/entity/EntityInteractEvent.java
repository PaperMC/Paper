package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Called when an entity interacts with an object
 */
@SuppressWarnings("serial")
public class EntityInteractEvent extends EntityEvent implements Cancellable {
    protected Block block;

    private boolean cancelled;

    public EntityInteractEvent(Entity entity, Block block) {
        super(Type.ENTITY_INTERACT, entity);
        this.block = block;
    }

    public boolean isCancelled() {
        return cancelled;
    }

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
