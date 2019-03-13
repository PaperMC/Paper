package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public interface Directional {

    /**
     * Sets the direction that this block is facing in
     *
     * @param face The facing direction
     */
    public void setFacingDirection(@NotNull BlockFace face);

    /**
     * Gets the direction this block is facing
     *
     * @return the direction this block is facing
     */
    @NotNull
    public BlockFace getFacing();
}
