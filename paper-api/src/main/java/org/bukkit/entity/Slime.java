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
}
