package org.bukkit.inventory.meta;

import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a spawn egg and it's spawned type.
 */
public interface SpawnEggMeta extends ItemMeta {

    /**
     * Get the type of entity this egg will spawn.
     *
     * @return The entity type. May be null for implementation specific default.
     * @deprecated different types are different items
     */
    @Deprecated
    @Contract("-> fail")
    EntityType getSpawnedType();

    /**
     * Set the type of entity this egg will spawn.
     *
     * @param type The entity type. May be null for implementation specific
     * default.
     * @deprecated different types are different items
     */
    @Deprecated
    @Contract("_ -> fail")
    void setSpawnedType(EntityType type);

    /**
     * Gets the {@link EntitySnapshot} that will be spawned by this spawn egg or null if no entity
     * has been set. <br>
     * <p>
     * All applicable data from the egg will be copied, such as custom name, health,
     * and velocity. <br>
     *
     * @return the entity snapshot or null if no entity has been set
     */
    @Nullable
    EntitySnapshot getSpawnedEntity();

    /**
     * Sets the {@link EntitySnapshot} that will be spawned by this spawn egg. <br>
     * <p>
     * All applicable data from the entity will be copied, such as custom name,
     * health, and velocity. <br>
     *
     * @param snapshot the snapshot
     */
    void setSpawnedEntity(@NotNull EntitySnapshot snapshot);

    @NotNull
    @Override
    SpawnEggMeta clone();
}
