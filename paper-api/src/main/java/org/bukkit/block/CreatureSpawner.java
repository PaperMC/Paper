package org.bukkit.block;

import org.bukkit.spawner.Spawner;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a creature spawner.
 *
 * @since 1.0.0 R1
 */
public interface CreatureSpawner extends TileState, Spawner {

    /**
     * Set the spawner mob type.
     *
     * @param creatureType The creature type's name or null to clear.
     * @deprecated magic value, use
     * {@link #setSpawnedType(org.bukkit.entity.EntityType)}.
     * @since 1.1.0 R5
     */
    @Deprecated(since = "1.11.2")
    public void setCreatureTypeByName(@Nullable String creatureType);

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type's name if is set.
     * @deprecated magic value, use {@link #getSpawnedType()}.
     * @since 1.1.0 R5
     */
    @Deprecated(since = "1.11.2")
    @Nullable
    public String getCreatureTypeName();
}
