package org.bukkit.event.painting;

import org.bukkit.entity.Painting;
import org.bukkit.event.Event;

/**
 * Represents a painting-related event.
 *
 * @author Tanel Suurhans
 */
public class PaintingEvent extends Event {

    protected Painting painting;

    protected PaintingEvent(final Type type, final Painting painting) {
        super(type);
        this.painting = painting;
    }

    /**
     * Get the painting.
     *
     * @return the painting
     */
    public Painting getPainting() {
        return painting;
    }
}
