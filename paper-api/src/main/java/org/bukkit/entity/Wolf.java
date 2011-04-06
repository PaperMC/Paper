
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

    /**
     * Check if this wolf is tame
     * 
     * @return true if tame
     */
    public boolean isTame();

    /**
     * Sets if wolf is tame
     * 
     * @param tame true if tame
     */
    public void setTame(boolean tame);

    /**
     * Gets the name of the current owning player
     * 
     * @return owners name, "" or null if unowned
     */
    public String getOwner();

    /**
     * Set the wolf to be owned by given player (also is tamed)
     * 
     * @param player name of owner, or null/"" if setting to unowned
     */
    public void setOwner(String player);

}
