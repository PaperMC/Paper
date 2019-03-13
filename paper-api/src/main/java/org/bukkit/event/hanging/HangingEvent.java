package org.bukkit.event.hanging;

import org.bukkit.entity.Hanging;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a hanging entity-related event.
 */
public abstract class HangingEvent extends Event {
    protected Hanging hanging;

    protected HangingEvent(@NotNull final Hanging painting) {
        this.hanging = painting;
    }

    /**
     * Gets the hanging entity involved in this event.
     *
     * @return the hanging entity
     */
    @NotNull
    public Hanging getEntity() {
        return hanging;
    }
}
