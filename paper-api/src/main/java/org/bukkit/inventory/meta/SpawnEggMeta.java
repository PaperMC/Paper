package org.bukkit.inventory.meta;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    SpawnEggMeta clone();
}
