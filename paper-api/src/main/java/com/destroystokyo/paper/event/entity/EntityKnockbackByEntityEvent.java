package com.destroystokyo.paper.event.entity;

import io.papermc.paper.event.entity.EntityKnockbackEvent;
import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when an Entity is knocked back by the hit of another Entity. The acceleration
 * vector can be modified. If this event is cancelled, the entity is not knocked back.
 */
@NullMarked
public class EntityKnockbackByEntityEvent extends EntityPushedByEntityAttackEvent {

    private final float knockbackStrength;

    @ApiStatus.Internal
    public EntityKnockbackByEntityEvent(final LivingEntity entity, final Entity hitBy, final EntityKnockbackEvent.Cause cause, final float knockbackStrength, final Vector knockback) {
        super(entity, cause, hitBy, knockback);
        this.knockbackStrength = knockbackStrength;
    }

    /**
     * @return the entity which was knocked back
     */
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    /**
     * @return the original knockback strength.
     * @apiNote this value doesn't necessarily relate to {@link #getKnockback()}.
     */
    @ApiStatus.Obsolete(since = "1.20.6")
    public float getKnockbackStrength() {
        return this.knockbackStrength;
    }

    /**
     * Gets the causing entity. Same as {@link #getPushedBy()}.
     *
     * @return the Entity which hit
     */
    public Entity getHitBy() {
        return super.getPushedBy();
    }

}
