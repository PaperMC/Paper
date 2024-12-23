package org.bukkit.entity;

/**
 * Represents a Slime.
 *
 * @since 1.0.0
 */
public interface Slime extends Mob, Enemy {

    /**
     * @return The size of the slime
     */
    public int getSize();

    /**
     * Setting the size of the slime (regardless of previous size)
     * will set the following attributes:
     * <ul>
     *     <li>{@link org.bukkit.attribute.Attribute#MAX_HEALTH}</li>
     *     <li>{@link org.bukkit.attribute.Attribute#MOVEMENT_SPEED}</li>
     *     <li>{@link org.bukkit.attribute.Attribute#ATTACK_DAMAGE}</li>
     * </ul>
     * to their per-size defaults and heal the
     * slime to its max health (assuming it's alive).
     *
     * @param sz The new size of the slime.
     */
    public void setSize(int sz);

    // Paper start
    /**
     * Get whether this slime can randomly wander/jump around on its own
     *
     * @return true if can wander
     * @since 1.13
     */
    public boolean canWander();

    /**
     * Set whether this slime can randomly wander/jump around on its own
     *
     * @param canWander true if can wander
     * @since 1.13
     */
    public void setWander(boolean canWander);
    // Paper end
}
