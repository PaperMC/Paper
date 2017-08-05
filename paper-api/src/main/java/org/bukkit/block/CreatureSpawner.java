package org.bukkit.block;

import org.bukkit.entity.EntityType;

/**
 * Represents a captured state of a creature spawner.
 */
public interface CreatureSpawner extends BlockState {

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type.
     */
    public EntityType getSpawnedType();

    /**
     * Set the spawner's creature type.
     *
     * @param creatureType The creature type.
     */
    public void setSpawnedType(EntityType creatureType);

    /**
     * Set the spawner mob type.
     *
     * @param creatureType The creature type's name.
     * @deprecated magic value, use
     * {@link #setSpawnedType(org.bukkit.entity.EntityType)}.
     */
    @Deprecated
    public void setCreatureTypeByName(String creatureType);

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type's name.
     * @deprecated magic value, use {@link #getSpawnedType()}.
     */
    @Deprecated
    public String getCreatureTypeName();

    /**
     * Get the spawner's delay.
     *
     * @return The delay.
     */
    public int getDelay();

    /**
     * Set the spawner's delay.
     *
     * @param delay The delay.
     */
    public void setDelay(int delay);
}
