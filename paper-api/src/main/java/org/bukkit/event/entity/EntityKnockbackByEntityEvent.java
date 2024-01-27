package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity receives knockback from another entity.
 */
public class EntityKnockbackByEntityEvent extends EntityKnockbackEvent {

    private final Entity source;

    public EntityKnockbackByEntityEvent(@NotNull final LivingEntity entity, @NotNull final Entity source, @NotNull final KnockbackCause cause, final double force, @NotNull final Vector rawKnockback, @NotNull final Vector knockback) {
        super(entity, cause, force, rawKnockback, knockback);

        this.source = source;
    }

    /**
     * Get the entity that has caused knockback to the defender.
     *
     * @return entity that caused knockback
     */
    @NotNull
    public Entity getSourceEntity() {
        return source;
    }
}
