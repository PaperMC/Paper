package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityTargetLivingEntityEvent extends CraftEntityTargetEvent implements EntityTargetLivingEntityEvent {

    public CraftEntityTargetLivingEntityEvent(final Entity entity, final @Nullable LivingEntity target, final TargetReason reason) {
        super(entity, target, reason);
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return (LivingEntity) this.target;
    }

    @Override
    public void setTarget(final @Nullable Entity target) {
        if (target == null || target instanceof LivingEntity) {
            super.setTarget(target);
        }
    }
}
