package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.EntityEffect;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.Metadatable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * Represents a base entity in the world
 */
public interface Entity extends Metadatable {

    /**
     * Gets the entity's current position
     *
     * @return Location containing the position of this entity
     */
    public Location getLocation();

    /**
     * Sets this entity's velocity
     *
     * @param velocity New velocity to travel with
     */
    public void setVelocity(Vector velocity);

    /**
     * Gets this entity's current velocity
     *
     * @return Current travelling velocity of this entity
     */
    public Vector getVelocity();

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
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Location location);

    /**
     * Teleports this entity to the given location
     *
     * @param location New location to teleport this entity to
     * @param cause The cause of this teleportation
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Location location, TeleportCause cause);

    /**
     * Teleports this entity to the target Entity
     *
     * @param destination Entity to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Entity destination);

    /**
     * Teleports this entity to the target Entity
     *
     * @param destination Entity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Entity destination, TeleportCause cause);

    /**
     * Returns a list of entities within a bounding box centered around this entity
     *
     * @param x 1/2 the size of the box along x axis
     * @param y 1/2 the size of the box along y axis
     * @param z 1/2 the size of the box along z axis
     * @return List<Entity> List of entities nearby
     */
    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z);

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
     * @param ticks Current ticks remaining
     */
    public void setFireTicks(int ticks);

    /**
     * Mark the entity's removal.
     */
    public void remove();

    /**
     * Returns true if this entity has been marked for removal.
     *
     * @return True if it is dead.
     */
    public boolean isDead();

    /**
     * Returns false if the entity has died or been despawned for some other
     * reason.
     * @return True if valid.
     */
    public boolean isValid();

    /**
     * Gets the {@link Server} that contains this Entity
     *
     * @return Server instance running this Entity
     */
    public Server getServer();

    /**
     * Gets the primary passenger of a vehicle. For vehicles that could have
     * multiple passengers, this will only return the primary passenger.
     *
     * @return an entity
     */
    public abstract Entity getPassenger();

    /**
     * Set the passenger of a vehicle.
     *
     * @param passenger The new passenger.
     * @return false if it could not be done for whatever reason
     */
    public abstract boolean setPassenger(Entity passenger);

    /**
     * Check if a vehicle has passengers.
     *
     * @return True if the vehicle has no passengers.
     */
    public abstract boolean isEmpty();

    /**
     * Eject any passenger.
     *
     * @return True if there was a passenger.
     */
    public abstract boolean eject();

    /**
     * Returns the distance this entity has fallen
     *
     * @return The distance.
     */
    public float getFallDistance();

    /**
     * Sets the fall distance for this entity
     *
     * @param distance The new distance.
     */
    public void setFallDistance(float distance);

    /**
     * Record the last {@link EntityDamageEvent} inflicted on this entity
     *
     * @param event a {@link EntityDamageEvent}
     */
    public void setLastDamageCause(EntityDamageEvent event);

    /**
     * Retrieve the last {@link EntityDamageEvent} inflicted on this entity. This event may have been cancelled.
     *
     * @return the last known {@link EntityDamageEvent} or null if hitherto unharmed
     */
    public EntityDamageEvent getLastDamageCause();

    /**
     * Returns a unique and persistent id for this entity
     *
     * @return unique id
     */
    public UUID getUniqueId();

    /**
     * Gets the amount of ticks this entity has lived for.
     * <p />
     * This is the equivalent to "age" in entities.
     *
     * @return Age of entity
     */
    public int getTicksLived();

    /**
     * Sets the amount of ticks this entity has lived for.
     * <p />
     * This is the equivalent to "age" in entities. May not be less than one tick.
     *
     * @param value Age of entity
     */
    public void setTicksLived(int value);

    /**
     * Performs the specified {@link EntityEffect} for this entity.
     * <p />
     * This will be viewable to all players near the entity.
     *
     * @param type Effect to play.
     */
    public void playEffect(EntityEffect type);

    /**
     * Get the type of the entity.
     * @return The entity type.
     */
    public EntityType getType();

    /**
     * Returns whether this entity is inside a vehicle.
     *
     * @return True if the entity is in a vehicle.
     */
    public boolean isInsideVehicle();

    /**
     * Leave the current vehicle. If the entity is currently in a vehicle
     * (and is removed from it), true will be returned, otherwise false will
     * be returned.
     *
     * @return True if the entity was in a vehicle.
     */
    public boolean leaveVehicle();

    /**
     * Get the vehicle that this player is inside. If there is no vehicle,
     * null will be returned.
     *
     * @return The current vehicle.
     */
    public Entity getVehicle();
}
