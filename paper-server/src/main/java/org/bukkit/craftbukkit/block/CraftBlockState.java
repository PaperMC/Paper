
package org.bukkit.craftbukkit.block;

import org.bukkit.Block;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftBlockState implements BlockState {
    private final CraftWorld world;
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    protected int type;
    protected byte data;
    protected byte light;

    public CraftBlockState(final CraftWorld world, final int x, final int y, final int z, final int type, final byte data) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.data = data;
        this.light = (byte)world.getHandle().i(x, y, z);
        this.chunk = (CraftChunk)world.getChunkAt(x << 4, z << 4);
    }

    /**
     * Gets the world which contains this Block
     *
     * @return World containing this block
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the x-coordinate of this block
     *
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of this block
     *
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the z-coordinate of this block
     *
     * @return z-coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the chunk which contains this block
     *
     * @return Containing Chunk
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * Sets the metadata for this block
     *
     * @param data New block specific metadata
     */
    public void setData(final byte data) {
        this.data = data;
        world.getHandle().c(x, y, z, data);
    }

    /**
     * Gets the metadata for this block
     *
     * @return block specific metadata
     */
    public byte getData() {
        return data;
    }

    /**
     * Sets the type of this block
     *
     * @param type Material to change this block to
     */
    public void setType(final Material type) {
        setTypeID(type.getID());
    }

    /**
     * Sets the type-ID of this block
     *
     * @param type Type-ID to change this block to
     */
    public void setTypeID(final int type) {
        this.type = type;
        world.getHandle().d(x, y, z, type);
    }

    /**
     * Gets the type of this block
     *
     * @return block type
     */
    public Material getType() {
        return Material.getMaterial(getTypeID());
    }

    /**
     * Gets the type-ID of this block
     *
     * @return block type-ID
     */
    public int getTypeID() {
        return type;
    }

    /**
     * Gets the light level between 0-15
     *
     * @return light level
     */
    public byte getLightLevel() {
        return light;
    }

    public Block getBlock() {
        return world.getBlockAt(x, y, z);
    }
}
