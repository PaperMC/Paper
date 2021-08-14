package org.bukkit.generator;

import java.util.UUID;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Holds various information of a World
 */
public interface WorldInfo {

    /**
     * Gets the unique name of this world
     *
     * @return Name of this world
     */
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
}
