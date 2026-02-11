package com.destroystokyo.paper.event.entity;

import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired when an Entity is knocked back by the hit of another Entity. The acceleration
 * vector can be modified. If this event is cancelled, the entity is not knocked back.
 */
public interface EntityKnockbackByEntityEvent extends EntityPushedByEntityAttackEvent {

    /**
     * @return the entity which was knocked back
     */
    @Override
    LivingEntity getEntity();

    /**
     * @return the original knockback strength.
     * @apiNote this value doesn't necessarily relate to {@link #getKnockback()}.
     */
    @ApiStatus.Obsolete(since = "1.20.6")
    float getKnockbackStrength();

    /**
     * Gets the causing entity. Same as {@link #getPushedBy()}.
     *
     * @return the Entity which hit
     */
    Entity getHitBy();
}
