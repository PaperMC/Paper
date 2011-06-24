package org.bukkit.event.painting;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.event.Cancellable;

/**
 * Triggered when a painting is removed
 */
public class PaintingBreakEvent extends PaintingEvent implements Cancellable {

    private boolean cancelled;
    private RemoveCause cause;

    public PaintingBreakEvent(final Painting painting, RemoveCause cause) {
        super(Type.PAINTING_BREAK, painting);
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

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
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
         * Removed by the world - block the painting is on is destroyed, water flowing over etc
         */
        WORLD

    }
}
