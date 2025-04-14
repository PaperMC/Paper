package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an Entity targets a {@link LivingEntity} and can only target
 * LivingEntity's.
 */
public class EntityTargetLivingEntityEvent extends EntityTargetEvent {

    @ApiStatus.Internal
    public EntityTargetLivingEntityEvent(@NotNull final Entity entity, @Nullable final LivingEntity target, @NotNull final TargetReason reason) {
        super(entity, target, reason);
    }

    @Override
    @Nullable
    public LivingEntity getTarget() {
        return (LivingEntity) super.getTarget();
    }

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
    public void setTarget(@Nullable Entity target) {
        if (target == null || target instanceof LivingEntity) {
            super.setTarget(target);
        }
    }
}
