package org.bukkit.event.painting;

import org.bukkit.entity.Painting;
import org.bukkit.event.Event;

/**
 * Represents a painting-related event.
 */
@SuppressWarnings("serial")
public abstract class PaintingEvent extends Event {

    protected Painting painting;

    protected PaintingEvent(final Type type, final Painting painting) {
        super(type);
        this.painting = painting;
    }

    /**
     * Gets the painting involved in this event.
     *
     * @return the painting
     */
    public Painting getPainting() {
        return painting;
    }
}
