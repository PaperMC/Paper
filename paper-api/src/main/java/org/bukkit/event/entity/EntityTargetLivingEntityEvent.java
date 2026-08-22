package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

/**
 * Called when an Entity targets a {@link LivingEntity} and can only target
 * LivingEntity's.
 */
public interface EntityTargetLivingEntityEvent extends EntityTargetEvent {

    @Override
    LivingEntity getTarget();

    /**
     * Set the Entity that you want the mob to target.
     * <p>
     * It is possible to be {@code null}, {@code null} will cause the entity to be
     * target-less.
     * <p>
     * Must be a LivingEntity, or {@code null}.
     *
     * @param target The entity to target
     */
    @Override
    void setTarget(@Nullable Entity target);
}
