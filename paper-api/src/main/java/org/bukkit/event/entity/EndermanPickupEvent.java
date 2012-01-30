package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * @deprecated Use EntityChangeBlockEvent instead
 */
@SuppressWarnings("serial")
@Deprecated
public class EndermanPickupEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel;
    private final Block block;

    public EndermanPickupEvent(final Entity what, final Block block) {
        super(what);
        this.block = block;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Get the block that the enderman is going to pick up.
     *
     * @return block the enderman is about to pick up
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
