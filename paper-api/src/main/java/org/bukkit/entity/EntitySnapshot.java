package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an immutable copy of an entity's state. Can be used at any time to
 * create an instance of the stored entity.
 */
public interface EntitySnapshot {

    /**
     * Creates an entity using this template. Does not spawn the copy in the world.
     * <br>
     *
     * @param world the world to create the entity in
     * @return a copy of this entity.
     */
    @NotNull
    Entity createEntity(@NotNull World world);

    /**
     * Creates an entity using this template and spawns it at the provided location.
     *
     * @param to the location to copy to
     * @return the new entity.
     */
    @NotNull
    Entity createEntity(@NotNull Location to);

    /**
     * Gets the type of entity this template holds.
     *
     * @return the type
     */
    @NotNull
    EntityType getEntityType();

    /**
     * Get this EntitySnapshot as an NBT string.
     * <p>
     * This string should not be relied upon as a serializable value.
     *
     * @return the NBT string
     */
    @NotNull
    @ApiStatus.Experimental
    String getAsString();
}
