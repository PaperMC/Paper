package org.bukkit.event.hanging;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Triggered when a hanging entity is removed by an entity
 */
public class HangingBreakByEntityEvent extends HangingBreakEvent {

    private final Entity remover;

    @ApiStatus.Internal
    public HangingBreakByEntityEvent(@NotNull final Hanging hanging, @NotNull final Entity remover) {
        this(hanging, remover, HangingBreakEvent.RemoveCause.ENTITY);
    }

    @ApiStatus.Internal
    public HangingBreakByEntityEvent(@NotNull final Hanging hanging, @NotNull final Entity remover, @NotNull final HangingBreakEvent.RemoveCause cause) {
        super(hanging, cause);
        this.remover = remover;
    }

    /**
     * Gets the entity that removed the hanging entity.
     *
     * @return the entity that removed the hanging entity
     */
    @NotNull
    public Entity getRemover() {
        return this.remover;
    }
}
