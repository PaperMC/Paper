package org.bukkit.event.painting;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.event.Cancellable;

/**
 * Triggered when a painting is removed
 *
 * @author Tanel Suurhans
 */

public class PaintingBreakEvent extends PaintingEvent implements Cancellable {

    private boolean cancelled;
    private RemoveCause cause;

    public PaintingBreakEvent(final Painting painting, RemoveCause cause) {
        super(Type.PAINTING_BREAK, painting);
        this.cause = cause;
    }

    public RemoveCause getCause(){
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
         * Removed by the world - block destroyed behind, water flowing over etc
         */
        WORLD

    }

}