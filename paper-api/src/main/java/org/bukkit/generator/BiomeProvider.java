package org.bukkit.generator;

import java.util.List;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

/// Class for providing biomes.
public abstract class BiomeProvider {

    /// Return the Biome which should be present at the provided location.
    ///
    /// Notes:
    ///
    /// This method **must** be completely thread safe and able to handle
    /// multiple concurrent callers.
    ///
    /// This method should only return biomes which are present in the list
    /// returned by [#getBiomes(WorldInfo)]
    ///
    /// This method should **never** return [Biome#CUSTOM].
    ///
    /// @param worldInfo The world info of the world the biome will be used for
    /// @param x The X-coordinate from world origin
    /// @param y The Y-coordinate from world origin
    /// @param z The Z-coordinate from world origin
    /// @return Biome for the given location
    @NotNull
    public abstract Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z);

    /// Return the Biome which should be present at the provided location.
    ///
    /// Notes:
    ///
    /// This method **must** be completely thread safe and able to handle
    /// multiple concurrent callers.
    ///
    /// This method should only return biomes which are present in the list
    /// returned by [#getBiomes(WorldInfo)]
    ///
    /// This method should **never** return [Biome#CUSTOM].
    /// Only this method is called if both this and
    /// [#getBiome(WorldInfo, int, int, int)] are overridden.
    ///
    /// @param worldInfo The world info of the world the biome will be used for
    /// @param x The X-coordinate from world origin
    /// @param y The Y-coordinate from world origin
    /// @param z The Z-coordinate from world origin
    /// @param biomeParameterPoint The parameter point that is provided by default
    ///                       for this location (contains temperature, humidity,
    ///                       continentalness, erosion, depth and weirdness)
    /// @return Biome for the given location
    /// @see #getBiome(WorldInfo, int, int, int)
    @NotNull
    public Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z, @NotNull BiomeParameterPoint biomeParameterPoint) {
        return getBiome(worldInfo, x, y, z);
    }

    /// Returns a list with every biome the [BiomeProvider] will use for
    /// the given world.
    ///
    /// Notes:
    ///
    /// This method only gets called once, when the world is loaded. Returning
    /// another list or modifying the values from the initial returned list later
    /// one, are not respected.
    ///
    /// This method should **never** return a list which contains
    /// [Biome#CUSTOM].
    ///
    /// @param worldInfo The world info of the world the list will be used for
    /// @return A list with every biome the [BiomeProvider] uses
    @NotNull
    public abstract List<Biome> getBiomes(@NotNull WorldInfo worldInfo);
}
