package org.bukkit.entity;

import org.bukkit.loot.Lootable;

/**
 * Represents a Mob. Mobs are living entities with simple AI.
 */
public interface Mob extends LivingEntity, Lootable {

    /**
     * Instructs this Mob to set the specified LivingEntity as its target.
     * <p>
     * Hostile creatures may attack their target, and friendly creatures may
     * follow their target.
     *
     * @param target New LivingEntity to target, or null to clear the target
     */
    public void setTarget(LivingEntity target);

    /**
     * Gets the current target of this Mob
     *
     * @return Current target of this creature, or null if none exists
     */
    public LivingEntity getTarget();
}
