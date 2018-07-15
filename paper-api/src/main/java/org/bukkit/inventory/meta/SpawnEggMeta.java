package org.bukkit.inventory.meta;

import org.bukkit.entity.EntityType;

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
    EntityType getSpawnedType();

    /**
     * Set the type of entity this egg will spawn.
     *
     * @param type The entity type. May be null for implementation specific
     * default.
     * @deprecated different types are different items
     */
    @Deprecated
    void setSpawnedType(EntityType type);

    @Override
    SpawnEggMeta clone();
}
