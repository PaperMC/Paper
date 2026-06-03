package org.bukkit.entity;

/**
 * Represents an abstract cube mob.
 */
public interface AbstractCubeMob extends Mob {

    /**
     * @return the size of the slime
     */
    int getSize();

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
     * @param size the new size of the slime.
     */
    void setSize(int size);

    /**
     * Get whether this slime can randomly wander/jump around on its own
     *
     * @return {@code true} if can wander
     */
    boolean canWander();

    /**
     * Set whether this slime can randomly wander/jump around on its own
     *
     * @param canWander {@code true} if can wander
     */
    void setWander(boolean canWander);
}
