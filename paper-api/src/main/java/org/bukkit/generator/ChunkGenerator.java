package org.bukkit.generator;

import java.util.List;
import java.util.Random;
import org.bukkit.World;

/**
 * A chunk generator is responsible for the initial shaping of an entire chunk.
 * For example, the nether chunk generator should shape netherrack and soulsand
 */
public abstract class ChunkGenerator {
    /**
     * Shapes the chunk for the given coordinates.<br />
     * <br />
     * This method should return a byte[32768] in the following format:
     * <pre>
     * for (int x = 0; x < 16; x++) {
     *     for (int z = 0; z < 16; z++) {
     *         for (int y = 0; y < 128; y++) {
     *             // result[(x * 16 + z) * 128 + y] = ??;
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
     * @return byte[] containing the types for each block created by this generator
     */
    public abstract byte[] generate(World world, Random random, int x, int z);

    /**
     * Tests if the specified location is valid for a natural spawn position
     *
     * @param world The world we're testing on
     * @param x X-coordinate of the block to test
     * @param z Z-coordinate of the block to test
     * @return true if the location is valid, otherwise false
     */
    public abstract boolean canSpawn(World world, int x, int z);

    /**
     * Gets a list of default {@link BlockPopulator}s to apply to a given world
     *
     * @param world World to apply to
     * @return List containing any amount of BlockPopulators
     */
    public abstract List<BlockPopulator> getDefaultPopulators(World world);
}
