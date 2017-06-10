package org.bukkit;

/**
 * Further information regarding heightmaps.
 *
 * @see <a href="https://minecraft.wiki/w/Chunk_format">Minecraft Wiki</a>
 */
public enum HeightMap {

    /**
     * The highest block that blocks motion or contains a fluid.
     */
    MOTION_BLOCKING,
    /**
     * The highest block that blocks motion or contains a fluid, excluding leaves.
     */
    MOTION_BLOCKING_NO_LEAVES,
    /**
     * The highest non-air block, solid block.
     */
    OCEAN_FLOOR,
    /**
     * The highest block that is neither air nor contains a fluid, for worldgen.
     */
    OCEAN_FLOOR_WG,
    /**
     * The highest non-air block.
     */
    WORLD_SURFACE,
    /**
     * The highest non-air block, for worldgen.
     */
    WORLD_SURFACE_WG,
}
