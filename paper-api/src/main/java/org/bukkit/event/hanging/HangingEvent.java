package org.bukkit.event.hanging;

import org.bukkit.entity.Hanging;
import org.bukkit.event.Event;

/**
 * Represents a hanging entity-related event.
 */
public interface HangingEvent extends Event {

    /**
     * Gets the hanging entity involved in this event.
     *
     * @return the hanging entity
     */
    Hanging getEntity();
}
