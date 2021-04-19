package org.bukkit.entity;

import org.bukkit.material.Colorable;

public interface Shulker extends Golem, Colorable {

    /**
     * Gets the peek state of the shulker between 0.0 and 1.0.
     *
     * @return the peek state of the shulker between 0.0 and 1.0
     */
    public float getPeek();

    /**
     * Sets the peek state of the shulker, should be in between 0.0 and 1.0.
     *
     * @param value peek state of the shulker, should be in between 0.0 and 1.0
     * @throws IllegalArgumentException thrown if the value exceeds the valid
     * range in between of 0.0 and 1.0
     */
    public void setPeek(float value);
}
