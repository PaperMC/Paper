package org.bukkit.entity;

import org.bukkit.DyeColor;

/**
 * Represents a Wolf
 */
public interface Wolf extends Animals, Tameable, Sittable {

    /**
     * Checks if this wolf is angry
     *
     * @return Anger true if angry
     */
    public boolean isAngry();

    /**
     * Sets the anger of this wolf.
     * <p>
     * An angry wolf can not be fed or tamed, and will actively look for
     * targets to attack.
     *
     * @param angry true if angry
     */
    public void setAngry(boolean angry);

    /**
     * Get the collar color of this wolf
     *
     * @return the color of the collar
     */
    public DyeColor getCollarColor();

    /**
     * Set the collar color of this wolf
     *
     * @param color the color to apply
     */
    public void setCollarColor(DyeColor color);
}
