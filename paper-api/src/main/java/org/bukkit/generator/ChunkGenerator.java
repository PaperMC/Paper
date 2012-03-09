package org.bukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

/**
 * A chunk generator is responsible for the initial shaping of an entire chunk.
 * For example, the nether chunk generator should shape netherrack and soulsand
 */
public abstract class ChunkGenerator {
    /**
     * Interface to biome data for chunk to be generated: initialized with default values for world type and seed.
     *
     * Custom generator is free to access and tailor values during generateBlockSections() or generateExtBlockSections().
     */
    public interface BiomeGrid {
        /**
         * Get biome at x, z within chunk being generated
         * @param x - 0-15
         * @param z - 0-15
         * @return Biome value
         */
        Biome getBiome(int x, int z);
        /**
         * Set biome at x, z within chunk being generated
         *
         * @param x - 0-15
         * @param z - 0-15
         * @param bio - Biome value
         */
        void setBiome(int x, int z, Biome bio);
    }
    @Deprecated
    /**
     * Shapes the chunk for the given coordinates.<br />
     * <br />
     * This method should return a byte[32768] in the following format:
     *
     * <pre>
     * for (int x = 0; x &lt; 16; x++) {
     *     for (int z = 0; z &lt; 16; z++) {
     *         for (int y = 0; y &lt; 128; y++) {
     *             // result[(x * 16 + z) * 128 + y] = ??;
     *         }
     *     }
     * }
     * </pre>
     *
     * Note that this method should <b>never</b> attempt to get the Chunk at
     * the passed coordinates, as doing so may cause an infinite loop
     *
     * Note this deprecated method will only be called when both generateExtBlockSections()
     * and generateBlockSections() are unimplemented and return null.

     * @param world The world this chunk will be used for
     * @param random The random generator to use
     * @param x The X-coordinate of the chunk
     * @param z The Z-coordinate of the chunk
     * @return byte[] containing the types for each block created by this generator
     */
    public byte[] generate(World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Custom generator is missing required methods: generate(), generateBlockSections() and generateExtBlockSections()");
    }

    /**
     * Shapes the chunk for the given coordinates.<br />
     * <br />
     * This method should return a short[][] array in the following format:
     *
     * <pre>
     * result[y >> 4] = null IF the 16x16x16 section from y to y+15 is empty
     * result[y >> 4] = new short[4096] if the section has any non-empty blocks
     *
     * within the section:
     *
     * for (int x = 0; x &lt; 16; x++) {
     *     for (int z = 0; z &lt; 16; z++) {
     *         for (int yy = y & 0xF; yy &lt; 16; yy++) {
     *             result[(yy << 8) | (z << 4) | x] = ??;
     *         }
     *     }
     * }
     * </pre>
     *
     * Note that this method should <b>never</b> attempt to get the Chunk at
     * the passed coordinates, as doing so may cause an infinite loop
     *
     * Note generators that do not return block IDs above 255 should not implement
     * this method, or should have it return null (which will result in the
     * generateBlockSections() method being called).
     *
     * @param world The world this chunk will be used for
     * @param random The random generator to use
     * @param x The X-coordinate of the chunk
     * @param z The Z-coordinate of the chunk
     * @param biomes Proposed biome values for chunk - can be updated by generator
     * @return short[][] containing the types for each block created by this generator
     */
    public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
        return null; // Default - returns null, which drives call to generateBlockSections()
    }

    /**
     * Shapes the chunk for the given coordinates.<br />
     * <br />
     * This method should return a byte[][] array in the following format:
     *
     * <pre>
     * result[y >> 4] = null IF the 16x16x16 section from y to y+15 is empty
     * result[y >> 4] = new byte[4096] if the section has any non-empty blocks
     *
     * within the section:
     *
     * for (int x = 0; x &lt; 16; x++) {
     *     for (int z = 0; z &lt; 16; z++) {
     *         for (int yy = y & 0xF; yy &lt; 16; yy++) {
     *             result[(yy << 8) | (z << 4) | x] = ??;
     *         }
     *     }
     * }
     * </pre>
     *
     * Note that this method should <b>never</b> attempt to get the Chunk at
     * the passed coordinates, as doing so may cause an infinite loop
     *
     * @param world The world this chunk will be used for
     * @param random The random generator to use
     * @param x The X-coordinate of the chunk
     * @param z The Z-coordinate of the chunk
     * @param biomes Proposed biome values for chunk - can be updated by generator
     * @return short[][] containing the types for each block created by this generator
     */
    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
        return null; // Default - returns null, which drives call to generate()
    }

    /**
     * Tests if the specified location is valid for a natural spawn position
     *
     * @param world The world we're testing on
     * @param x X-coordinate of the block to test
     * @param z Z-coordinate of the block to test
     * @return true if the location is valid, otherwise false
     */
    public boolean canSpawn(World world, int x, int z) {
        Block highest = world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);

        switch (world.getEnvironment()) {
        case NETHER:
            return true;
        case THE_END:
            return highest.getType() != Material.AIR && highest.getType() != Material.WATER && highest.getType() != Material.LAVA;
        case NORMAL:
        default:
            return highest.getType() == Material.SAND || highest.getType() == Material.GRAVEL;
        }
    }

    /**
     * Gets a list of default {@link BlockPopulator}s to apply to a given world
     *
     * @param world World to apply to
     * @return List containing any amount of BlockPopulators
     */
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return new ArrayList<BlockPopulator>();
    }

    /**
     * Gets a fixed spawn location to use for a given world.
     * <p />
     * A null value is returned if a world should not use a fixed spawn point,
     * and will instead attempt to find one randomly.
     *
     * @param world The world to locate a spawn point for
     * @param random Random generator to use in the calculation
     * @return Location containing a new spawn point, otherwise null
     */
    public Location getFixedSpawnLocation(World world, Random random) {
        return null;
    }
}
