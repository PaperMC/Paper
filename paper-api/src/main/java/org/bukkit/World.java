
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
    
    /**
     * Spawns an arrow.
     * 
     * @param loc
     * @param velocity velocity vector
     * @param speed a reasonable speed is 0.6
     * @param spread a reasonable spread is 12
     * @return the arrow entity
     */
    public ArrowEntity spawnArrow(Location loc, Vector velocity,
            float speed, float spread);
}
