package org.bukkit.block;

import org.bukkit.entity.CreatureType;

/**
 * Represents a creature spawner.
 */
public interface CreatureSpawner extends BlockState {

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type.
     */
    public CreatureType getCreatureType();

    /**
     * Set the spawner creature type.
     *
     * @param creatureType The creature type.
     */
    public void setCreatureType(CreatureType creatureType);

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type's name.
     */
    public String getCreatureTypeId();

    /**
     * Set the spawner mob type.
     *
     * @param creatureType The creature type's name.
     */
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
