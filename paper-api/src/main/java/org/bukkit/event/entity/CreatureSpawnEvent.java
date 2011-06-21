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
    private SpawnReason spawnReason;

    public CreatureSpawnEvent(Entity spawnee, CreatureType mobtype, Location loc, SpawnReason spawnReason) {
        super(Type.CREATURE_SPAWN, spawnee);
        this.creatureType = mobtype;
        this.location = loc;
        this.spawnReason = spawnReason;
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

    /**
     * Gets the reason for why the creature is being spawned.
     *
     * @return A SpawnReason value detailing the reason for the creature being spawned
     */
    public SpawnReason getSpawnReason() {
        return spawnReason;
    }

    /**
     * An enum to specify the type of spawning
     */
    public enum SpawnReason {

        /**
         * When something spawns from natural means
         */
        NATURAL,
        /**
         * When a creature spawns from a spawner
         */
        SPAWNER,
        /**
         * When a creature spawns from an egg
         */
        EGG,
        /**
         * When a creature spawns because of a lightning strike
         */
        LIGHTNING,
        /**
         * When a creature is spawned by a player that is sleeping
         */
        BED,
        /**
         * When a creature is manually spawned
         */
        CUSTOM
    }
}
