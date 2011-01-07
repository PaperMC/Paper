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
     * Gets the block at the given face<br />
     * <br />
     * This method is equal to getFace(face, 1)
     *
     * @param face Face of this block to return
     * @return Block at the given face
     * @see Block.getFace(BlockFace face, int distance);
     */
    Block getFace(BlockFace face);

    /**
     * Gets the block at the given distance of the given face<br />
     * <br />
     * For example, the following method places water at 100,102,100; two blocks
     * above 100,100,100.
     * <pre>
     * Block block = world.getBlockAt(100,100,100);
     * Block shower = block.getFace(BlockFace.Up, 2);
     * shower.setType(Material.WATER);
     * </pre>
     *
     * @param face Face of this block to return
     * @param distance Distance to get the block at
     * @return Block at the given face
     */
    Block getFace(BlockFace face, int distance);

    /**
     * Gets the block at the given offsets
     *
     * @param modX X-coordinate offset
     * @param modY Y-coordinate offset
     * @param modZ Z-coordinate offset
     * @return Block at the given offsets
     */
    Block getRelative(int modX, int modY, int modZ);

    /**
     * Gets the type of this block
     *
     * @return block type
     */
    Material getType();

    /**
     * Gets the type-ID of this block
     *
     * @return block type-ID
     */
    int getTypeID();
    
    /**
     * Gets the light level between 0-15
     * 
     * @return light level
     */
    byte getLightLevel();

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
    void setData(byte data);

    /**
     * Sets the type of this block
     *
     * @param type Material to change this block to
     */
    void setType(Material type);

    /**
     * Sets the type-ID of this block
     *
     * @param type Type-ID to change this block to
     */
    void setTypeID(int type);

    /**
     * Gets the face relation of this block compared to the given block<br />
     * <br />
     * For example:
     * <pre>
     * Block current = world.getBlockAt(100, 100, 100);
     * Block target = world.getBlockAt(100, 101, 100);
     *
     * current.getFace(target) == BlockFace.Up;
     * </pre>
     * <br />
     * If the given block is not connected to this block, null may be returned
     *
     * @param block Block to compare against this block
     * @return BlockFace of this block which has the requested block, or null
     */
    BlockFace getFace(Block block);
}
