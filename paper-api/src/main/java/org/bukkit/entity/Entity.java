
package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a base entity in the world
 */
public interface Entity {
    /**
     * Gets the entitys current position
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
}
