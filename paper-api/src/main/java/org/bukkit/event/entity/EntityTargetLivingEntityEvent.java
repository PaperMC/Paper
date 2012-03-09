package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * Called when an Entity targets a {@link LivingEntity} and can only target LivingEntity's.
 */
public class EntityTargetLivingEntityEvent extends EntityTargetEvent{
    public EntityTargetLivingEntityEvent(final Entity entity, final LivingEntity target, final TargetReason reason) {
        super(entity, target, reason);
    }

    public LivingEntity getTarget() {
        return (LivingEntity) super.getTarget();
    }

    /**
     * Set the Entity that you want the mob to target.
     * It is possible to be null, null will cause the entity to be
     * target-less.
     * <p />
     * Must be a LivingEntity, or null
     *
     * @param target The entity to target
     */
    public void setTarget(Entity target) {
        if (target == null || target instanceof LivingEntity) {
            super.setTarget(target);
        }
    }
}
