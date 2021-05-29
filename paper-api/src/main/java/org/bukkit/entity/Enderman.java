package org.bukkit.entity;

import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an Enderman.
 */
public interface Enderman extends Monster {

    // Paper start
    /**
     * Try to teleport the enderman to a random nearby location.
     *
     * May conditionally fail if the random location was not valid
     * @return If the enderman teleported successfully or not
     */

    public boolean teleportRandomly();
    // Paper end

    /**
     * Gets the id and data of the block that the Enderman is carrying.
     *
     * @return MaterialData containing the id and data of the block
     * @deprecated use {@link #getCarriedBlock()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.13")
    public MaterialData getCarriedMaterial();

    /**
     * Sets the id and data of the block that the Enderman is carrying.
     *
     * @param material data to set the carried block to
     * @deprecated use {@link #setCarriedBlock(BlockData)}
     */
    @Deprecated(forRemoval = true, since = "1.13")
    public void setCarriedMaterial(@NotNull MaterialData material);

    /**
     * Gets the data of the block that the Enderman is carrying.
     *
     * @return BlockData containing the carried block, or null if none
     */
    @Nullable
    public BlockData getCarriedBlock();

    /**
     * Sets the data of the block that the Enderman is carrying.
     *
     * @param blockData data to set the carried block to, or null to remove
     */
    public void setCarriedBlock(@Nullable BlockData blockData);

    /**
     * Randomly teleports the Enderman in a 64x64x64 block cuboid region.
     * <p>
     * If the randomly selected point is in the ground, the point is moved 1 block
     * down until air is found or until it goes under
     * {@link org.bukkit.World#getMinHeight()}.
     * <p>
     * This method will return false if this Enderman is not alive, or if the
     * teleport location was obstructed, or if the teleport location is in water.
     *
     * @return true if the teleport succeeded.
     */
    public boolean teleport();

    /**
     * Randomly teleports the Enderman towards the given <code>entity</code>.
     * <p>
     * The point is selected by drawing a vector between this enderman and the
     * given <code>entity</code>. That vector's length is set to 16 blocks.
     * That point is then moved within a 8x8x8 cuboid region. If the randomly
     * selected point is in the ground, the point is moved 1 block down until
     * air is found or until it goes under
     * {@link org.bukkit.World#getMinHeight()}.
     * <p>
     * This method will return false if this Enderman is not alive, or if the
     * teleport location was obstructed, or if the teleport location is in water.
     *
     * @param entity The entity to teleport towards.
     * @return true if the teleport succeeded.
     */
    public boolean teleportTowards(@NotNull Entity entity);

    // Paper start
    /**
     * Returns whether the enderman is screaming/angry.
     *
     * @return whether the enderman is screaming
     */
    boolean isScreaming();

    /**
     * Sets whether the enderman is screaming/angry.
     *
     * @param screaming whether the enderman is screaming
     */
    void setScreaming(boolean screaming);

    /**
     * Returns whether the enderman has been stared at.
     * If set to true, players will hear an ambient sound.
     *
     * @return whether the enderman has been stared at
     */
    boolean hasBeenStaredAt();

    /**
     * Sets whether the enderman has been stared at.
     * If set to true, players will hear an ambient sound.
     *
     * @param hasBeenStaredAt whether the enderman has been stared at
     */
    void setHasBeenStaredAt(boolean hasBeenStaredAt);
    // Paper end
}
