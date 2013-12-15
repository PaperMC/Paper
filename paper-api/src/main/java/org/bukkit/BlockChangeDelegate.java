package org.bukkit;

/**
 * A delegate for handling block changes. This serves as a direct interface
 * between generation algorithms in the server implementation and utilizing
 * code.
 */
public interface BlockChangeDelegate {

    /**
     * Set a block type at the specified coordinates without doing all world
     * updates and notifications.
     * <p>
     * It is safe to have this call World.setTypeId, but it may be slower than
     * World.setRawTypeId.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param typeId New block ID
     * @return true if the block was set successfully
     * @deprecated Magic value
     */
    @Deprecated
    public boolean setRawTypeId(int x, int y, int z, int typeId);

    /**
     * Set a block type and data at the specified coordinates without doing
     * all world updates and notifications.
     * <p>
     * It is safe to have this call World.setTypeId, but it may be slower than
     * World.setRawTypeId.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param typeId New block ID
     * @param data Block data
     * @return true if the block was set successfully
     * @deprecated Magic value
     */
    @Deprecated
    public boolean setRawTypeIdAndData(int x, int y, int z, int typeId, int data);

    /**
     * Set a block type at the specified coordinates.
     * <p>
     * This method cannot call World.setRawTypeId, a full update is needed.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param typeId New block ID
     * @return true if the block was set successfully
     * @deprecated Magic value
     */
    @Deprecated
    public boolean setTypeId(int x, int y, int z, int typeId);

    /**
     * Set a block type and data at the specified coordinates.
     * <p>
     * This method cannot call World.setRawTypeId, a full update is needed.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param typeId New block ID
     * @param data Block data
     * @return true if the block was set successfully
     * @deprecated Magic value
     */
    @Deprecated
    public boolean setTypeIdAndData(int x, int y, int z, int typeId, int data);

    /**
     * Get the block type at the location.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return The block ID
     * @deprecated Magic value
     */
    @Deprecated
    public int getTypeId(int x, int y, int z);

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
