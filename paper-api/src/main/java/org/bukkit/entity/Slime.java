package org.bukkit.entity;

/**
 * Represents a Slime.
 */
public interface Slime extends LivingEntity {

    /**
     * @return The size of the slime
     */
    public int getSize();

    /**
     * @param sz The new size of the slime.
     */
    public void setSize(int sz);

    /**
     * Set the {@link LivingEntity} target for this slime. Set to null to clear
     * the target.
     *
     * @param target the entity to target
     */
    public void setTarget(LivingEntity target);

    /**
     * Get the {@link LivingEntity} this slime is currently targeting.
     *
     * @return the current target, or null if no target exists.
     */
    public LivingEntity getTarget();
}
