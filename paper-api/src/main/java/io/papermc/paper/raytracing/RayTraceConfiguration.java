package io.papermc.paper.raytracing;

import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Predicate;

/**
 * Holds information about how to cast a raytrace.
 */
public interface RayTraceConfiguration {

    /**
     * Creates a new builder.
     *
     * @return the new builder
     */
    @NotNull
    static Builder builder() {
        return new RayTraceConfigurationBuilderImpl();
    }

    /**
     * Gets the maximum distance.
     *
     * @return the maximum distance
     */
    double maxDistance();

    /**
     * Gets the FluidCollisionMode when looking for block collisions.
     *
     * @return the FluidCollisionMode
     */
    @Nullable
    FluidCollisionMode fluidCollisionMode();

    /**
     * Gets if the raytrace will ignore passable blocks when looking for block collisions.
     *
     * @return if the raytrace will ignore passable blocks
     */
    boolean ignorePassableBlocks();

    /**
     * Gets the size of the raytrace when looking for entity collisions.
     *
     * @return the raytrace size
     */
    double raySize();

    /**
     * Gets the current entity filter.
     *
     * @return predicate for entities the ray can potentially collide with, or null to consider all entities
     */
    @Nullable
    Predicate<? super Entity> entityFilter();

    /**
     * Gets the current block filter.
     *
     * @return predicate for blocks the ray can potentially collide with, or null to consider all blocks
     */
    @Nullable
    Predicate<? super Block> blockFilter();

    /**
     * Gets which RayTraceTargets this configuration was made for.
     *
     * @return the targets
     */
    @NotNull
    List<Targets> targets();

    /**
     * Helps you create a RayTraceConfiguration.
     */
    interface Builder {

        /**
         * Gets the maximum distance.
         *
         * @return the maximum distance
         */
        double maxDistance();

        /**
         * Sets the maximum distance.
         *
         * @param maxDistance the new maxDistance
         * @return a reference to this object
         */
        @NotNull
        @Contract("_ -> this")
        Builder maxDistance(double maxDistance);

        /**
         * Gets the FluidCollisionMode when looking for block collisions.
         *
         * @return the FluidCollisionMode
         */
        @Nullable
        FluidCollisionMode fluidCollisionMode();

        /**
         * Sets the FluidCollisionMode when looking for block collisions.
         *
         * @param fluidCollisionMode the new FluidCollisionMode
         * @return a reference to this object
         */
        @NotNull
        @Contract("_ -> this")
        Builder fluidCollisionMode(@Nullable FluidCollisionMode fluidCollisionMode);

        /**
         * Gets if the raytrace will ignore passable blocks when looking for block collisions.
         *
         * @return if the raytrace will ignore passable blocks
         */
        boolean ignorePassableBlocks();

        /**
         * Gets if the raytrace will ignore passable blocks when looking for block collisions.
         *
         * @param ignorePassableBlocks if the raytrace should ignore passable blocks
         * @return a reference to this object
         */
        @NotNull
        @Contract("_ -> this")
        Builder ignorePassableBlocks(boolean ignorePassableBlocks);

        /**
         * Gets the size of the raytrace when looking for entity collisions.
         *
         * @return the raytrace size
         */
        double raySize();

        /**
         * Sets the size of the raytrace when looking for entity collisions.
         *
         * @param raySize the new raytrace size
         * @return a reference to this object
         */
        @NotNull
        @Contract("_ -> this")
        Builder raySize(double raySize);

        /**
         * Gets the current entity filter when looking for entity collisions.
         *
         * @return predicate for entities the ray can potentially collide with, or null to consider all entities
         */
        @Nullable
        Predicate<? super Entity> entityFilter();

        /**
         * Sets the current entity filter when looking for entity collisions.
         *
         * @param entityFilter predicate for entities the ray can potentially collide with, or null to consider all entities
         * @return a reference to this object
         */
        @NotNull
        @Contract("_ -> this")
        Builder entityFilter(@Nullable Predicate<? super Entity> entityFilter);

        /**
         * Gets the current block filter when looking for block collisions.
         *
         * @return predicate for blocks the ray can potentially collide with, or null to consider all blocks
         */
        @Nullable
        Predicate<? super Block> blockFilter();

        /**
         * Sets the current block filter when looking for block collisions.
         *
         * @param blockFilter predicate for blocks the ray can potentially collide with, or null to consider all blocks
         * @return a reference to this object
         */
        @NotNull
        @Contract("_ -> this")
        Builder blockFilter(@Nullable Predicate<? super Block> blockFilter);

        /**
         * Builds a configuration based on the provided targets.
         *
         * @return the configuration
         */
        @NotNull
        RayTraceConfiguration target(@NotNull Targets first, @NotNull Targets... others);
    }

    /**
     * List of Targets the builder can target.
     */
    enum Targets {
        ENTITIES,
        BLOCKS
    }
}
