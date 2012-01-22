package org.bukkit.entity;

import org.bukkit.material.Colorable;

/**
 * Represents a Sheep.
 */
public interface Sheep extends Animals, Colorable {

    /**
     * @return Whether the sheep is sheared.
     */
    public boolean isSheared();

    /**
     * @param flag Whether to shear the sheep
     */
    public void setSheared(boolean flag);
}
