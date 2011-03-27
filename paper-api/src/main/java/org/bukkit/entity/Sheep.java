/**
 * 
 */
package org.bukkit.entity;
import org.bukkit.material.Colorable;

/**
 * Represents a Sheep.
 * 
 * @author Cogito
 *
 */
public interface Sheep extends Animals, Colorable {
    /**
     * @author Celtic Minstrel
     * @return Whether the sheep is sheared.
     */
    public boolean isSheared();
    /**
     * @author Celtic Minstrel
     * @param flag Whether to shear the sheep
     */
    public void setSheared(boolean flag);
}
