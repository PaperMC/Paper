package org.bukkit.entity;

import org.bukkit.block.BlockFace;
import org.bukkit.material.Colorable;
import org.jetbrains.annotations.NotNull;

public interface Shulker extends Golem, Colorable, Enemy {

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

    /**
     * Gets the face to which the shulker is attached.
     *
     * @return the face to which the shulker is attached
     */
    @NotNull
    public BlockFace getAttachedFace();

    /**
     * Sets the face to which the shulker is attached.
     *
     * @param face the face to attach the shulker to
     */
    public void setAttachedFace(@NotNull BlockFace face);
}
