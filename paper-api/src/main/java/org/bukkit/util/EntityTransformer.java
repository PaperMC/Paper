package org.bukkit.util;

import org.bukkit.entity.Entity;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A EntityTransformer is used to modify entities that are spawned by structure.
 */
@FunctionalInterface
@ApiStatus.Experimental
public interface EntityTransformer {

    /**
     * Transforms an entity in a structure.
     *
     * @param region the accessible region
     * @param x the x position of the entity
     * @param y the y position of the entity
     * @param z the z position of the entity
     * @param entity the entity
     * @param allowedToSpawn if the entity is allowed to spawn
     *
     * @return {@code true} if the entity should be spawned otherwise
     * {@code false}
     */
    boolean transform(@NotNull LimitedRegion region, int x, int y, int z, @NotNull Entity entity, boolean allowedToSpawn);
}
