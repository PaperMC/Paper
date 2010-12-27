
package org.bukkit.craftbukkit;

import org.bukkit.Chunk;

public class CraftChunk implements Chunk {
    private final int x;
    private final int z;

    protected CraftChunk(final int x, final int z) {
        this.x = x;
        this.z = z;
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
}
