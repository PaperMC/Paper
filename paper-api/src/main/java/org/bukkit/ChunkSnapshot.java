package org.bukkit;

import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a static, thread-safe snapshot of chunk of blocks.
 * <p>
 * Purpose is to allow clean, efficient copy of a chunk data to be made, and
 * then handed off for processing in another thread (e.g. map rendering)
 */
public interface ChunkSnapshot extends BiomesSnapshot {

    /**
     * Get block type for block at corresponding coordinate in the chunk
     *
     * @param x 0-15
     * @param y world minHeight (inclusive) - world maxHeight (exclusive)
     * @param z 0-15
     * @return block material type
     */
    @NotNull
    Material getBlockType(int x, int y, int z);

    /**
     * Get block data for block at corresponding coordinate in the chunk
     *
     * @param x 0-15
     * @param y world minHeight (inclusive) - world maxHeight (exclusive)
     * @param z 0-15
     * @return block material type
     */
    @NotNull
    BlockData getBlockData(int x, int y, int z);

    /**
     * Get block data for block at corresponding coordinate in the chunk
     *
     * @param x 0-15
     * @param y world minHeight (inclusive) - world maxHeight (exclusive)
     * @param z 0-15
     * @return 0-15
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    int getData(int x, int y, int z);

    /**
     * Get sky light level for block at corresponding coordinate in the chunk
     *
     * @param x 0-15
     * @param y world minHeight (inclusive) - world maxHeight (exclusive)
     * @param z 0-15
     * @return 0-15
     */
    int getBlockSkyLight(int x, int y, int z);

    /**
     * Get light level emitted by block at corresponding coordinate in the
     * chunk
     *
     * @param x 0-15
     * @param y world minHeight (inclusive) - world maxHeight (exclusive)
     * @param z 0-15
     * @return 0-15
     */
    int getBlockEmittedLight(int x, int y, int z);

    /**
     * Gets the highest non-air coordinate at the given coordinates
     *
     * @param x X-coordinate of the blocks (0-15)
     * @param z Z-coordinate of the blocks (0-15)
     * @return Y-coordinate of the highest non-air block
     */
    int getHighestBlockYAt(int x, int z);

    /**
     * Get biome at given coordinates
     *
     * @param x X-coordinate (0-15)
     * @param z Z-coordinate (0-15)
     * @return Biome at given coordinate
     * @deprecated biomes are now 3-dimensional
     */
    @NotNull
    @Deprecated(since = "1.15")
    Biome getBiome(int x, int z);

    /**
     * Get raw biome temperature at given coordinates
     *
     * @param x X-coordinate (0-15)
     * @param z Z-coordinate (0-15)
     * @return temperature at given coordinate
     * @deprecated biomes are now 3-dimensional
     */
    @Deprecated(since = "1.15")
    double getRawBiomeTemperature(int x, int z);

    /**
     * Get world full time when chunk snapshot was captured
     *
     * @return time in ticks
     */
    long getCaptureFullTime();

    /**
     * Test if section is empty
     *
     * @param sy - section Y coordinate (block Y / 16, world minHeight (inclusive) - world maxHeight (exclusive))
     * @return true if empty, false if not
     */
    boolean isSectionEmpty(int sy);

    /**
     * Tests if this snapshot contains the specified block.
     *
     * @param block block to test
     * @return if the block is contained within
     */
    boolean contains(@NotNull BlockData block);
}
