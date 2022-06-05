package org.bukkit.generator;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.world.WorldInitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A block populator is responsible for generating a small area of blocks.
 * <p>
 * For example, generating glowstone inside the nether or generating dungeons
 * full of treasure
 * <p>
 * A BlockPopulator can be used in combination with a custom {@link ChunkGenerator}
 * by returning it in the method {@link ChunkGenerator#getDefaultPopulators(World)}
 * or by adding it manually to the worlds populator list returned by {@link World#getPopulators()}.
 * <p>
 * When adding a BlockPopulator manually to a world it is recommended to do so during
 * the {@link WorldInitEvent}.
 */
public abstract class BlockPopulator {

    /**
     * Populates an area of blocks at or around the given chunk.
     * <p>
     * The chunks on each side of the specified chunk must already exist; that
     * is, there must be one north, east, south and west of the specified
     * chunk. The "corner" chunks may not exist, in which scenario the
     * populator should record any changes required for those chunks and
     * perform the changes when they are ready.
     *
     * @param world The world to generate in
     * @param random The random generator to use
     * @param source The chunk to generate for
     * @deprecated Use {@link #populate(WorldInfo, Random, int, int, LimitedRegion)}
     */
    @Deprecated
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
    }

    /**
     * Populates an area of blocks at or around the given chunk.
     * <p>
     * Notes:
     * <p>
     * This method should <b>never</b> attempt to get the Chunk at the passed
     * coordinates, as doing so may cause an infinite loop
     * <p>
     * This method should <b>never</b> modify a {@link LimitedRegion} at a later
     * point of time.
     * <p>
     * This method <b>must</b> be completely thread safe and able to handle
     * multiple concurrent callers.
     * <p>
     * No physics are applied, whether or not it is set to true in
     * {@link org.bukkit.block.BlockState#update(boolean, boolean)}
     * <p>
     * <b>Only</b> use the {@link org.bukkit.block.BlockState} returned by
     * {@link LimitedRegion},
     * <b>never</b> use methods from a {@link World} to modify the chunk.
     *
     * @param worldInfo The world info of the world to generate in
     * @param random The random generator to use
     * @param chunkX The X-coordinate of the chunk
     * @param chunkZ The Z-coordinate of the chunk
     * @param limitedRegion The chunk region to populate
     */
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
    }
}
