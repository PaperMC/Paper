package org.bukkit.entity;

import com.destroystokyo.paper.entity.RangedEntity;

/**
 * Represents a snowman entity
 *
 * @since 1.0.0 R1
 */
public interface Snowman extends Golem, RangedEntity, io.papermc.paper.entity.Shearable { // Paper

    /**
     * Gets whether this snowman is in "derp mode", meaning it is not wearing a
     * pumpkin.
     *
     * @return True if the snowman is bald, false if it is wearing a pumpkin
     * @since 1.9.4
     */
    boolean isDerp();

    /**
     * Sets whether this snowman is in "derp mode", meaning it is not wearing a
     * pumpkin. NOTE: This value is not persisted to disk and will therefore
     * reset when the chunk is reloaded.
     *
     * @param derpMode True to remove the pumpkin, false to add a pumpkin
     * @since 1.9.4
     */
    void setDerp(boolean derpMode);
}
