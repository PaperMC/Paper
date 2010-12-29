
package org.bukkit.craftbukkit;

import org.bukkit.*;

public class CraftBlock implements Block {
    private final CraftWorld world;
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    protected int type;
    protected byte data;

    protected CraftBlock(final CraftWorld world, final int x, final int y, final int z, final int type, final byte data) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.data = data;
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
     * Sets the type-ID of this block
     *
     * @param type Type-ID to change this block to
     */
    public void setType(final int type) {
        this.type = type;
        world.getHandle().d(x, y, z, type);
    }

    /**
     * Gets the type-ID of this block
     *
     * @return block type-ID
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the block at the given face
     *
     * @param face Face of this block to return
     * @return Block at the given face
     */
    public Block getFace(final BlockFace face) {
        return getRelative(face.getModX(), face.getModY(), face.getModZ());
    }

    /**
     * Gets the block at the given offsets
     *
     * @param modX X-coordinate offset
     * @param modY Y-coordinate offset
     * @param modZ Z-coordinate offset
     * @return Block at the given offsets
     */
    public Block getRelative(final int modX, final int modY, final int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

    @Override
    public String toString() {
        return "CraftBlock{" + "world=" + world + "x=" + x + "y=" + y + "z=" + z + "type=" + type + "data=" + data + '}';
    }
}
