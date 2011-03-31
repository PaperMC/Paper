
package org.bukkit.entity;

/**
 * Represents a Wolf
 */
public interface Wolf extends Animals {
    /**
     * Checks if this wolf is angry
     *
     * @return Anger true if angry
     */
    public boolean isAngry();

    /**
     * Sets the anger of this wolf
     *
     * @param angry true if angry
     */
    public void setAngry(boolean angry);

    /**
     * Checks if this wolf is sitting
     *
     * @return true if sitting
     */
    public boolean isSitting();

    /**
     * Sets if this wolf is sitting
     *
     * @param sitting true if sitting
     */
    public void setSitting(boolean sitting);
}
