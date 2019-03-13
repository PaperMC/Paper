package org.bukkit;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * A delegate for handling block changes. This serves as a direct interface
 * between generation algorithms in the server implementation and utilizing
 * code.
 */
public interface BlockChangeDelegate {

    /**
     * Set a block data at the specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param blockData Block data
     * @return true if the block was set successfully
     */
    public boolean setBlockData(int x, int y, int z, @NotNull BlockData blockData);

    /**
     * Get the block data at the location.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return The block data
     */
    @NotNull
    public BlockData getBlockData(int x, int y, int z);

    /**
     * Gets the height of the world.
     *
     * @return Height of the world
     */
    public int getHeight();

    /**
     * Checks if the specified block is empty (air) or not.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return True if the block is considered empty.
     */
    public boolean isEmpty(int x, int y, int z);
}
