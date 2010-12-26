
package org.bukkit;

/**
 * Represents a world.
 *
 * Currently there is only one world in the default Minecraft spec, but this
 * may change with the addition of a functional Nether world
 */
public interface World {
    public Block getBlockAt(int x, int y, int z);

    public Chunk getChunkAt(int x, int z);

    public Chunk getChunkAt(Block block);

    public boolean isChunkLoaded();
}
