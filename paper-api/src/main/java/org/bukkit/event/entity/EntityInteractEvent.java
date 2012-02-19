package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity interacts with an object
 */
public class EntityInteractEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected Block block;
    private boolean cancelled;

    public EntityInteractEvent(final Entity entity, final Block block) {
        super(entity);
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
