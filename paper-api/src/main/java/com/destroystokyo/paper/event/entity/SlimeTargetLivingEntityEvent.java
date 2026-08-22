package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.LivingEntity;

/**
 * Fired when a Slime decides to change direction to target a LivingEntity.
 * <p>
 * This event does not fire for the entity's actual movement. Only when it
 * is choosing to start moving.
 */
public interface SlimeTargetLivingEntityEvent extends SlimePathfindEvent {

    /**
     * Get the targeted entity
     *
     * @return Targeted entity
     */
    LivingEntity getTarget();
}
