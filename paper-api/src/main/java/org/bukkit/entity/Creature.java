package org.bukkit.entity;

/**
 * Represents a Creature. Any LivingEntity that is not human is a Creature.
 * @author Cogito
 *
 */
public interface Creature extends LivingEntity{
    /**
     * Instructs this Creature to set the specified LivingEntity as its target.
     * Hostile creatures may attack their target, and friendly creatures may
     * follow their target.
     *
     * @param target New LivingEntity to target
     */
    public void setTarget(LivingEntity target);
}
