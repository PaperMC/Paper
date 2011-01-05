
package org.bukkit.craftbukkit;

import org.bukkit.Chunk;
import org.bukkit.World;

public class CraftChunk implements Chunk {
    private final CraftWorld world;
    private final int x;
    private final int z;

    protected CraftChunk(final CraftWorld world, final int x, final int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Gets the world containing this chunk
     *
     * @return World
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the X-coordinate of this chunk
     *
     * @return X-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Z-coordinate of this chunk
     *
     * @return Z-coordinate
     */
    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + x + "z=" + z + '}';
    }
}
