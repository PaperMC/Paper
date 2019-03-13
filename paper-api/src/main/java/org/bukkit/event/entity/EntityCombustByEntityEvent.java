package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity causes another entity to combust.
 */
public class EntityCombustByEntityEvent extends EntityCombustEvent {
    private final Entity combuster;

    public EntityCombustByEntityEvent(@NotNull final Entity combuster, @NotNull final Entity combustee, final int duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    /**
     * Get the entity that caused the combustion event.
     *
     * @return the Entity that set the combustee alight.
     */
    @NotNull
    public Entity getCombuster() {
        return combuster;
    }
}
