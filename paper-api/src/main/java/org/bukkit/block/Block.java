package org.bukkit.block;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.Location;

/**
 * Represents a block. This is a live object, and only one Block may exist for
 * any given location in a world. The state of the block may change concurrently
 * to your own handling of it; use block.getState() to get a snapshot state of a
 * block which will not be modified.
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
     * Gets the block at the given offsets
     *
     * @param face face
     * @return Block at the given offsets
     */
    Block getRelative(BlockFace face);

    /**
     * Gets the type of this block
     *
     * @return block type
     */
    Material getType();

    /**
     * Gets the type-id of this block
     *
     * @return block type-id
     */
    int getTypeId();

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
     * Gets the Location of the block
     *
     * @return Location of block
     */

    Location getLocation();

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
    
    void setData(byte data, boolean applyPhyiscs);

    /**
     * Sets the type of this block
     *
     * @param type Material to change this block to
     */
    void setType(Material type);

    /**
     * Sets the type-id of this block
     *
     * @param type Type-Id to change this block to
     * @return whether the block was changed
     */
    boolean setTypeId(int type);
    
    boolean setTypeId(int type, boolean applyPhysics);
    
    boolean setTypeIdAndData(int type, byte data, boolean applyPhyiscs);

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

    /**
     * Captures the current state of this block. You may then cast that state
     * into any accepted type, such as Furnace or Sign.
     *
     * The returned object will never be updated, and you are not guaranteed that
     * (for example) a sign is still a sign after you capture its state.
     *
     * @return BlockState with the current state of this block.
     */
    BlockState getState();

    /**
     * Returns the biome that this block resides in
     *
     * @return Biome type containing this block
     */
    Biome getBiome();

    /**
     * Returns true if the block is being powered by Redstone.
     *
     * @return
     */
    boolean isBlockPowered();

    /**
     * Returns true if the block is being indirectly powered by Redstone.
     *
     * @return
     */
    boolean isBlockIndirectlyPowered();

    /**
     * Returns true if the block face is being powered by Redstone.
     *
     * @return
     */
    boolean isBlockFacePowered(BlockFace face);

    /**
     * Returns true if the block face is being indirectly powered by Redstone.
     *
     * @return
     */
    boolean isBlockFaceIndirectlyPowered(BlockFace face);
   
    /**
     * Returns the redstone power being provided to this block face
     * 
     * @param face the face of the block to query or BlockFace.SELF for the block itself
     * @return 
     */
    int getBlockPower(BlockFace face);

    /**
     * Returns the redstone power being provided to this block
     * 
     * @return 
     */
    int getBlockPower();
}
