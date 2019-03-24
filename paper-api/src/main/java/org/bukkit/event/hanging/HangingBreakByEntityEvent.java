package org.bukkit.event.hanging;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Triggered when a hanging entity is removed by an entity
 */
public class HangingBreakByEntityEvent extends HangingBreakEvent {
    private final Entity remover;

    public HangingBreakByEntityEvent(@NotNull final Hanging hanging, @NotNull final Entity remover) { // Paper
        this(hanging, remover, HangingBreakEvent.RemoveCause.ENTITY);
    }

    public HangingBreakByEntityEvent(@NotNull final Hanging hanging, @NotNull final Entity remover, @NotNull final HangingBreakEvent.RemoveCause cause) { // Paper
        super(hanging, cause);
        this.remover = remover;
    }

    /**
     * Gets the entity that removed the hanging entity.
     *
     * @return the entity that removed the hanging entity
     */
    @NotNull // Paper
    public Entity getRemover() {
        return remover;
    }
}
