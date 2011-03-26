package org.bukkit.event.entity;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

/**
 * Stores data for damage events
 */
public class CreatureSpawnEvent extends EntityEvent implements Cancellable {

    private Location location;
    private boolean canceled;
    private CreatureType creatureType;

    public CreatureSpawnEvent(Entity spawnee, CreatureType mobtype, Location loc) {
        super(Type.CREATURE_SPAWN, spawnee);
        this.creatureType = mobtype;
        this.location = loc;
    }

    /**
     * Gets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is canceled
     */
    public boolean isCancelled() {
        return canceled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the location at which the creature is spawning.
     * @return The location at which the creature is spawning
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the type of creature being spawned.
     *
     * @return A CreatureType value detailing the type of creature being spawned
     */
    public CreatureType getCreatureType() {
        return creatureType;
    }
}