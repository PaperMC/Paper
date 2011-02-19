
package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a base entity in the world
 */
public interface Entity {
    /**
     * Gets the entity's current position
     *
     * @return Location containing the position of this entity
     */
    public Location getLocation();

    /**
     * Gets the current world this entity resides in
     *
     * @return World
     */
    public World getWorld();

    /**
     * Teleports this entity to the given location
     *
     * @param location New location to teleport this entity to
     */
    public void teleportTo(Location location);

    /**
     * Teleports this entity to the target Entity
     *
     * @param destination Entity to teleport this entity to
     */
    public void teleportTo(Entity destination);

    /**
     * Returns a unique id for this entity
     *
     * @return Entity id
     */
    public int getEntityId();

    /**
     * Returns the entity's current fire ticks (ticks before the entity stops being on fire).
     *
     * @return int fireTicks
     */
    public int getFireTicks();

    /**
     * Returns the entity's maximum fire ticks.
     *
     * @return int maxFireTicks
     */
    public int getMaxFireTicks();

    /**
     * Sets the entity's current fire ticks (ticks before the entity stops being on fire).
     *
     * @param ticks
     */
    public void setFireTicks(int ticks);
    
    /**
     * Mark the entity's removal.
     */
    public void remove();
}
