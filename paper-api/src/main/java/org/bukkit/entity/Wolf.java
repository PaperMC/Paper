package org.bukkit.entity;

/**
 * Represents a Wolf
 */
public interface Wolf extends Animals, Tameable {

    /**
     * Checks if this wolf is angry
     *
     * @return Anger true if angry
     */
    public boolean isAngry();

    /**
     * Sets the anger of this wolf
     * An angry wolf can not be fed or tamed, and will actively look for targets to attack.
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
     * Will remove any path that the wolf was following beforehand.
     *
     * @param sitting true if sitting
     */
    public void setSitting(boolean sitting);

}
