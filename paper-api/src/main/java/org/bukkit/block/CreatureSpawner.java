package org.bukkit.block;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;

/**
 * Represents a creature spawner.
 */
public interface CreatureSpawner extends BlockState {

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type.
     * @deprecated In favour of {@link #getSpawnedType()}.
     */
    @Deprecated
    public CreatureType getCreatureType();

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
     * Set the spawner creature type.
     *
     * @param creatureType The creature type.
     * @deprecated In favour of {@link #setSpawnedType(EntityType)}.
     */
    @Deprecated
    public void setCreatureType(CreatureType creatureType);

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type's name.
     * @deprecated Use {@link #getCreatureTypeName()}.
     */
    @Deprecated
    public String getCreatureTypeId();

    /**
     * Set the spawner mob type.
     *
     * @param creatureType The creature type's name.
     */
    public void setCreatureTypeByName(String creatureType);

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type's name.
     */
    public String getCreatureTypeName();

    /**
     * Set the spawner mob type.
     *
     * @param creatureType The creature type's name.
     * @deprecated Use {@link #setCreatureTypeByName(String)}.
     */
    @Deprecated
    public void setCreatureTypeId(String creatureType);

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
