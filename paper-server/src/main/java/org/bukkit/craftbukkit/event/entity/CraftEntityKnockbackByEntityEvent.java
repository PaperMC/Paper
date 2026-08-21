package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.util.Vector;

@Deprecated(forRemoval = true)
public class CraftEntityKnockbackByEntityEvent extends CraftEntityKnockbackEvent implements EntityKnockbackByEntityEvent {

    private final Entity source;

    public CraftEntityKnockbackByEntityEvent(final LivingEntity entity, final Entity source, final KnockbackCause cause, final double force, final Vector rawKnockback, final Vector knockback) {
        super(entity, cause, force, rawKnockback, knockback);
        this.source = source;
    }

    @Override
    public Entity getSourceEntity() {
        return this.source;
    }
}
