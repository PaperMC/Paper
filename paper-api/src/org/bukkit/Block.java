
package org.bukkit;

/**
 * Represents a block
 */
public interface Block {
    /**
     * Gets the metadata for this block
     *
     * @return block specific metadata
     */
    byte getData();

    /**
     * Gets the block at the given face
     *
     * @param face Face of this block to return
     * @return Block at the given face
     */
    Block getFace(final BlockFace face);

    /**
     * Gets the block at the given offsets
     *
     * @param modX X-coordinate offset
     * @param modY Y-coordinate offset
     * @param modZ Z-coordinate offset
     * @return Block at the given offsets
     */
    Block getRelative(final int modX, final int modY, final int modZ);

    /**
     * Gets the type-ID of this block
     *
     * @return block type-ID
     */
    int getType();

    /**
     * Gets the world which contains this Block
     *
     * @return World containing this block
     */
    World getWorld();

    /**
     * Gets the x-coordinate of this block
     *
     * @return x-coordinate
     */
    int getX();

    /**
     * Gets the y-coordinate of this block
     *
     * @return y-coordinate
     */
    int getY();

    /**
     * Gets the z-coordinate of this block
     *
     * @return z-coordinate
     */
    int getZ();

    /**
     * Gets the chunk which contains this block
     *
     * @return Containing Chunk
     */
    Chunk getChunk();

    /**
     * Sets the metadata for this block
     *
     * @param data New block specific metadata
     */
    void setData(final byte data);

    /**
     * Sets the type-ID of this block
     *
     * @param type Type-ID to change this block to
     */
    void setType(final int type);
}
