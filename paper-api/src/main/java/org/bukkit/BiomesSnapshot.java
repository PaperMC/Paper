package org.bukkit;

import org.bukkit.block.Biome;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a static, thread-safe snapshot of chunk of biomes.
 * <p>
 * Purpose is to allow clean, efficient copy of a biome data to be made, and
 * then handed off for processing in another thread
 */
@NullMarked
public interface BiomesSnapshot {

    /**
     * Gets the X-coordinate of this chunk
     *
     * @return X-coordinate
     */
    int getX();

    /**
     * Gets the Z-coordinate of this chunk
     *
     * @return Z-coordinate
     */
    int getZ();

    /**
     * Gets name of the world containing this chunk
     *
     * @return Parent World Name
     */
    String getWorldName();

    /**
     * Get biome at given coordinates
     *
     * @param x X-coordinate (0-15)
     * @param y Y-coordinate (world minHeight (inclusive) - world maxHeight (exclusive))
     * @param z Z-coordinate (0-15)
     * @return Biome at given coordinate
     */
    Biome getBiome(int x, int y, int z);

    /**
     * Get biome at given coordinates
     *
     * @param x     X-coordinate (0-15)
     * @param y     Y-coordinate (world minHeight (inclusive) - world maxHeight (exclusive))
     * @param z     Z-coordinate (0-15)
     * @param biome the biome to set at the give coordinate
     */
    void setBiome(int x, int y, int z, Biome biome);

    /**
     * Get raw biome temperature at given coordinates
     *
     * @param x X-coordinate (0-15)
     * @param y Y-coordinate (world minHeight (inclusive) - world maxHeight (exclusive))
     * @param z Z-coordinate (0-15)
     * @return temperature at given coordinate
     */
    double getRawBiomeTemperature(int x, int y, int z);

    /**
     * Tests if this chunk contains the specified biome.
     *
     * @param biome biome to test
     * @return if the biome is contained within
     */
    boolean contains(Biome biome);
}
