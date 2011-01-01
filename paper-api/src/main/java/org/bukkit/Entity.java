
package org.bukkit;

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
     * Returns a unique ID for this entity
     * 
     * @return Entity ID
     */
    public int getEntityID();
}
