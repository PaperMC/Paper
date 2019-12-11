package org.bukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A chunk generator is responsible for the initial shaping of an entire
 * chunk. For example, the nether chunk generator should shape netherrack and
 * soulsand.
 *
 * By default only one thread will call
 * {@link #generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}
 * at a time, although this may not necessarily be the main server thread.
 *
 * If your generator is capable of fully asynchronous generation, then
 * {@link #isParallelCapable()} should be overriden accordingly to allow
 * multiple concurrent callers.
 */
public abstract class ChunkGenerator {

    /**
     * Interface to biome section for chunk to be generated: initialized with
     * default values for world type and seed.
     * <p>
     * Custom generator is free to access and tailor values during
     * generateBlockSections() or generateExtBlockSections().
     */
    public interface BiomeGrid {

        /**
         * Get biome at x, z within chunk being generated
         *
         * @param x - 0-15
         * @param z - 0-15
         * @return Biome value
         * @deprecated biomes are now 3-dimensional
         */
        @NotNull
        @Deprecated
        Biome getBiome(int x, int z);

        /**
         * Get biome at x, z within chunk being generated
         *
         * @param x - 0-15
         * @param y - 0-255
         * @param z - 0-15
         * @return Biome value
         */
        @NotNull
        Biome getBiome(int x, int y, int z);

        /**
         * Set biome at x, z within chunk being generated
         *
         * @param x - 0-15
         * @param z - 0-15
         * @param bio - Biome value
         * @deprecated biomes are now 3-dimensional
         */
        @Deprecated
        void setBiome(int x, int z, @NotNull Biome bio);

        /**
         * Set biome at x, z within chunk being generated
         *
         * @param x - 0-15
         * @param y - 0-255
         * @param z - 0-15
         * @param bio - Biome value
         */
        void setBiome(int x, int y, int z, @NotNull Biome bio);
    }

    /**
     * Shapes the chunk for the given coordinates.
     *
     * This method must return a ChunkData.
     * <p>
     * Notes:
     * <p>
     * This method should <b>never</b> attempt to get the Chunk at
     * the passed coordinates, as doing so may cause an infinite loop
     * <p>
     * This method should <b>never</b> modify a ChunkData after it has
     * been returned.
     * <p>
     * This method <b>must</b> return a ChunkData returned by {@link ChunkGenerator#createChunkData(org.bukkit.World)}
     *
     * @param world The world this chunk will be used for
     * @param random The random generator to use
     * @param x The X-coordinate of the chunk
     * @param z The Z-coordinate of the chunk
     * @param biome Proposed biome values for chunk - can be updated by
     *     generator
     * @return ChunkData containing the types for each block created by this
     *     generator
     */
    @NotNull
    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        throw new UnsupportedOperationException("Custom generator is missing required method generateChunkData");
    }

    /**
     * Create a ChunkData for a world.
     * @param world the world the ChunkData is for
     * @return a new ChunkData for world
     */
    @NotNull
    protected final ChunkData createChunkData(@NotNull World world) {
        return Bukkit.getServer().createChunkData(world);
    }

    /**
     * Tests if the specified location is valid for a natural spawn position
     *
     * @param world The world we're testing on
     * @param x X-coordinate of the block to test
     * @param z Z-coordinate of the block to test
     * @return true if the location is valid, otherwise false
     */
    public boolean canSpawn(@NotNull World world, int x, int z) {
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
     * Gets a list of default {@link BlockPopulator}s to apply to a given
     * world
     *
     * @param world World to apply to
     * @return List containing any amount of BlockPopulators
     */
    @NotNull
    public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return new ArrayList<BlockPopulator>();
    }

    /**
     * Gets a fixed spawn location to use for a given world.
     * <p>
     * A null value is returned if a world should not use a fixed spawn point,
     * and will instead attempt to find one randomly.
     *
     * @param world The world to locate a spawn point for
     * @param random Random generator to use in the calculation
     * @return Location containing a new spawn point, otherwise null
     */
    @Nullable
    public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return null;
    }

    /**
     * Gets if this ChunkGenerator is parallel capable.
     *
     * See {@link ChunkGenerator} for more information.
     *
     * @return parallel capable status
     */
    public boolean isParallelCapable() {
        return false;
    }

    /**
     * Data for a Chunk.
     */
    public static interface ChunkData {
        /**
         * Get the maximum height for the chunk.
         *
         * Setting blocks at or above this height will do nothing.
         *
         * @return the maximum height
         */
        public int getMaxHeight();

        /**
         * Set the block at x,y,z in the chunk data to material.
         *
         * Note: setting blocks outside the chunk's bounds does nothing.
         *
         * @param x the x location in the chunk from 0-15 inclusive
         * @param y the y location in the chunk from 0 (inclusive) - maxHeight (exclusive)
         * @param z the z location in the chunk from 0-15 inclusive
         * @param material the type to set the block to
         */
        public void setBlock(int x, int y, int z, @NotNull Material material);

        /**
         * Set the block at x,y,z in the chunk data to material.
         *
         * Setting blocks outside the chunk's bounds does nothing.
         *
         * @param x the x location in the chunk from 0-15 inclusive
         * @param y the y location in the chunk from 0 (inclusive) - maxHeight (exclusive)
         * @param z the z location in the chunk from 0-15 inclusive
         * @param material the type to set the block to
         */
        public void setBlock(int x, int y, int z, @NotNull MaterialData material);

        /**
         * Set the block at x,y,z in the chunk data to material.
         *
         * Setting blocks outside the chunk's bounds does nothing.
         *
         * @param x the x location in the chunk from 0-15 inclusive
         * @param y the y location in the chunk from 0 (inclusive) - maxHeight (exclusive)
         * @param z the z location in the chunk from 0-15 inclusive
         * @param blockData the type to set the block to
         */
        public void setBlock(int x, int y, int z, @NotNull BlockData blockData);

        /**
         * Set a region of this chunk from xMin, yMin, zMin (inclusive)
         * to xMax, yMax, zMax (exclusive) to material.
         *
         * Setting blocks outside the chunk's bounds does nothing.
         *
         * @param xMin minimum x location (inclusive) in the chunk to set
         * @param yMin minimum y location (inclusive) in the chunk to set
         * @param zMin minimum z location (inclusive) in the chunk to set
         * @param xMax maximum x location (exclusive) in the chunk to set
         * @param yMax maximum y location (exclusive) in the chunk to set
         * @param zMax maximum z location (exclusive) in the chunk to set
         * @param material the type to set the blocks to
         */
        public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, @NotNull Material material);

        /**
         * Set a region of this chunk from xMin, yMin, zMin (inclusive)
         * to xMax, yMax, zMax (exclusive) to material.
         *
         * Setting blocks outside the chunk's bounds does nothing.
         *
         * @param xMin minimum x location (inclusive) in the chunk to set
         * @param yMin minimum y location (inclusive) in the chunk to set
         * @param zMin minimum z location (inclusive) in the chunk to set
         * @param xMax maximum x location (exclusive) in the chunk to set
         * @param yMax maximum y location (exclusive) in the chunk to set
         * @param zMax maximum z location (exclusive) in the chunk to set
         * @param material the type to set the blocks to
         */
        public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, @NotNull MaterialData material);

        /**
         * Set a region of this chunk from xMin, yMin, zMin (inclusive) to xMax,
         * yMax, zMax (exclusive) to material.
         *
         * Setting blocks outside the chunk's bounds does nothing.
         *
         * @param xMin minimum x location (inclusive) in the chunk to set
         * @param yMin minimum y location (inclusive) in the chunk to set
         * @param zMin minimum z location (inclusive) in the chunk to set
         * @param xMax maximum x location (exclusive) in the chunk to set
         * @param yMax maximum y location (exclusive) in the chunk to set
         * @param zMax maximum z location (exclusive) in the chunk to set
         * @param blockData the type to set the blocks to
         */
        public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, @NotNull BlockData blockData);

        /**
         * Get the type of the block at x, y, z.
         *
         * Getting blocks outside the chunk's bounds returns air.
         *
         * @param x the x location in the chunk from 0-15 inclusive
         * @param y the y location in the chunk from 0 (inclusive) - maxHeight (exclusive)
         * @param z the z location in the chunk from 0-15 inclusive
         * @return the type of the block or Material.AIR if x, y or z are outside the chunk's bounds
         */
        @NotNull
        public Material getType(int x, int y, int z);

        /**
         * Get the type and data of the block at x, y, z.
         *
         * Getting blocks outside the chunk's bounds returns air.
         *
         * @param x the x location in the chunk from 0-15 inclusive
         * @param y the y location in the chunk from 0 (inclusive) - maxHeight (exclusive)
         * @param z the z location in the chunk from 0-15 inclusive
         * @return the type and data of the block or the MaterialData for air if x, y or z are outside the chunk's bounds
         */
        @NotNull
        public MaterialData getTypeAndData(int x, int y, int z);

        /**
         * Get the type and data of the block at x, y, z.
         *
         * Getting blocks outside the chunk's bounds returns air.
         *
         * @param x the x location in the chunk from 0-15 inclusive
         * @param y the y location in the chunk from 0 (inclusive) - maxHeight (exclusive)
         * @param z the z location in the chunk from 0-15 inclusive
         * @return the data of the block or the BlockData for air if x, y or z are outside the chunk's bounds
         */
        @NotNull
        public BlockData getBlockData(int x, int y, int z);

        /**
         * Get the block data at x,y,z in the chunk data.
         *
         * Getting blocks outside the chunk's bounds returns 0.
         *
         * @param x the x location in the chunk from 0-15 inclusive
         * @param y the y location in the chunk from 0 (inclusive) - maxHeight (exclusive)
         * @param z the z location in the chunk from 0-15 inclusive
         * @return the block data value or air if x, y or z are outside the chunk's bounds
         * @deprecated Uses magic values
         */
        @Deprecated
        public byte getData(int x, int y, int z);
    }
}
