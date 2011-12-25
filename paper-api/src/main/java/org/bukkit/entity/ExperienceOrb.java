package org.bukkit.entity;

/**
 * Represents an Experience Orb.
 */
public interface ExperienceOrb extends Entity {
    /**
     * Gets how much experience is contained within this orb
     *
     * @return Amount of experience
     */
    public int getExperience();

    /**
     * Sets how much experience is contained within this orb
     *
     * @param value Amount of experience
     */
    public void setExperience(int value);
}
