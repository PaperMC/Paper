package org.bukkit;

/**
 * A delegate for handling block changes. This serves as a direct interface
 * between generation algorithms in the server implementation and utilizing
 * code.
 *
 * @author sk89q
 */
public interface BlockChangeDelegate {

    /**
     * Set a block type at the specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param typeId New block ID
     * @return true if the block was set successfully
     */
    public boolean setRawTypeId(int x, int y, int z, int typeId);

    /**
     * Set a block type and data at the specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param typeId New block ID
     * @param data Block data
     * @return true if the block was set successfully
     */
    public boolean setRawTypeIdAndData(int x, int y, int z, int typeId, int data);

    /**
     * Get the block type at the location.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return The block ID
     */
    public int getTypeId(int x, int y, int z);

    /**
     * Gets the height of the world.
     *
     * @return Height of the world
     */
    public int getHeight();
}
