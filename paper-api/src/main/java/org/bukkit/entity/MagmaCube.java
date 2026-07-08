package org.bukkit.entity;

/**
 * Represents a MagmaCube.
 */
public interface MagmaCube extends AbstractCubeMob, Enemy {

    /**
     * Setting the size of the magma cube (regardless of previous size)
     * will set the following attributes:
     * <ul>
     *     <li>{@link org.bukkit.attribute.Attribute#MAX_HEALTH}</li>
     *     <li>{@link org.bukkit.attribute.Attribute#MOVEMENT_SPEED}</li>
     *     <li>{@link org.bukkit.attribute.Attribute#ATTACK_DAMAGE}</li>
     *     <li>{@link org.bukkit.attribute.Attribute#ARMOR}</li>
     * </ul>
     * to their per-size defaults and heal the
     * magma cube to its max health (assuming it's alive).
     *
     * @param size the new size of the magma cube.
     */
    void setSize(int size);
}
