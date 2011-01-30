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
     * @param x
     * @param y
     * @param z
     * @param typeId
     * @return true if the block was set successfully
     */
    public boolean setTypeId(int x, int y, int z, int typeId);

    /**
     * Set a block type and data at the specified coordinates.
     * 
     * @param x
     * @param y
     * @param z
     * @param typeId
     * @param data
     * @return true if the block was set successfully
     */
    public boolean setTypeIdAndData(int x, int y, int z, int typeId, int data);
    
    /**
     * Get the block type at the location.
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getTypeId(int x, int y, int z);
}
