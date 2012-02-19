package org.bukkit.event.painting;

import org.bukkit.entity.Painting;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Triggered when a painting is removed
 */
public class PaintingBreakEvent extends PaintingEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final RemoveCause cause;

    public PaintingBreakEvent(final Painting painting, final RemoveCause cause) {
        super(painting);
        this.cause = cause;
    }

    /**
     * Gets the cause for the painting's removal
     *
     * @return the RemoveCause for the painting's removal
     */
    public RemoveCause getCause() {
        return cause;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * An enum to specify the cause of the removal
     */
    public enum RemoveCause {
        /**
         * Removed by an entity
         */
        ENTITY,
        /**
         * Removed by fire
         */
        FIRE,
        /**
         * Removed by placing a block on it
         */
        OBSTRUCTION,
        /**
         * Removed by water flowing over it
         */
        WATER,
        /**
         * Removed by destroying the block behind it, etc
         */
        PHYSICS,
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
