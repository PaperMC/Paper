package org.bukkit.generator;

import java.util.UUID;
import io.papermc.paper.world.flag.FeatureFlagSetHolder;
import org.bukkit.Keyed;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Holds various information of a World
 */
public interface WorldInfo extends FeatureFlagSetHolder, Keyed {

    /**
     * Gets the legacy Bukkit name of this world.
     *
     * <p>This method is considered obsolete and is a candidate for future deprecation.
     * Prefer using {@link #getKey()} as the world identity.</p>
     *
     * @return Bukkit name of this world
     */
    @ApiStatus.Obsolete
    @NotNull
    String getName();

    /**
     * Gets the Unique ID of this world
     *
     * @return Unique ID of this world.
     */
    @NotNull
    UUID getUID();

    /**
     * Gets the {@link World.Environment} type of this world
     *
     * @return This worlds Environment type
     */
    @NotNull
    World.Environment getEnvironment();

    /**
     * Gets the Seed for this world.
     *
     * @return This worlds Seed
     */
    long getSeed();

    /**
     * Gets the minimum height of this world.
     * <p>
     * If the min height is 0, there are only blocks from y=0.
     *
     * @return Minimum height of the world
     */
    int getMinHeight();

    /**
     * Gets the maximum height of this world.
     * <p>
     * If the max height is 100, there are only blocks from y=0 to y=99.
     *
     * @return Maximum height of the world
     */
    int getMaxHeight();

    // Paper start
    /**
     * Get the vanilla {@link BiomeProvider} for this world.
     *
     * @return vanilla biome provider
     */
    @NotNull BiomeProvider vanillaBiomeProvider();
    // Paper end
}
