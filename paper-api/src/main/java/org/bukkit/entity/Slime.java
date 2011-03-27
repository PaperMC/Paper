/**
 * 
 */
package org.bukkit.entity;

/**
 * Represents a Slime.
 * 
 * @author Cogito
 *
 */
public interface Slime extends LivingEntity {
    /**
     * @author Celtic Minstrel
     * @return The size of the slime
     */
    public int getSize();
    /**
     * @author Celtic Minstrel
     * @param sz The new size of the slime.
     */
    public void setSize(int sz);
}
