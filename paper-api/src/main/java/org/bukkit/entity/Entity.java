package org.bukkit.entity;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.material.Directional;
import org.bukkit.metadata.Metadatable;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a base entity in the world
 * <p>
 * Not all methods are guaranteed to work/may have side effects when
 * {@link #isInWorld()} is false.
 */
public interface Entity extends Metadatable, CommandSender, Nameable, PersistentDataHolder {

    /**
     * Gets the entity's current position
     *
     * @return a new copy of Location containing the position of this entity
     */
    @NotNull
    public Location getLocation();

    /**
     * Stores the entity's current position in the provided Location object.
     * <p>
     * If the provided Location is null this method does nothing and returns
     * null.
     *
     * @param loc the location to copy into
     * @return The Location object provided or null
     */
    @Contract("null -> null; !null -> !null")
    @Nullable
    public Location getLocation(@Nullable Location loc);

    /**
     * Sets this entity's velocity in meters per tick
     *
     * @param velocity New velocity to travel with
     */
    public void setVelocity(@NotNull Vector velocity);

    /**
     * Gets this entity's current velocity
     *
     * @return Current traveling velocity of this entity
     */
    @NotNull
    public Vector getVelocity();

    /**
     * Gets the entity's height
     *
     * @return height of entity
     */
    public double getHeight();

    /**
     * Gets the entity's width
     *
     * @return width of entity
     */
    public double getWidth();

    /**
     * Gets the entity's current bounding box.
     * <p>
     * The returned bounding box reflects the entity's current location and
     * size.
     *
     * @return the entity's current bounding box
     */
    @NotNull
    public BoundingBox getBoundingBox();

    /**
     * Returns true if the entity is supported by a block. This value is a
     * state updated by the server and is not recalculated unless the entity
     * moves.
     *
     * @return True if entity is on ground.
     * @see Player#isOnGround()
     */
    public boolean isOnGround();

    /**
     * Returns true if the entity is in water.
     *
     * @return <code>true</code> if the entity is in water.
     */
    public boolean isInWater();

    /**
     * Gets the current world this entity resides in
     *
     * @return World
     */
    @NotNull
    public World getWorld();

    /**
     * Sets the entity's rotation.
     * <p>
     * Note that if the entity is affected by AI, it may override this rotation.
     *
     * @param yaw the yaw
     * @param pitch the pitch
     * @throws UnsupportedOperationException if used for players
     */
    public void setRotation(float yaw, float pitch);

    /**
     * Teleports this entity to the given location. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param location New location to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(@NotNull Location location);

    /**
     * Teleports this entity to the given location. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param location New location to teleport this entity to
     * @param cause The cause of this teleportation
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(@NotNull Location location, @NotNull TeleportCause cause);

    /**
     * Teleports this entity to the target Entity. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param destination Entity to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(@NotNull Entity destination);

    /**
     * Teleports this entity to the target Entity. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param destination Entity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(@NotNull Entity destination, @NotNull TeleportCause cause);

    /**
     * Returns a list of entities within a bounding box centered around this
     * entity
     *
     * @param x 1/2 the size of the box along x axis
     * @param y 1/2 the size of the box along y axis
     * @param z 1/2 the size of the box along z axis
     * @return {@code List<Entity>} List of entities nearby
     */
    @NotNull
    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z);

    /**
     * Returns a unique id for this entity
     *
     * @return Entity id
     */
    public int getEntityId();

    /**
     * Returns the entity's current fire ticks (ticks before the entity stops
     * being on fire).
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
     * Sets the entity's current fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @param ticks Current ticks remaining
     */
    public void setFireTicks(int ticks);

    /**
     * Sets if the entity has visual fire (it will always appear to be on fire).
     *
     * @param fire whether visual fire is enabled
     */
    void setVisualFire(boolean fire);

    /**
     * Gets if the entity has visual fire (it will always appear to be on fire).
     *
     * @return whether visual fire is enabled
     */
    boolean isVisualFire();

    /**
     * Returns the entity's current freeze ticks (amount of ticks the entity has
     * been in powdered snow).
     *
     * @return int freeze ticks
     */
    int getFreezeTicks();

    /**
     * Returns the entity's maximum freeze ticks (amount of ticks before it will
     * be fully frozen)
     *
     * @return int max freeze ticks
     */
    int getMaxFreezeTicks();

    /**
     * Sets the entity's current freeze ticks (amount of ticks the entity has
     * been in powdered snow).
     *
     * @param ticks Current ticks
     */
    void setFreezeTicks(int ticks);

    /**
     * Gets if the entity is fully frozen (it has been in powdered snow for max
     * freeze ticks).
     *
     * @return freeze status
     */
    boolean isFrozen();

    /**
     * Mark the entity's removal.
     *
     * @throws UnsupportedOperationException if you try to remove a {@link Player} use {@link Player#kickPlayer(String)} in this case instead
     */
    public void remove();

    /**
     * Returns true if this entity has been marked for removal.
     *
     * @return True if it is dead.
     */
    public boolean isDead();

    /**
     * Returns false if the entity has died, been despawned for some other
     * reason, or has not been added to the world.
     *
     * @return True if valid.
     */
    public boolean isValid();

    /**
     * Gets the {@link Server} that contains this Entity
     *
     * @return Server instance running this Entity
     */
    @Override
    @NotNull
    public Server getServer();

    /**
     * Returns true if the entity gets persisted.
     * <p>
     * By default all entities are persistent. An entity will also not get
     * persisted, if it is riding an entity that is not persistent.
     * <p>
     * The persistent flag on players controls whether or not to save their
     * playerdata file when they quit. If a player is directly or indirectly
     * riding a non-persistent entity, the vehicle at the root and all its
     * passengers won't get persisted.
     * <p>
     * <b>This should not be confused with
     * {@link LivingEntity#setRemoveWhenFarAway(boolean)} which controls
     * despawning of living entities. </b>
     *
     * @return true if this entity is persistent
     */
    public boolean isPersistent();

    /**
     * Sets whether or not the entity gets persisted.
     *
     * @param persistent the persistence status
     * @see #isPersistent()
     */
    public void setPersistent(boolean persistent);

    /**
     * Gets the primary passenger of a vehicle. For vehicles that could have
     * multiple passengers, this will only return the primary passenger.
     *
     * @return an entity
     * @deprecated entities may have multiple passengers, use
     * {@link #getPassengers()}
     */
    @Deprecated
    @Nullable
    public Entity getPassenger();

    /**
     * Set the passenger of a vehicle.
     *
     * @param passenger The new passenger.
     * @return false if it could not be done for whatever reason
     * @deprecated entities may have multiple passengers, use
     * {@link #addPassenger(org.bukkit.entity.Entity)}
     */
    @Deprecated
    public boolean setPassenger(@NotNull Entity passenger);

    /**
     * Gets a list of passengers of this vehicle.
     * <p>
     * The returned list will not be directly linked to the entity's current
     * passengers, and no guarantees are made as to its mutability.
     *
     * @return list of entities corresponding to current passengers.
     */
    @NotNull
    public List<Entity> getPassengers();

    /**
     * Add a passenger to the vehicle.
     *
     * @param passenger The passenger to add
     * @return false if it could not be done for whatever reason
     */
    public boolean addPassenger(@NotNull Entity passenger);

    /**
     * Remove a passenger from the vehicle.
     *
     * @param passenger The passenger to remove
     * @return false if it could not be done for whatever reason
     */
    public boolean removePassenger(@NotNull Entity passenger);

    /**
     * Check if a vehicle has passengers.
     *
     * @return True if the vehicle has no passengers.
     */
    public boolean isEmpty();

    /**
     * Eject any passenger.
     *
     * @return True if there was a passenger.
     */
    public boolean eject();

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
     * @deprecated method is for internal use only and will be removed
     */
    @Deprecated(forRemoval = true)
    public void setLastDamageCause(@Nullable EntityDamageEvent event);

    /**
     * Retrieve the last {@link EntityDamageEvent} inflicted on this entity.
     * This event may have been cancelled.
     *
     * @return the last known {@link EntityDamageEvent} or null if hitherto
     *     unharmed
     */
    @Nullable
    public EntityDamageEvent getLastDamageCause();

    /**
     * Returns a unique and persistent id for this entity
     *
     * @return unique id
     */
    @NotNull
    public UUID getUniqueId();

    /**
     * Gets the amount of ticks this entity has lived for.
     * <p>
     * This is the equivalent to "age" in entities.
     *
     * @return Age of entity
     */
    public int getTicksLived();

    /**
     * Sets the amount of ticks this entity has lived for.
     * <p>
     * This is the equivalent to "age" in entities. May not be less than one
     * tick.
     *
     * @param value Age of entity
     */
    public void setTicksLived(int value);

    /**
     * Performs the specified {@link EntityEffect} for this entity.
     * <p>
     * This will be viewable to all players near the entity.
     * <p>
     * If the effect is not applicable to this class of entity, it will not play.
     *
     * @param type Effect to play.
     */
    public void playEffect(@NotNull EntityEffect type);

    /**
     * Get the type of the entity.
     *
     * @return The entity type.
     */
    @NotNull
    public EntityType getType();

    /**
     * Get the {@link Sound} this entity makes while swimming.
     *
     * @return the swimming sound
     */
    @NotNull
    public Sound getSwimSound();

    /**
     * Get the {@link Sound} this entity makes when splashing in water. For most
     * entities, this is just {@link Sound#ENTITY_GENERIC_SPLASH}.
     *
     * @return the splash sound
     */
    @NotNull
    public Sound getSwimSplashSound();

    /**
     * Get the {@link Sound} this entity makes when splashing in water at high
     * speeds. For most entities, this is just {@link Sound#ENTITY_GENERIC_SPLASH}.
     *
     * @return the splash sound
     */
    @NotNull
    public Sound getSwimHighSpeedSplashSound();

    /**
     * Returns whether this entity is inside a vehicle.
     *
     * @return True if the entity is in a vehicle.
     */
    public boolean isInsideVehicle();

    /**
     * Leave the current vehicle. If the entity is currently in a vehicle (and
     * is removed from it), true will be returned, otherwise false will be
     * returned.
     *
     * @return True if the entity was in a vehicle.
     */
    public boolean leaveVehicle();

    /**
     * Get the vehicle that this entity is inside. If there is no vehicle,
     * null will be returned.
     *
     * @return The current vehicle.
     */
    @Nullable
    public Entity getVehicle();

    /**
     * Sets whether or not to display the mob's custom name client side. The
     * name will be displayed above the mob similarly to a player.
     * <p>
     * This value has no effect on players, they will always display their
     * name.
     *
     * @param flag custom name or not
     */
    public void setCustomNameVisible(boolean flag);

    /**
     * Gets whether or not the mob's custom name is displayed client side.
     * <p>
     * This value has no effect on players, they will always display their
     * name.
     *
     * @return if the custom name is displayed
     */
    public boolean isCustomNameVisible();

    /**
     * Sets whether or not this entity is visible by default.
     *
     * If this entity is not visible by default, then
     * {@link Player#showEntity(org.bukkit.plugin.Plugin, org.bukkit.entity.Entity)}
     * will need to be called before the entity is visible to a given player.
     *
     * @param visible default visibility status
     */
    public void setVisibleByDefault(boolean visible);

    /**
     * Gets whether or not this entity is visible by default.
     *
     * If this entity is not visible by default, then
     * {@link Player#showEntity(org.bukkit.plugin.Plugin, org.bukkit.entity.Entity)}
     * will need to be called before the entity is visible to a given player.
     *
     * @return default visibility status
     */
    public boolean isVisibleByDefault();

    /**
     * Get all players that are currently tracking this entity.
     * <p>
     * 'Tracking' means that this entity has been sent to the player and that
     * they are receiving updates on its state. Note that the client's {@code
     * 'Entity Distance'} setting does not affect the range at which entities
     * are tracked.
     *
     * @return the players tracking this entity, or an empty set if none
     */
    @NotNull
    Set<Player> getTrackedBy();

    /**
     * Sets whether the entity has a team colored (default: white) glow.
     *
     * <b>nb: this refers to the 'Glowing' entity property, not whether a
     * glowing potion effect is applied</b>
     *
     * @param flag if the entity is glowing
     */
    void setGlowing(boolean flag);

    /**
     * Gets whether the entity is glowing or not.
     *
     * <b>nb: this refers to the 'Glowing' entity property, not whether a
     * glowing potion effect is applied</b>
     *
     * @return whether the entity is glowing
     */
    boolean isGlowing();

    /**
     * Sets whether the entity is invulnerable or not.
     * <p>
     * When an entity is invulnerable it can only be damaged by players in
     * creative mode.
     *
     * @param flag if the entity is invulnerable
     */
    public void setInvulnerable(boolean flag);

    /**
     * Gets whether the entity is invulnerable or not.
     *
     * @return whether the entity is
     */
    public boolean isInvulnerable();

    /**
     * Gets whether the entity is silent or not.
     *
     * @return whether the entity is silent.
     */
    public boolean isSilent();

    /**
     * Sets whether the entity is silent or not.
     * <p>
     * When an entity is silent it will not produce any sound.
     *
     * @param flag if the entity is silent
     */
    public void setSilent(boolean flag);

    /**
     * Returns whether gravity applies to this entity.
     *
     * @return whether gravity applies
     */
    boolean hasGravity();

    /**
     * Sets whether gravity applies to this entity.
     *
     * @param gravity whether gravity should apply
     */
    void setGravity(boolean gravity);

    /**
     * Gets the period of time (in ticks) before this entity can use a portal.
     *
     * @return portal cooldown ticks
     */
    int getPortalCooldown();

    /**
     * Sets the period of time (in ticks) before this entity can use a portal.
     *
     * @param cooldown portal cooldown ticks
     */
    void setPortalCooldown(int cooldown);

    /**
     * Returns a set of tags for this entity.
     * <br>
     * Entities can have no more than 1024 tags.
     *
     * @return a set of tags for this entity
     */
    @NotNull
    Set<String> getScoreboardTags();

    /**
     * Add a tag to this entity.
     * <br>
     * Entities can have no more than 1024 tags.
     *
     * @param tag the tag to add
     * @return true if the tag was successfully added
     */
    boolean addScoreboardTag(@NotNull String tag);

    /**
     * Removes a given tag from this entity.
     *
     * @param tag the tag to remove
     * @return true if the tag was successfully removed
     */
    boolean removeScoreboardTag(@NotNull String tag);

    /**
     * Returns the reaction of the entity when moved by a piston.
     *
     * @return reaction
     */
    @NotNull
    PistonMoveReaction getPistonMoveReaction();

    /**
     * Get the closest cardinal {@link BlockFace} direction an entity is
     * currently facing.
     * <br>
     * This will not return any non-cardinal directions such as
     * {@link BlockFace#UP} or {@link BlockFace#DOWN}.
     * <br>
     * {@link Hanging} entities will override this call and thus their behavior
     * may be different.
     *
     * @return the entity's current cardinal facing.
     * @see Hanging
     * @see Directional#getFacing()
     */
    @NotNull
    BlockFace getFacing();

    /**
     * Gets the entity's current pose.
     *
     * <b>Note that the pose is only updated at the end of a tick, so may be
     * inconsistent with other methods. eg {@link Player#isSneaking()} being
     * true does not imply the current pose will be {@link Pose#SNEAKING}</b>
     *
     * @return current pose
     */
    @NotNull
    Pose getPose();

    /**
     * Get the category of spawn to which this entity belongs.
     *
     * @return the entity´s category spawn
     */
    @NotNull
    SpawnCategory getSpawnCategory();

    /**
     * Checks if this entity has been spawned in a world. <br>
     * Entities not spawned in a world will not tick, be sent to players, or be
     * saved to the server files.
     *
     * @return whether the entity has been spawned in a world
     */
    boolean isInWorld();

    /**
     * Get this entity as an NBT string.
     * <p>
     * This string should not be relied upon as a serializable value.
     *
     * @return the NBT string or null if one cannot be made
     */
    @Nullable
    @ApiStatus.Experimental
    String getAsString();

    /**
     * Crates an {@link EntitySnapshot} representing the current state of this entity.
     *
     * @return a snapshot representing this entity or null if one cannot be made
     */
    @Nullable
    @ApiStatus.Experimental
    EntitySnapshot createSnapshot();

    /**
     * Creates a copy of this entity and all its data. Does not spawn the copy in
     * the world. <br>
     * <b>Note:</b> Players cannot be copied.
     *
     * @return a copy of this entity.
     */
    @NotNull
    @ApiStatus.Experimental
    Entity copy();

    /**
     * Creates a copy of this entity and all its data. Spawns the copy at the given location. <br>
     * <b>Note:</b> Players cannot be copied.
     * @param to the location to copy to
     * @return a copy of this entity.
     */
    @NotNull
    @ApiStatus.Experimental
    Entity copy(@NotNull Location to);
}
