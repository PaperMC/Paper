package org.bukkit.craftbukkit;

import org.bukkit.ChunkSnapshot;
import org.bukkit.block.Biome;

import net.minecraft.server.BiomeBase;
/**
 * Represents a static, thread-safe snapshot of chunk of blocks
 * Purpose is to allow clean, efficient copy of a chunk data to be made, and then handed off for processing in another thread (e.g. map rendering)
 */
public class CraftChunkSnapshot implements ChunkSnapshot {
    private final int x, z;
    private final String worldname;
    private final byte[] buf; // Flat buffer in uncompressed chunk file format
    private final byte[] hmap; // Height map
    private final long captureFulltime;
    private final BiomeBase[] biome;
    private final double[] biomeTemp;
    private final double[] biomeRain;

    private static final int BLOCKDATA_OFF = 32768;
    private static final int BLOCKLIGHT_OFF = BLOCKDATA_OFF + 16384;
    private static final int SKYLIGHT_OFF = BLOCKLIGHT_OFF + 16384;

    /**
     * Constructor
     */
    CraftChunkSnapshot(int x, int z, String wname, long wtime, byte[] buf, byte[] hmap, BiomeBase[] biome, double[] biomeTemp, double[] biomeRain) {
        this.x = x;
        this.z = z;
        this.worldname = wname;
        this.captureFulltime = wtime;
        this.buf = buf;
        this.hmap = hmap;
        this.biome = biome;
        this.biomeTemp = biomeTemp;
        this.biomeRain = biomeRain;
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

    /**
     * Gets name of the world containing this chunk
     *
     * @return Parent World Name
     */
    public String getWorldName() {
        return worldname;
    }

    /**
     * Get block type for block at corresponding coordinate in the chunk
     *
     * @param x 0-15
     * @param y 0-127
     * @param z 0-15
     * @return 0-255
     */
    public int getBlockTypeId(int x, int y, int z) {
        return buf[x << 11 | z << 7 | y] & 255;
    }

    /**
     * Get block data for block at corresponding coordinate in the chunk
     *
     * @param x 0-15
     * @param y 0-127
     * @param z 0-15
     * @return 0-15
     */
    public int getBlockData(int x, int y, int z) {
        int off = ((x << 10) | (z << 6) | (y >> 1)) + BLOCKDATA_OFF;

        return ((y & 1) == 0) ? (buf[off] & 0xF) : ((buf[off] >> 4) & 0xF);
    }

    /**
     * Get sky light level for block at corresponding coordinate in the chunk
     *
     * @param x 0-15
     * @param y 0-127
     * @param z 0-15
     * @return 0-15
     */
    public int getBlockSkyLight(int x, int y, int z) {
        int off = ((x << 10) | (z << 6) | (y >> 1)) + SKYLIGHT_OFF;

        return ((y & 1) == 0) ? (buf[off] & 0xF) : ((buf[off] >> 4) & 0xF);
    }

    /**
     * Get light level emitted by block at corresponding coordinate in the chunk
     *
     * @param x 0-15
     * @param y 0-127
     * @param z 0-15
     * @return 0-15
     */
    public int getBlockEmittedLight(int x, int y, int z) {
        int off = ((x << 10) | (z << 6) | (y >> 1)) + BLOCKLIGHT_OFF;

        return ((y & 1) == 0) ? (buf[off] & 0xF) : ((buf[off] >> 4) & 0xF);
    }

    /**
     * Gets the highest non-air coordinate at the given coordinates
     *
     * @param x X-coordinate of the blocks
     * @param z Z-coordinate of the blocks
     * @return Y-coordinate of the highest non-air block
     */
    public int getHighestBlockYAt(int x, int z) {
        return hmap[z << 4 | x] & 255;
    }

    /**
     * Get biome at given coordinates
     *
     * @param x X-coordinate
     * @param z Z-coordinate
     * @return Biome at given coordinate
     */
    public Biome getBiome(int x, int z) {
        BiomeBase base = biome[x << 4 | z];

        if (base == BiomeBase.RAINFOREST) {
            return Biome.RAINFOREST;
        } else if (base == BiomeBase.SWAMPLAND) {
            return Biome.SWAMPLAND;
        } else if (base == BiomeBase.SEASONAL_FOREST) {
            return Biome.SEASONAL_FOREST;
        } else if (base == BiomeBase.FOREST) {
            return Biome.FOREST;
        } else if (base == BiomeBase.SAVANNA) {
            return Biome.SAVANNA;
        } else if (base == BiomeBase.SHRUBLAND) {
            return Biome.SHRUBLAND;
        } else if (base == BiomeBase.TAIGA) {
            return Biome.TAIGA;
        } else if (base == BiomeBase.DESERT) {
            return Biome.DESERT;
        } else if (base == BiomeBase.PLAINS) {
            return Biome.PLAINS;
        } else if (base == BiomeBase.ICE_DESERT) {
            return Biome.ICE_DESERT;
        } else if (base == BiomeBase.TUNDRA) {
            return Biome.TUNDRA;
        } else if (base == BiomeBase.HELL) {
            return Biome.HELL;
        }
        return null;
    }

    /**
     * Get raw biome temperature (0.0-1.0) at given coordinate
     *
     * @param x X-coordinate
     * @param z Z-coordinate
     * @return temperature at given coordinate
     */
    public double getRawBiomeTemperature(int x, int z) {
        return biomeTemp[x << 4 | z];
    }

    /**
     * Get raw biome rainfall (0.0-1.0) at given coordinate
     *
     * @param x X-coordinate
     * @param z Z-coordinate
     * @return rainfall at given coordinate
     */
    public double getRawBiomeRainfall(int x, int z) {
        return biomeRain[x << 4 | z];
    }

    /**
     * Get world full time when chunk snapshot was captured
     * @return time in ticks
     */
    public long getCaptureFullTime() {
        return captureFulltime;
    }
}
