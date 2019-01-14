package org.bukkit;

import java.io.File;
import org.bukkit.generator.ChunkGenerator;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/**
 * Represents a world, which may contain entities, chunks and blocks
 */
public interface World extends PluginMessageRecipient, Metadatable {

    /**
     * Gets the {@link Block} at the given coordinates
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @return Block at the given coordinates
     */
    public Block getBlockAt(int x, int y, int z);

    /**
     * Gets the {@link Block} at the given {@link Location}
     *
     * @param location Location of the block
     * @return Block at the given location
     */
    public Block getBlockAt(Location location);

    /**
     * Gets the y coordinate of the lowest block at this position such that the
     * block and all blocks above it are transparent for lighting purposes.
     *
     * @param x X-coordinate of the blocks
     * @param z Z-coordinate of the blocks
     * @return Y-coordinate of the described block
     */
    public int getHighestBlockYAt(int x, int z);

    /**
     * Gets the y coordinate of the lowest block at the given {@link Location}
     * such that the block and all blocks above it are transparent for lighting
     * purposes.
     *
     * @param location Location of the blocks
     * @return Y-coordinate of the highest non-air block
     */
    public int getHighestBlockYAt(Location location);

    /**
     * Gets the lowest block at the given coordinates such that the block and
     * all blocks above it are transparent for lighting purposes.
     *
     * @param x X-coordinate of the block
     * @param z Z-coordinate of the block
     * @return Highest non-empty block
     */
    public Block getHighestBlockAt(int x, int z);

    /**
     * Gets the lowest block at the given {@link Location} such that the block
     * and all blocks above it are transparent for lighting purposes.
     *
     * @param location Coordinates to get the highest block
     * @return Highest non-empty block
     */
    public Block getHighestBlockAt(Location location);

    /**
     * Gets the {@link Chunk} at the given coordinates
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return Chunk at the given coordinates
     */
    public Chunk getChunkAt(int x, int z);

    /**
     * Gets the {@link Chunk} at the given {@link Location}
     *
     * @param location Location of the chunk
     * @return Chunk at the given location
     */
    public Chunk getChunkAt(Location location);

    /**
     * Gets the {@link Chunk} that contains the given {@link Block}
     *
     * @param block Block to get the containing chunk from
     * @return The chunk that contains the given block
     */
    public Chunk getChunkAt(Block block);

    /**
     * Checks if the specified {@link Chunk} is loaded
     *
     * @param chunk The chunk to check
     * @return true if the chunk is loaded, otherwise false
     */
    public boolean isChunkLoaded(Chunk chunk);

    /**
     * Gets an array of all loaded {@link Chunk}s
     *
     * @return Chunk[] containing all loaded chunks
     */
    public Chunk[] getLoadedChunks();

    /**
     * Loads the specified {@link Chunk}
     *
     * @param chunk The chunk to load
     */
    public void loadChunk(Chunk chunk);

    /**
     * Checks if the {@link Chunk} at the specified coordinates is loaded
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return true if the chunk is loaded, otherwise false
     */
    public boolean isChunkLoaded(int x, int z);

    /**
     * Checks if the {@link Chunk} at the specified coordinates is generated
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return true if the chunk is generated, otherwise false
     */
    public boolean isChunkGenerated(int x, int z);

    /**
     * Checks if the {@link Chunk} at the specified coordinates is loaded and
     * in use by one or more players
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return true if the chunk is loaded and in use by one or more players,
     *     otherwise false
     */
    public boolean isChunkInUse(int x, int z);

    /**
     * Loads the {@link Chunk} at the specified coordinates
     * <p>
     * If the chunk does not exist, it will be generated.
     * <p>
     * This method is analogous to {@link #loadChunk(int, int, boolean)} where
     * generate is true.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     */
    public void loadChunk(int x, int z);

    /**
     * Loads the {@link Chunk} at the specified coordinates
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param generate Whether or not to generate a chunk if it doesn't
     *     already exist
     * @return true if the chunk has loaded successfully, otherwise false
     */
    public boolean loadChunk(int x, int z, boolean generate);

    /**
     * Safely unloads and saves the {@link Chunk} at the specified coordinates
     * <p>
     * This method is analogous to {@link #unloadChunk(int, int, boolean,
     * boolean)} where safe and saveis true
     *
     * @param chunk the chunk to unload
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    public boolean unloadChunk(Chunk chunk);

    /**
     * Safely unloads and saves the {@link Chunk} at the specified coordinates
     * <p>
     * This method is analogous to {@link #unloadChunk(int, int, boolean,
     * boolean)} where safe and saveis true
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    public boolean unloadChunk(int x, int z);

    /**
     * Safely unloads and optionally saves the {@link Chunk} at the specified
     * coordinates
     * <p>
     * This method is analogous to {@link #unloadChunk(int, int, boolean,
     * boolean)} where save is true
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param save Whether or not to save the chunk
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    public boolean unloadChunk(int x, int z, boolean save);

    /**
     * Unloads and optionally saves the {@link Chunk} at the specified
     * coordinates
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param save Controls whether the chunk is saved
     * @param safe Controls whether to unload the chunk when players are
     *     nearby
     * @return true if the chunk has unloaded successfully, otherwise false
     * @deprecated it is never safe to remove a chunk in use
     */
    @Deprecated
    public boolean unloadChunk(int x, int z, boolean save, boolean safe);

    /**
     * Safely queues the {@link Chunk} at the specified coordinates for
     * unloading
     * <p>
     * This method is analogous to {@link #unloadChunkRequest(int, int,
     * boolean)} where safe is true
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return true is the queue attempt was successful, otherwise false
     */
    public boolean unloadChunkRequest(int x, int z);

    /**
     * Queues the {@link Chunk} at the specified coordinates for unloading
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param safe Controls whether to queue the chunk when players are nearby
     * @return Whether the chunk was actually queued
     * @deprecated it is never safe to remove a chunk in use
     */
    @Deprecated
    public boolean unloadChunkRequest(int x, int z, boolean safe);

    /**
     * Regenerates the {@link Chunk} at the specified coordinates
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return Whether the chunk was actually regenerated
     *
     * @deprecated regenerating a single chunk is not likely to produce the same
     * chunk as before as terrain decoration may be spread across chunks. Use of
     * this method should be avoided as it is known to produce buggy results.
     */
    @Deprecated
    public boolean regenerateChunk(int x, int z);

    /**
     * Resends the {@link Chunk} to all clients
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return Whether the chunk was actually refreshed
     * 
     * @deprecated This method is not guaranteed to work suitably across all client implementations.
     */
    @Deprecated
    public boolean refreshChunk(int x, int z);

    /**
     * Gets whether the chunk at the specified chunk coordinates is force
     * loaded.
     * <p>
     * A force loaded chunk will not be unloaded due to lack of player activity.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return force load status
     */
    public boolean isChunkForceLoaded(int x, int z);

    /**
     * Sets whether the chunk at the specified chunk coordinates is force
     * loaded.
     * <p>
     * A force loaded chunk will not be unloaded due to lack of player activity.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param forced
     */
    public void setChunkForceLoaded(int x, int z, boolean forced);

    /**
     * Returns all force loaded chunks in this world.
     * <p>
     * A force loaded chunk will not be unloaded due to lack of player activity.
     *
     * @return unmodifiable collection of force loaded chunks
     */
    public Collection<Chunk> getForceLoadedChunks();

    /**
     * Drops an item at the specified {@link Location}
     *
     * @param location Location to drop the item
     * @param item ItemStack to drop
     * @return ItemDrop entity created as a result of this method
     */
    public Item dropItem(Location location, ItemStack item);

    /**
     * Drops an item at the specified {@link Location} with a random offset
     *
     * @param location Location to drop the item
     * @param item ItemStack to drop
     * @return ItemDrop entity created as a result of this method
     */
    public Item dropItemNaturally(Location location, ItemStack item);

    /**
     * Creates an {@link Arrow} entity at the given {@link Location}
     *
     * @param location Location to spawn the arrow
     * @param direction Direction to shoot the arrow in
     * @param speed Speed of the arrow. A recommend speed is 0.6
     * @param spread Spread of the arrow. A recommend spread is 12
     * @return Arrow entity spawned as a result of this method
     */
    public Arrow spawnArrow(Location location, Vector direction, float speed, float spread);

    /**
     * Creates an arrow entity of the given class at the given {@link Location}
     *
     * @param <T> type of arrow to spawn
     * @param location Location to spawn the arrow
     * @param direction Direction to shoot the arrow in
     * @param speed Speed of the arrow. A recommend speed is 0.6
     * @param spread Spread of the arrow. A recommend spread is 12
     * @param clazz the Entity class for the arrow
     * {@link org.bukkit.entity.SpectralArrow},{@link org.bukkit.entity.Arrow},{@link org.bukkit.entity.TippedArrow}
     * @return Arrow entity spawned as a result of this method
     */
    public <T extends Arrow> T spawnArrow(Location location, Vector direction, float speed, float spread, Class<T> clazz);

    /**
     * Creates a tree at the given {@link Location}
     *
     * @param location Location to spawn the tree
     * @param type Type of the tree to create
     * @return true if the tree was created successfully, otherwise false
     */
    public boolean generateTree(Location location, TreeType type);

    /**
     * Creates a tree at the given {@link Location}
     *
     * @param loc Location to spawn the tree
     * @param type Type of the tree to create
     * @param delegate A class to call for each block changed as a result of
     *     this method
     * @return true if the tree was created successfully, otherwise false
     */
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate);

    /**
     * Creates a entity at the given {@link Location}
     *
     * @param loc The location to spawn the entity
     * @param type The entity to spawn
     * @return Resulting Entity of this method, or null if it was unsuccessful
     */
    public Entity spawnEntity(Location loc, EntityType type);

    /**
     * Strikes lightning at the given {@link Location}
     *
     * @param loc The location to strike lightning
     * @return The lightning entity.
     */
    public LightningStrike strikeLightning(Location loc);

    /**
     * Strikes lightning at the given {@link Location} without doing damage
     *
     * @param loc The location to strike lightning
     * @return The lightning entity.
     */
    public LightningStrike strikeLightningEffect(Location loc);

    /**
     * Get a list of all entities in this World
     *
     * @return A List of all Entities currently residing in this world
     */
    public List<Entity> getEntities();

    /**
     * Get a list of all living entities in this World
     *
     * @return A List of all LivingEntities currently residing in this world
     */
    public List<LivingEntity> getLivingEntities();

    /**
     * Get a collection of all entities in this World matching the given
     * class/interface
     *
     * @param <T> an entity subclass
     * @param classes The classes representing the types of entity to match
     * @return A List of all Entities currently residing in this world that
     *     match the given class/interface
     */
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes);

    /**
     * Get a collection of all entities in this World matching the given
     * class/interface
     * 
     * @param <T> an entity subclass
     * @param cls The class representing the type of entity to match
     * @return A List of all Entities currently residing in this world that
     *     match the given class/interface
     */
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls);

    /**
     * Get a collection of all entities in this World matching any of the
     * given classes/interfaces
     *
     * @param classes The classes representing the types of entity to match
     * @return A List of all Entities currently residing in this world that
     *     match one or more of the given classes/interfaces
     */
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes);

    /**
     * Get a list of all players in this World
     *
     * @return A list of all Players currently residing in this world
     */
    public List<Player> getPlayers();

    /**
     * Returns a list of entities within a bounding box centered around a
     * Location.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the size of the
     * search bounding box.
     *
     * @param location The center of the bounding box
     * @param x 1/2 the size of the box along x axis
     * @param y 1/2 the size of the box along y axis
     * @param z 1/2 the size of the box along z axis
     * @return the collection of entities near location. This will always be a
     *      non-null collection.
     */
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z);

    /**
     * Returns a list of entities within a bounding box centered around a
     * Location.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the size of the
     * search bounding box.
     *
     * @param location The center of the bounding box
     * @param x 1/2 the size of the box along x axis
     * @param y 1/2 the size of the box along y axis
     * @param z 1/2 the size of the box along z axis
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @return the collection of entities near location. This will always be a
     *     non-null collection.
     */
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z, Predicate<Entity> filter);

    /**
     * Returns a list of entities within the given bounding box.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the size of the
     * search bounding box.
     *
     * @param boundingBox the bounding box
     * @return the collection of entities within the bounding box, will always
     *     be a non-null collection
     */
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox);

    /**
     * Returns a list of entities within the given bounding box.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the size of the
     * search bounding box.
     *
     * @param boundingBox the bounding box
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @return the collection of entities within the bounding box, will always
     *     be a non-null collection
     */
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter);

    /**
     * Performs a ray trace that checks for entity collisions.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the maximum
     * distance.
     *
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @return the closest ray trace hit result, or <code>null</code> if there
     *     is no hit
     * @see #rayTraceEntities(Location, Vector, double, double, Predicate)
     */
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance);

    /**
     * Performs a ray trace that checks for entity collisions.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the maximum
     * distance.
     *
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param raySize entity bounding boxes will be uniformly expanded (or
     *     shrinked) by this value before doing collision checks
     * @return the closest ray trace hit result, or <code>null</code> if there
     *     is no hit
     * @see #rayTraceEntities(Location, Vector, double, double, Predicate)
     */
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize);

    /**
     * Performs a ray trace that checks for entity collisions.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the maximum
     * distance.
     *
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @return the closest ray trace hit result, or <code>null</code> if there
     *     is no hit
     * @see #rayTraceEntities(Location, Vector, double, double, Predicate)
     */
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, Predicate<Entity> filter);

    /**
     * Performs a ray trace that checks for entity collisions.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the maximum
     * distance.
     *
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param raySize entity bounding boxes will be uniformly expanded (or
     *     shrinked) by this value before doing collision checks
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @return the closest ray trace hit result, or <code>null</code> if there
     *     is no hit
     */
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize, Predicate<Entity> filter);

    /**
     * Performs a ray trace that checks for block collisions using the blocks'
     * precise collision shapes.
     * <p>
     * This takes collisions with passable blocks into account, but ignores
     * fluids.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param start the start location
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @return the ray trace hit result, or <code>null</code> if there is no hit
     * @see #rayTraceBlocks(Location, Vector, double, FluidCollisionMode, boolean)
     */
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance);

    /**
     * Performs a ray trace that checks for block collisions using the blocks'
     * precise collision shapes.
     * <p>
     * This takes collisions with passable blocks into account.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param start the start location
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param fluidCollisionMode the fluid collision mode
     * @return the ray trace hit result, or <code>null</code> if there is no hit
     * @see #rayTraceBlocks(Location, Vector, double, FluidCollisionMode, boolean)
     */
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode);

    /**
     * Performs a ray trace that checks for block collisions using the blocks'
     * precise collision shapes.
     * <p>
     * If collisions with passable blocks are ignored, fluid collisions are
     * ignored as well regardless of the fluid collision mode.
     * <p>
     * Portal blocks are only considered passable if the ray starts within
     * them. Apart from that collisions with portal blocks will be considered
     * even if collisions with passable blocks are otherwise ignored.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param start the start location
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param fluidCollisionMode the fluid collision mode
     * @param ignorePassableBlocks whether to ignore passable but collidable
     *     blocks (ex. tall grass, signs, fluids, ..)
     * @return the ray trace hit result, or <code>null</code> if there is no hit
     */
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks);

    /**
     * Performs a ray trace that checks for both block and entity collisions.
     * <p>
     * Block collisions use the blocks' precise collision shapes. The
     * <code>raySize</code> parameter is only taken into account for entity
     * collision checks.
     * <p>
     * If collisions with passable blocks are ignored, fluid collisions are
     * ignored as well regardless of the fluid collision mode.
     * <p>
     * Portal blocks are only considered passable if the ray starts within them.
     * Apart from that collisions with portal blocks will be considered even if
     * collisions with passable blocks are otherwise ignored.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param start the start location
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param fluidCollisionMode the fluid collision mode
     * @param ignorePassableBlocks whether to ignore passable but collidable
     *     blocks (ex. tall grass, signs, fluids, ..)
     * @param raySize entity bounding boxes will be uniformly expanded (or
     *     shrinked) by this value before doing collision checks
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @return the closest ray trace hit result with either a block or an
     *     entity, or <code>null</code> if there is no hit
     */
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, Predicate<Entity> filter);

    /**
     * Gets the unique name of this world
     *
     * @return Name of this world
     */
    public String getName();

    /**
     * Gets the Unique ID of this world
     *
     * @return Unique ID of this world.
     */
    public UUID getUID();

    /**
     * Gets the default spawn {@link Location} of this world
     *
     * @return The spawn location of this world
     */
    public Location getSpawnLocation();

    /**
     * Sets the spawn location of the world.
     * <br>
     * The location provided must be equal to this world.
     *
     * @param location The {@link Location} to set the spawn for this world at.
     * @return True if it was successfully set.
     */
    public boolean setSpawnLocation(Location location);

    /**
     * Sets the spawn location of the world
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return True if it was successfully set.
     */
    public boolean setSpawnLocation(int x, int y, int z);

    /**
     * Gets the relative in-game time of this world.
     * <p>
     * The relative time is analogous to hours * 1000
     *
     * @return The current relative time
     * @see #getFullTime() Returns an absolute time of this world
     */
    public long getTime();

    /**
     * Sets the relative in-game time on the server.
     * <p>
     * The relative time is analogous to hours * 1000
     * <p>
     * Note that setting the relative time below the current relative time
     * will actually move the clock forward a day. If you require to rewind
     * time, please see {@link #setFullTime(long)}
     *
     * @param time The new relative time to set the in-game time to (in
     *     hours*1000)
     * @see #setFullTime(long) Sets the absolute time of this world
     */
    public void setTime(long time);

    /**
     * Gets the full in-game time on this world
     *
     * @return The current absolute time
     * @see #getTime() Returns a relative time of this world
     */
    public long getFullTime();

    /**
     * Sets the in-game time on the server
     * <p>
     * Note that this sets the full time of the world, which may cause adverse
     * effects such as breaking redstone clocks and any scheduled events
     *
     * @param time The new absolute time to set this world to
     * @see #setTime(long) Sets the relative time of this world
     */
    public void setFullTime(long time);

    /**
     * Returns whether the world has an ongoing storm.
     *
     * @return Whether there is an ongoing storm
     */
    public boolean hasStorm();

    /**
     * Set whether there is a storm. A duration will be set for the new
     * current conditions.
     *
     * @param hasStorm Whether there is rain and snow
     */
    public void setStorm(boolean hasStorm);

    /**
     * Get the remaining time in ticks of the current conditions.
     *
     * @return Time in ticks
     */
    public int getWeatherDuration();

    /**
     * Set the remaining time in ticks of the current conditions.
     *
     * @param duration Time in ticks
     */
    public void setWeatherDuration(int duration);

    /**
     * Returns whether there is thunder.
     *
     * @return Whether there is thunder
     */
    public boolean isThundering();

    /**
     * Set whether it is thundering.
     *
     * @param thundering Whether it is thundering
     */
    public void setThundering(boolean thundering);

    /**
     * Get the thundering duration.
     *
     * @return Duration in ticks
     */
    public int getThunderDuration();

    /**
     * Set the thundering duration.
     *
     * @param duration Duration in ticks
     */
    public void setThunderDuration(int duration);

    /**
     * Creates explosion at given coordinates with given power
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param power The power of explosion, where 4F is TNT
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(double x, double y, double z, float power);

    /**
     * Creates explosion at given coordinates with given power and optionally
     * setting blocks on fire.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire);

    /**
     * Creates explosion at given coordinates with given power and optionally
     * setting blocks on fire or breaking blocks.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @param breakBlocks Whether or not to have blocks be destroyed
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks);

    /**
     * Creates explosion at given coordinates with given power
     *
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(Location loc, float power);

    /**
     * Creates explosion at given coordinates with given power and optionally
     * setting blocks on fire.
     *
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(Location loc, float power, boolean setFire);

    /**
     * Gets the {@link Environment} type of this world
     *
     * @return This worlds Environment type
     */
    public Environment getEnvironment();

    /**
     * Gets the Seed for this world.
     *
     * @return This worlds Seed
     */
    public long getSeed();

    /**
     * Gets the current PVP setting for this world.
     *
     * @return True if PVP is enabled
     */
    public boolean getPVP();

    /**
     * Sets the PVP setting for this world.
     *
     * @param pvp True/False whether PVP should be Enabled.
     */
    public void setPVP(boolean pvp);

    /**
     * Gets the chunk generator for this world
     *
     * @return ChunkGenerator associated with this world
     */
    public ChunkGenerator getGenerator();

    /**
     * Saves world to disk
     */
    public void save();

    /**
     * Gets a list of all applied {@link BlockPopulator}s for this World
     *
     * @return List containing any or none BlockPopulators
     */
    public List<BlockPopulator> getPopulators();

    /**
     * Spawn an entity of a specific class at the given {@link Location}
     *
     * @param location the {@link Location} to spawn the entity at
     * @param clazz the class of the {@link Entity} to spawn
     * @param <T> the class of the {@link Entity} to spawn
     * @return an instance of the spawned {@link Entity}
     * @throws IllegalArgumentException if either parameter is null or the
     *     {@link Entity} requested cannot be spawned
     */
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException;

    /**
     * Spawn an entity of a specific class at the given {@link Location}, with
     * the supplied function run before the entity is added to the world.
     * <br>
     * Note that when the function is run, the entity will not be actually in
     * the world. Any operation involving such as teleporting the entity is undefined
     * until after this function returns.
     *
     * @param location the {@link Location} to spawn the entity at
     * @param clazz the class of the {@link Entity} to spawn
     * @param function the function to be run before the entity is spawned.
     * @param <T> the class of the {@link Entity} to spawn
     * @return an instance of the spawned {@link Entity}
     * @throws IllegalArgumentException if either parameter is null or the
     *     {@link Entity} requested cannot be spawned
     */
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException;

    /**
     * Spawn a {@link FallingBlock} entity at the given {@link Location} of
     * the specified {@link Material}. The material dictates what is falling.
     * When the FallingBlock hits the ground, it will place that block.
     * <p>
     * The Material must be a block type, check with {@link Material#isBlock()
     * material.isBlock()}. The Material may not be air.
     *
     * @param location The {@link Location} to spawn the FallingBlock
     * @param data The block data
     * @return The spawned {@link FallingBlock} instance
     * @throws IllegalArgumentException if {@link Location} or {@link
     *     MaterialData} are null or {@link Material} of the {@link MaterialData} is not a block
     */
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException;

    /**
     * Spawn a {@link FallingBlock} entity at the given {@link Location} of
     * the specified {@link Material}. The material dictates what is falling.
     * When the FallingBlock hits the ground, it will place that block.
     * <p>
     * The Material must be a block type, check with {@link Material#isBlock()
     * material.isBlock()}. The Material may not be air.
     *
     * @param location The {@link Location} to spawn the FallingBlock
     * @param data The block data
     * @return The spawned {@link FallingBlock} instance
     * @throws IllegalArgumentException if {@link Location} or {@link
     *     BlockData} are null
     */
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException;

    /**
     * Spawn a {@link FallingBlock} entity at the given {@link Location} of the
     * specified {@link Material}. The material dictates what is falling.
     * When the FallingBlock hits the ground, it will place that block.
     * <p>
     * The Material must be a block type, check with {@link Material#isBlock()
     * material.isBlock()}. The Material may not be air.
     *
     * @param location The {@link Location} to spawn the FallingBlock
     * @param material The block {@link Material} type
     * @param data The block data
     * @return The spawned {@link FallingBlock} instance
     * @throws IllegalArgumentException if {@link Location} or {@link
     *     Material} are null or {@link Material} is not a block
     * @deprecated Magic value
     */
    @Deprecated
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data) throws IllegalArgumentException;

    /**
     * Plays an effect to all players within a default radius around a given
     * location.
     *
     * @param location the {@link Location} around which players must be to
     *     hear the sound
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     */
    public void playEffect(Location location, Effect effect, int data);

    /**
     * Plays an effect to all players within a given radius around a location.
     *
     * @param location the {@link Location} around which players must be to
     *     hear the effect
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     * @param radius the radius around the location
     */
    public void playEffect(Location location, Effect effect, int data, int radius);

    /**
     * Plays an effect to all players within a default radius around a given
     * location.
     *
     * @param <T> data dependant on the type of effect
     * @param location the {@link Location} around which players must be to
     *     hear the sound
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     */
    public <T> void playEffect(Location location, Effect effect, T data);

    /**
     * Plays an effect to all players within a given radius around a location.
     *
     * @param <T> data dependant on the type of effect
     * @param location the {@link Location} around which players must be to
     *     hear the effect
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     * @param radius the radius around the location
     */
    public <T> void playEffect(Location location, Effect effect, T data, int radius);

    /**
     * Get empty chunk snapshot (equivalent to all air blocks), optionally
     * including valid biome data. Used for representing an ungenerated chunk,
     * or for fetching only biome data without loading a chunk.
     *
     * @param x - chunk x coordinate
     * @param z - chunk z coordinate
     * @param includeBiome - if true, snapshot includes per-coordinate biome
     *     type
     * @param includeBiomeTemp - if true, snapshot includes per-coordinate
     *     raw biome temperature
     * @return The empty snapshot.
     */
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp);

    /**
     * Sets the spawn flags for this.
     *
     * @param allowMonsters - if true, monsters are allowed to spawn in this
     *     world.
     * @param allowAnimals - if true, animals are allowed to spawn in this
     *     world.
     */
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals);

    /**
     * Gets whether animals can spawn in this world.
     *
     * @return whether animals can spawn in this world.
     */
    public boolean getAllowAnimals();

    /**
     * Gets whether monsters can spawn in this world.
     *
     * @return whether monsters can spawn in this world.
     */
    public boolean getAllowMonsters();

    /**
     * Gets the biome for the given block coordinates.
     *
     * @param x X coordinate of the block
     * @param z Z coordinate of the block
     * @return Biome of the requested block
     */
    Biome getBiome(int x, int z);

    /**
     * Sets the biome for the given block coordinates
     *
     * @param x X coordinate of the block
     * @param z Z coordinate of the block
     * @param bio new Biome type for this block
     */
    void setBiome(int x, int z, Biome bio);

    /**
     * Gets the temperature for the given block coordinates.
     * <p>
     * It is safe to run this method when the block does not exist, it will
     * not create the block.
     * <p>
     * This method will return the raw temperature without adjusting for block
     * height effects.
     *
     * @param x X coordinate of the block
     * @param z Z coordinate of the block
     * @return Temperature of the requested block
     */
    public double getTemperature(int x, int z);

    /**
     * Gets the humidity for the given block coordinates.
     * <p>
     * It is safe to run this method when the block does not exist, it will
     * not create the block.
     *
     * @param x X coordinate of the block
     * @param z Z coordinate of the block
     * @return Humidity of the requested block
     */
    public double getHumidity(int x, int z);

    /**
     * Gets the maximum height of this world.
     * <p>
     * If the max height is 100, there are only blocks from y=0 to y=99.
     *
     * @return Maximum height of the world
     */
    public int getMaxHeight();

    /**
     * Gets the sea level for this world.
     * <p>
     * This is often half of {@link #getMaxHeight()}
     *
     * @return Sea level
     */
    public int getSeaLevel();

    /**
     * Gets whether the world's spawn area should be kept loaded into memory
     * or not.
     *
     * @return true if the world's spawn area will be kept loaded into memory.
     */
    public boolean getKeepSpawnInMemory();

    /**
     * Sets whether the world's spawn area should be kept loaded into memory
     * or not.
     *
     * @param keepLoaded if true then the world's spawn area will be kept
     *     loaded into memory.
     */
    public void setKeepSpawnInMemory(boolean keepLoaded);

    /**
     * Gets whether or not the world will automatically save
     *
     * @return true if the world will automatically save, otherwise false
     */
    public boolean isAutoSave();

    /**
     * Sets whether or not the world will automatically save
     *
     * @param value true if the world should automatically save, otherwise
     *     false
     */
    public void setAutoSave(boolean value);

    /**
     * Sets the Difficulty of the world.
     *
     * @param difficulty the new difficulty you want to set the world to
     */
    public void setDifficulty(Difficulty difficulty);

    /**
     * Gets the Difficulty of the world.
     *
     * @return The difficulty of the world.
     */
    public Difficulty getDifficulty();

    /**
     * Gets the folder of this world on disk.
     *
     * @return The folder of this world.
     */
    public File getWorldFolder();

    /**
     * Gets the type of this world.
     *
     * @return Type of this world.
     */
    public WorldType getWorldType();

    /**
     * Gets whether or not structures are being generated.
     *
     * @return True if structures are being generated.
     */
    public boolean canGenerateStructures();

    /**
     * Gets the world's ticks per animal spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn animals.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn animals in
     *     this world every tick.
     * <li>A value of 400 will mean the server will attempt to spawn animals
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, animal spawning will be disabled for this world. We
     * recommend using {@link #setSpawnFlags(boolean, boolean)} to control
     * this instead.
     * <p>
     * Minecraft default: 400.
     *
     * @return The world's ticks per animal spawns value
     */
    public long getTicksPerAnimalSpawns();

    /**
     * Sets the world's ticks per animal spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn animals.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn animals in
     *     this world every tick.
     * <li>A value of 400 will mean the server will attempt to spawn animals
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, animal spawning will be disabled for this world. We
     * recommend using {@link #setSpawnFlags(boolean, boolean)} to control
     * this instead.
     * <p>
     * Minecraft default: 400.
     *
     * @param ticksPerAnimalSpawns the ticks per animal spawns value you want
     *     to set the world to
     */
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns);

    /**
     * Gets the world's ticks per monster spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn monsters.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn monsters in
     *     this world every tick.
     * <li>A value of 400 will mean the server will attempt to spawn monsters
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, monsters spawning will be disabled for this world. We
     * recommend using {@link #setSpawnFlags(boolean, boolean)} to control
     * this instead.
     * <p>
     * Minecraft default: 1.
     *
     * @return The world's ticks per monster spawns value
     */
    public long getTicksPerMonsterSpawns();

    /**
     * Sets the world's ticks per monster spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn monsters.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn monsters in
     *     this world on every tick.
     * <li>A value of 400 will mean the server will attempt to spawn monsters
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, monsters spawning will be disabled for this world. We
     * recommend using {@link #setSpawnFlags(boolean, boolean)} to control
     * this instead.
     * <p>
     * Minecraft default: 1.
     *
     * @param ticksPerMonsterSpawns the ticks per monster spawns value you
     *     want to set the world to
     */
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns);

    /**
     * Gets limit for number of monsters that can spawn in a chunk in this
     * world
     *
     * @return The monster spawn limit
     */
    int getMonsterSpawnLimit();

    /**
     * Sets the limit for number of monsters that can spawn in a chunk in this
     * world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     * 
     * @param limit the new mob limit
     */
    void setMonsterSpawnLimit(int limit);

    /**
     * Gets the limit for number of animals that can spawn in a chunk in this
     * world
     *
     * @return The animal spawn limit
     */
    int getAnimalSpawnLimit();

    /**
     * Sets the limit for number of animals that can spawn in a chunk in this
     * world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     * 
     * @param limit the new mob limit
     */
    void setAnimalSpawnLimit(int limit);

    /**
     * Gets the limit for number of water animals that can spawn in a chunk in
     * this world
     *
     * @return The water animal spawn limit
     */
    int getWaterAnimalSpawnLimit();

    /**
     * Sets the limit for number of water animals that can spawn in a chunk in
     * this world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     * 
     * @param limit the new mob limit
     */
    void setWaterAnimalSpawnLimit(int limit);

    /**
     * Gets the limit for number of ambient mobs that can spawn in a chunk in
     * this world
     *
     * @return The ambient spawn limit
     */
    int getAmbientSpawnLimit();

    /**
     * Sets the limit for number of ambient mobs that can spawn in a chunk in
     * this world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     * 
     * @param limit the new mob limit
     */
    void setAmbientSpawnLimit(int limit);

    /**
     * Play a Sound at the provided Location in the World
     * <p>
     * This function will fail silently if Location or Sound are null.
     *
     * @param location The location to play the sound
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    void playSound(Location location, Sound sound, float volume, float pitch);

    /**
     * Play a Sound at the provided Location in the World.
     * <p>
     * This function will fail silently if Location or Sound are null. No
     * sound will be heard by the players if their clients do not have the
     * respective sound for the value passed.
     *
     * @param location the location to play the sound
     * @param sound the internal sound name to play
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    void playSound(Location location, String sound, float volume, float pitch);

    /**
     * Play a Sound at the provided Location in the World.
     * <p>
     * This function will fail silently if Location or Sound are null.
     *
     * @param location The location to play the sound
     * @param sound The sound to play
     * @param category the category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch);

    /**
     * Play a Sound at the provided Location in the World.
     * <p>
     * This function will fail silently if Location or Sound are null. No sound
     * will be heard by the players if their clients do not have the respective
     * sound for the value passed.
     *
     * @param location the location to play the sound
     * @param sound the internal sound name to play
     * @param category the category of the sound
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    void playSound(Location location, String sound, SoundCategory category, float volume, float pitch);

    /**
     * Get an array containing the names of all the {@link GameRule}s.
     *
     * @return An array of {@link GameRule} names.
     */
    public String[] getGameRules();

    /**
     * Gets the current state of the specified rule
     * <p>
     * Will return null if rule passed is null
     *
     * @param rule Rule to look up value of
     * @return String value of rule
     * @deprecated use {@link #getGameRuleValue(GameRule)} instead
     */
    @Deprecated
    public String getGameRuleValue(String rule);

    /**
     * Set the specified gamerule to specified value.
     * <p>
     * The rule may attempt to validate the value passed, will return true if
     * value was set.
     * <p>
     * If rule is null, the function will return false.
     *
     * @param rule Rule to set
     * @param value Value to set rule to
     * @return True if rule was set
     * @deprecated use {@link #setGameRule(GameRule, Object)} instead.
     */
    @Deprecated
    public boolean setGameRuleValue(String rule, String value);

    /**
     * Checks if string is a valid game rule
     *
     * @param rule Rule to check
     * @return True if rule exists
     */
    public boolean isGameRule(String rule);

    /**
     * Get the current value for a given {@link GameRule}.
     *
     * @param rule the GameRule to check
     * @param <T> the GameRule's type
     * @return the current value
     */
    public <T> T getGameRuleValue(GameRule<T> rule);

    /**
     * Get the default value for a given {@link GameRule}. This value is not
     * guaranteed to match the current value.
     *
     * @param rule the rule to return a default value for
     * @param <T> the type of GameRule
     * @return the default value
     */
    public <T> T getGameRuleDefault(GameRule<T> rule);

    /**
     * Set the given {@link GameRule}'s new value.
     *
     * @param rule the GameRule to update
     * @param newValue the new value
     * @param <T> the value type of the GameRule
     * @return true if the value was successfully set
     */
    public <T> boolean setGameRule(GameRule<T> rule, T newValue);

    /**
     * Gets the world border for this world.
     *
     * @return The world border for this world.
     */
    public WorldBorder getWorldBorder();

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     */
    public void spawnParticle(Particle particle, Location location, int count);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     */
    public void spawnParticle(Particle particle, double x, double y, double z, int count);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, Location location, int count, T data);


    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     */
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     */
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     */
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     */
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     * @param force whether to send the particle to players within an extended
     *              range and encourage their client to render it regardless of
     *              settings
     */
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force);

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     * @param force whether to send the particle to players within an extended
     *              range and encourage their client to render it regardless of
     *              settings
     */
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force);

    /**
     * Find the closest nearby structure of a given {@link StructureType}.
     * Finding unexplored structures can, and will, block if the world is
     * looking in chunks that gave not generated yet. This can lead to the world
     * temporarily freezing while locating an unexplored structure.
     * <p>
     * The {@code radius} is not a rigid square radius. Each structure may alter
     * how many chunks to check for each iteration. Do not assume that only a
     * radius x radius chunk area will be checked. For example,
     * {@link StructureType#WOODLAND_MANSION} can potentially check up to 20,000
     * blocks away (or more) regardless of the radius used.
     * <p>
     * This will <i>not</i> load or generate chunks. This can also lead to
     * instances where the server can hang if you are only looking for
     * unexplored structures. This is because it will keep looking further and
     * further out in order to find the structure.
     *
     * @param origin where to start looking for a structure
     * @param structureType the type of structure to find
     * @param radius the radius, in chunks, around which to search
     * @param findUnexplored true to only find unexplored structures
     * @return the closest {@link Location}, or null if no structure of the
     * specified type exists.
     */
    public Location locateNearestStructure(Location origin, StructureType structureType, int radius, boolean findUnexplored);

    /**
     * Represents various map environment types that a world may be
     */
    public enum Environment {

        /**
         * Represents the "normal"/"surface world" map
         */
        NORMAL(0),
        /**
         * Represents a nether based map ("hell")
         */
        NETHER(-1),
        /**
         * Represents the "end" map
         */
        THE_END(1);

        private final int id;
        private static final Map<Integer, Environment> lookup = new HashMap<Integer, Environment>();

        private Environment(int id) {
            this.id = id;
        }

        /**
         * Gets the dimension ID of this environment
         *
         * @return dimension ID
         * @deprecated Magic value
         */
        @Deprecated
        public int getId() {
            return id;
        }

        /**
         * Get an environment by ID
         *
         * @param id The ID of the environment
         * @return The environment
         * @deprecated Magic value
         */
        @Deprecated
        public static Environment getEnvironment(int id) {
            return lookup.get(id);
        }

        static {
            for (Environment env : values()) {
                lookup.put(env.getId(), env);
            }
        }
    }
}
