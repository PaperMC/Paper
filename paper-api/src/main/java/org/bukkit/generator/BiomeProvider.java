package org.bukkit.generator;

import java.util.List;
import java.util.Optional;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

/**
 * Class for providing biomes.
 */
public abstract class BiomeProvider {

    /**
     * Return the Biome which should be present at the provided location.
     * <p>
     * Notes:
     * <p>
     * This method <b>must</b> be completely thread safe and able to handle
     * multiple concurrent callers.
     * <p>
     * This method should only return biomes which are present in the list
     * returned by {@link #getBiomes(WorldInfo)}
     * <p>
     * This method should <b>never</b> return {@link Biome#CUSTOM}.
     *
     * @param worldInfo The world info of the world the biome will be used for
     * @param x The X-coordinate from world origin
     * @param y The Y-coordinate from world origin
     * @param z The Z-coordinate from world origin
     * @return Biome for the given location
     */
    @NotNull
    public abstract Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z);

    /**
     * Return the Biome which should be present at the provided location.
     * <p>
     * Notes:
     * <p>
     * This method <b>must</b> be completely thread safe and able to handle
     * multiple concurrent callers.
     * <p>
     * This method should only return biomes which are present in the list
     * returned by {@link #getBiomes(WorldInfo)}
     * <p>
     * This method should <b>never</b> return {@link Biome#CUSTOM}.
     * Only this method is called if both this and
     * {@link #getBiome(WorldInfo, int, int, int)} are overridden.
     *
     * @param worldInfo The world info of the world the biome will be used for
     * @param x The X-coordinate from world origin
     * @param y The Y-coordinate from world origin
     * @param z The Z-coordinate from world origin
     * @param biomeParameterPoint The parameter point that is provided by default
     *                       for this location (contains temperature, humidity,
     *                       continentalness, erosion, depth and weirdness)
     * @return Biome for the given location
     * @see #getBiome(WorldInfo, int, int, int)
     */
    @NotNull
    public Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z, @NotNull BiomeParameterPoint biomeParameterPoint) {
        return getBiome(worldInfo, x, y, z);
    }

    /**
     * Return a biome for structure placement searching (e.g. stronghold ring
     * position computation), bypassing expensive biome pipeline evaluation.
     * <p>
     * Implementations may return a coarse approximation sufficient to determine
     * whether a position is eligible for a structure (e.g. land vs ocean).
     * Return {@link Optional#empty()} to fall back to the full
     * {@link #getBiome(WorldInfo, int, int, int)} path.
     * <p>
     * This is called from {@code findBiomeHorizontal} which runs on background
     * executor threads. Implementations must be thread-safe.
     *
     * @param worldInfo The world info of the world being searched
     * @param x The X block coordinate
     * @param z The Z block coordinate
     * @return An optional biome for fast structure placement eligibility
     *         checking, or empty to use the full pipeline
     */
    @NotNull
    public Optional<Biome> getStructurePlacementBiome(@NotNull WorldInfo worldInfo, int x, int z) {
        return Optional.empty();
    }

    /**
     * Returns a list with every biome the {@link BiomeProvider} will use for
     * the given world.
     * <p>
     * Notes:
     * <p>
     * This method only gets called once, when the world is loaded. Returning
     * another list or modifying the values from the initial returned list later
     * one, are not respected.
     * <p>
     * This method should <b>never</b> return a list which contains
     * {@link Biome#CUSTOM}.
     *
     * @param worldInfo The world info of the world the list will be used for
     * @return A list with every biome the {@link BiomeProvider} uses
     */
    @NotNull
    public abstract List<Biome> getBiomes(@NotNull WorldInfo worldInfo);
}
