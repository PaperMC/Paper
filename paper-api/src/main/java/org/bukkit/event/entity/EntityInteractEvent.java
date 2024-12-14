package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity interacts with an object
 *
 * @since 1.0.0 R1
 */
public class EntityInteractEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected Block block;
    private boolean cancelled;

    public EntityInteractEvent(@NotNull final Entity entity, @NotNull final Block block) {
        super(entity);
        this.block = block;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Returns the involved block
     *
     * @return the block clicked with this item.
     */
    @NotNull
    public Block getBlock() {
        return block;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
