package org.bukkit.entity;

/**
 * A representation of an explosive entity
 */
public interface Explosive extends Entity {
    /**
     * Set the radius affected by this explosive's explosion
     *
     * @param yield The explosive yield
     */
    public void setYield(float yield);

    /**
     * Return the radius or yield of this explosive's explosion
     *
     * @return the radius of blocks affected
     */
    public float getYield();

    /**
     * Set whether or not this explosive's explosion causes fire
     *
     * @param isIncendiary Whether it should cause fire
     */
    public void setIsIncendiary(boolean isIncendiary);

    /**
     * Return whether or not this explosive creates a fire when exploding
     *
     * @return true if the explosive creates fire, false otherwise
     */
    public boolean isIncendiary();
}
