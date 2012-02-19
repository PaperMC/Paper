package org.bukkit.event.painting;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;

/**
 * Triggered when a painting is removed by an entity
 */
public class PaintingBreakByEntityEvent extends PaintingBreakEvent {
    private final Entity remover;

    public PaintingBreakByEntityEvent(final Painting painting, final Entity remover) {
        super(painting, RemoveCause.ENTITY);
        this.remover = remover;
    }

    /**
     * Gets the entity that removed the painting
     *
     * @return the entity that removed the painting.
     */
    public Entity getRemover() {
        return remover;
    }
}
