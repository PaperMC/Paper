package org.bukkit;

import io.papermc.paper.raytracing.PositionedRayTraceConfigurationBuilder;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.util.BiomeSearchResult;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.StructureSearchResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a world, which may contain entities, chunks and blocks
 */
public interface World extends RegionAccessor, WorldInfo, PluginMessageRecipient, Metadatable, PersistentDataHolder, Keyed, net.kyori.adventure.audience.ForwardingAudience { // Paper

    // Paper start - void damage configuration
    /**
     * Checks if void damage is enabled on this world.
     *
     * @return true if enabled
     */
    boolean isVoidDamageEnabled();

    /**
     * Sets whether void damage is enabled on this world.
     *
     * @param enabled true to enable void damage
     */
    void setVoidDamageEnabled(boolean enabled);

    /**
     * Gets the damage applied to the entities when they are in the void in this world.
     * Check {@link #isVoidDamageEnabled()} to see if void damage is enabled.
     *
     * @return amount of damage to apply
     * @see #isVoidDamageEnabled()
     */
    float getVoidDamageAmount();

    /**
     * Sets the damage applied to the entities when they are in the void in this world.
     * Check {@link #isVoidDamageEnabled()} to see if void damage is enabled.
     *
     * @param voidDamageAmount amount of damage to apply
     */
    void setVoidDamageAmount(float voidDamageAmount);

    /**
     * Gets the offset applied to {@link #getMinHeight()} to determine the height at which void damage starts to apply.
     *
     * @return offset from min build height
     * @see #isVoidDamageEnabled()
     */
    double getVoidDamageMinBuildHeightOffset();

    /**
     * Sets the offset applied to {@link #getMinHeight()} to determine the height at which void damage starts to apply.
     *
     * @param minBuildHeightOffset offset from min build height
     */
    void setVoidDamageMinBuildHeightOffset(double minBuildHeightOffset);
    // Paper end - void damage configuration

    // Paper start
    /**
     * @return The amount of entities in this world
     */
    int getEntityCount();

    /**
     * @return The amount of block entities in this world
     */
    int getTileEntityCount();

    /**
     * @return The amount of tickable block entities in this world
     */
    int getTickableTileEntityCount();

    /**
     * @return The amount of chunks in this world
     */
    int getChunkCount();

    /**
     * @return The amount of players in this world
     */
    int getPlayerCount();
    // Paper end
    // Paper start - structure check API
    /**
     * Check if the naturally-generated structure exists at the position.
     * <p>
     * Note that if the position is not loaded, this may cause chunk loads/generation
     * to check if a structure is at that position. Use {@link #isPositionLoaded(io.papermc.paper.math.Position)}
     * to check if a position is loaded
     *
     * @param position the position to check at
     * @param structure the structure to check for
     * @return true if that structure exists at the position
     */
    boolean hasStructureAt(io.papermc.paper.math.@NotNull Position position, @NotNull Structure structure);

    /**
     * Checks if this position is loaded.
     *
     * @param position position to check
     * @return true if loaded
     */
    default boolean isPositionLoaded(io.papermc.paper.math.@NotNull Position position) {
        return this.isChunkLoaded(position.blockX() >> 4, position.blockZ() >> 4);
    }
    // Paper end

    /**
     * Gets the {@link Block} at the given coordinates
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @return Block at the given coordinates
     */
    @NotNull
    public Block getBlockAt(int x, int y, int z);

    /**
     * Gets the {@link Block} at the given {@link Location}
     *
     * @param location Location of the block
     * @return Block at the given location
     */
    @NotNull
    default Block getBlockAt(@NotNull Location location) {
        return this.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    // Paper start
    /**
     * Gets the {@link Block} at the given block key
     *
     * @param key The block key. See {@link Block#getBlockKey()}
     * @return Block at the key
     * @see Block#getBlockKey(int, int, int)
     * @deprecated only encodes y block ranges from -512 to 511 and represents an already changed implementation detail
     */
    @NotNull
    @Deprecated(since = "1.18.1")
    public default Block getBlockAtKey(long key) {
        int x = Block.getBlockKeyX(key);
        int y = Block.getBlockKeyY(key);
        int z = Block.getBlockKeyZ(key);
        return getBlockAt(x, y, z);
    }

    /**
     * Gets the {@link Location} at the given block key
     *
     * @param key The block key. See {@link Location#toBlockKey()}
     * @return Location at the key
     * @see Block#getBlockKey(int, int, int)
     */
    @NotNull
    @Deprecated(since = "1.18.1")
    public default Location getLocationAtKey(long key) {
        int x = Block.getBlockKeyX(key);
        int y = Block.getBlockKeyY(key);
        int z = Block.getBlockKeyZ(key);
        return new Location(this, x, y, z);
    }
    // Paper end

    /**
     * Gets the highest non-empty (impassable) block at the given coordinates.
     *
     * @param x X-coordinate of the block
     * @param z Z-coordinate of the block
     * @return Highest non-empty block
     */
    @NotNull
    default Block getHighestBlockAt(int x, int z) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z), z);
    }

    /**
     * Gets the highest non-empty (impassable) block at the given coordinates.
     *
     * @param location Coordinates to get the highest block
     * @return Highest non-empty block
     */
    @NotNull
    default Block getHighestBlockAt(@NotNull Location location) {
        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    /**
     * Gets the highest block corresponding to the {@link HeightMap} at the
     * given coordinates.
     *
     * @param x X-coordinate of the block
     * @param z Z-coordinate of the block
     * @param heightMap the heightMap that is used to determine the highest
     * point
     * @return Highest block corresponding to the {@link HeightMap}
     */
    @NotNull
    default Block getHighestBlockAt(int x, int z, @NotNull HeightMap heightMap) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z, heightMap), z);
    }

    /**
     * Gets the highest block corresponding to the {@link HeightMap} at the
     * given coordinates.
     *
     * @param location Coordinates to get the highest block
     * @param heightMap the heightMap that is used to determine the highest
     * point
     * @return Highest block corresponding to the {@link HeightMap}
     */
    @NotNull
    default Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    /**
     * Gets the {@link Chunk} at the given coordinates
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return Chunk at the given coordinates
     */
    @NotNull
    public Chunk getChunkAt(int x, int z);

    /**
     * Gets the {@link Chunk} at the given coordinates
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param generate Whether the chunk should be fully generated or not
     * @return Chunk at the given coordinates
     */
    @NotNull
    public Chunk getChunkAt(int x, int z, boolean generate);

    /**
     * Gets the {@link Chunk} at the given {@link Location}
     *
     * @param location Location of the chunk
     * @return Chunk at the given location
     */
    @NotNull
    default Chunk getChunkAt(@NotNull Location location) {
        return this.getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    /**
     * Gets the {@link Chunk} that contains the given {@link Block}
     *
     * @param block Block to get the containing chunk from
     * @return The chunk that contains the given block
     */
    @NotNull
    public Chunk getChunkAt(@NotNull Block block);

    // Paper start - chunk long key API
    /**
     * Gets the chunk at the specified chunk key, which is the X and Z packed into a long.
     * <p>
     * See {@link Chunk#getChunkKey()} for easy access to the key, or you may calculate it as:
     * long chunkKey = (long) chunkX &amp; 0xffffffffL | ((long) chunkZ &amp; 0xffffffffL) &gt;&gt; 32;
     *
     * @param chunkKey The Chunk Key to look up the chunk by
     * @return The chunk at the specified key
     */
    @NotNull
    default Chunk getChunkAt(long chunkKey) {
        return getChunkAt(chunkKey, true);
    }

    /**
     * Gets the chunk at the specified chunk key, which is the X and Z packed into a long.
     * <p>
     * See {@link Chunk#getChunkKey()} for easy access to the key, or you may calculate it as:
     * long chunkKey = (long) chunkX &amp; 0xffffffffL | ((long) chunkZ &amp; 0xffffffffL) &gt;&gt; 32;
     *
     * @param chunkKey The Chunk Key to look up the chunk by
     * @param generate Whether the chunk should be fully generated or not
     * @return The chunk at the specified key
     */
    @NotNull
    default Chunk getChunkAt(long chunkKey, boolean generate) {
        return getChunkAt((int) chunkKey, (int) (chunkKey >> 32), generate);
    }
    // Paper end - chunk long key API

    // Paper start - isChunkGenerated API
    /**
     * Checks if a {@link Chunk} has been generated at the specified chunk key,
     * which is the X and Z packed into a long.
     *
     * @param chunkKey The Chunk Key to look up the chunk by
     * @return true if the chunk has been generated, otherwise false
     */
    default boolean isChunkGenerated(long chunkKey) {
        return isChunkGenerated((int) chunkKey, (int) (chunkKey >> 32));
    }
    // Paper end - isChunkGenerated API

    /**
     * Checks if the specified {@link Chunk} is loaded
     *
     * @param chunk The chunk to check
     * @return true if the chunk is loaded, otherwise false
     */
    public boolean isChunkLoaded(@NotNull Chunk chunk);

    /**
     * Gets an array of all loaded {@link Chunk}s
     *
     * @return Chunk[] containing all loaded chunks
     */
    public @NotNull Chunk @NotNull [] getLoadedChunks();

    /**
     * Loads the specified {@link Chunk}.
     * <p>
     * <b>This method will keep the specified chunk loaded until one of the
     * unload methods is manually called. Callers are advised to instead use
     * getChunkAt which will only temporarily load the requested chunk.</b>
     *
     * @param chunk The chunk to load
     */
    public void loadChunk(@NotNull Chunk chunk);

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
     * @deprecated This method was added to facilitate chunk garbage collection.
     *     As of the current Minecraft version chunks are now strictly managed and
     *     will not be loaded for more than 1 tick unless they are in use.
     */
    @Deprecated(since = "1.14")
    public boolean isChunkInUse(int x, int z);

    /**
     * Loads the {@link Chunk} at the specified coordinates.
     * <p>
     * <b>This method will keep the specified chunk loaded until one of the
     * unload methods is manually called. Callers are advised to instead use
     * getChunkAt which will only temporarily load the requested chunk.</b>
     * <p>
     * If the chunk does not exist, it will be generated.
     * <p>
     * This method is analogous to {@link #loadChunk(int, int, boolean)} where
     * generate is true.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     */
    default void loadChunk(int x, int z) {
        this.loadChunk(x, z, true);
    }

    /**
     * Loads the {@link Chunk} at the specified coordinates.
     * <p>
     * <b>This method will keep the specified chunk loaded until one of the
     * unload methods is manually called. Callers are advised to instead use
     * getChunkAt which will only temporarily load the requested chunk.</b>
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
     * This method is analogous to {@link #unloadChunk(int, int, boolean)}
     * where save is true.
     *
     * @param chunk the chunk to unload
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    default boolean unloadChunk(@NotNull Chunk chunk) {
        return this.unloadChunk(chunk.getX(), chunk.getZ());
    }

    /**
     * Safely unloads and saves the {@link Chunk} at the specified coordinates
     * <p>
     * This method is analogous to {@link #unloadChunk(int, int, boolean)}
     * where save is true.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    default boolean unloadChunk(int x, int z) {
        return this.unloadChunk(x, z, true);
    }

    /**
     * Safely unloads and optionally saves the {@link Chunk} at the specified
     * coordinates.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param save Whether or not to save the chunk
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    public boolean unloadChunk(int x, int z, boolean save);

    /**
     * Safely queues the {@link Chunk} at the specified coordinates for
     * unloading.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return true is the queue attempt was successful, otherwise false
     */
    public boolean unloadChunkRequest(int x, int z);

    /**
     * Regenerates the {@link Chunk} at the specified coordinates
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return Whether the chunk was actually regenerated
     *
     * @throws UnsupportedOperationException not implemented
     * @deprecated regenerating a single chunk is not likely to produce the same
     * chunk as before as terrain decoration may be spread across chunks. It may
     * or may not change blocks in the adjacent chunks as well.
     */
    @Deprecated(since = "1.13", forRemoval = true)
    default boolean regenerateChunk(int x, int z) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version! This is not a bug.");
    }

    /**
     * Resends the {@link Chunk} to all clients
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return Whether the chunk was actually refreshed
     *
     */
    public boolean refreshChunk(int x, int z);

    /**
     * Get a list of all players who are can view the specified chunk from their
     * client
     * <p>
     * This list will be empty if no players are viewing the chunk, or the chunk
     * is unloaded.
     *
     * @param chunk the chunk to check
     * @return collection of players who can see the chunk
     */
    @NotNull
    public Collection<Player> getPlayersSeeingChunk(@NotNull Chunk chunk);

    /**
     * Get a list of all players who are can view the specified chunk from their
     * client
     * <p>
     * This list will be empty if no players are viewing the chunk, or the chunk
     * is unloaded.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return collection of players who can see the chunk
     */
    @NotNull
    public Collection<Player> getPlayersSeeingChunk(int x, int z);

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
     * @param forced force load status
     */
    public void setChunkForceLoaded(int x, int z, boolean forced);

    /**
     * Returns all force loaded chunks in this world.
     * <p>
     * A force loaded chunk will not be unloaded due to lack of player activity.
     *
     * @return unmodifiable collection of force loaded chunks
     */
    @NotNull
    public Collection<Chunk> getForceLoadedChunks();

    /**
     * Adds a plugin ticket for the specified chunk, loading the chunk if it is
     * not already loaded.
     * <p>
     * A plugin ticket will prevent a chunk from unloading until it is
     * explicitly removed. A plugin instance may only have one ticket per chunk,
     * but each chunk can have multiple plugin tickets.
     * </p>
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param plugin Plugin which owns the ticket
     * @return {@code true} if a plugin ticket was added, {@code false} if the
     * ticket already exists for the plugin
     * @throws IllegalStateException If the specified plugin is not enabled
     * @see #removePluginChunkTicket(int, int, Plugin)
     */
    public boolean addPluginChunkTicket(int x, int z, @NotNull Plugin plugin);

    /**
     * Removes the specified plugin's ticket for the specified chunk
     * <p>
     * A plugin ticket will prevent a chunk from unloading until it is
     * explicitly removed. A plugin instance may only have one ticket per chunk,
     * but each chunk can have multiple plugin tickets.
     * </p>
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param plugin Plugin which owns the ticket
     * @return {@code true} if the plugin ticket was removed, {@code false} if
     * there is no plugin ticket for the chunk
     * @see #addPluginChunkTicket(int, int, Plugin)
     */
    public boolean removePluginChunkTicket(int x, int z, @NotNull Plugin plugin);

    /**
     * Removes all plugin tickets for the specified plugin
     * <p>
     * A plugin ticket will prevent a chunk from unloading until it is
     * explicitly removed. A plugin instance may only have one ticket per chunk,
     * but each chunk can have multiple plugin tickets.
     * </p>
     *
     * @param plugin Specified plugin
     * @see #addPluginChunkTicket(int, int, Plugin)
     * @see #removePluginChunkTicket(int, int, Plugin)
     */
    public void removePluginChunkTickets(@NotNull Plugin plugin);

    /**
     * Retrieves a collection specifying which plugins have tickets for the
     * specified chunk. This collection is not updated when plugin tickets are
     * added or removed to the chunk.
     * <p>
     * A plugin ticket will prevent a chunk from unloading until it is
     * explicitly removed. A plugin instance may only have one ticket per chunk,
     * but each chunk can have multiple plugin tickets.
     * </p>
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return unmodifiable collection containing which plugins have tickets for
     * the chunk
     * @see #addPluginChunkTicket(int, int, Plugin)
     * @see #removePluginChunkTicket(int, int, Plugin)
     */
    @NotNull
    public Collection<Plugin> getPluginChunkTickets(int x, int z);

    /**
     * Returns a map of which plugins have tickets for what chunks. The returned
     * map is not updated when plugin tickets are added or removed to chunks. If
     * a plugin has no tickets, it will be absent from the map.
     * <p>
     * A plugin ticket will prevent a chunk from unloading until it is
     * explicitly removed. A plugin instance may only have one ticket per chunk,
     * but each chunk can have multiple plugin tickets.
     * </p>
     *
     * @return unmodifiable map containing which plugins have tickets for what
     * chunks
     * @see #addPluginChunkTicket(int, int, Plugin)
     * @see #removePluginChunkTicket(int, int, Plugin)
     */
    @NotNull
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets();

    /**
     * Gets all Chunks intersecting the given BoundingBox.
     *
     * @param box BoundingBox to check
     * @return A collection of Chunks intersecting the given BoundingBox
     */
    @NotNull
    public Collection<Chunk> getIntersectingChunks(@NotNull BoundingBox box);

    /**
     * Drops an item at the specified {@link Location}
     *
     * @param location Location to drop the item
     * @param item ItemStack to drop
     * @return ItemDrop entity created as a result of this method
     */
    @NotNull
    default Item dropItem(@NotNull Location location, @NotNull ItemStack item) {
        return this.dropItem(location, item, null);
    }

    /**
     * Drops an item at the specified {@link Location}
     * Note that functions will run before the entity is spawned
     *
     * @param location Location to drop the item
     * @param item ItemStack to drop
     * @param function the function to be run before the entity is spawned.
     * @return ItemDrop entity created as a result of this method
     */
    @NotNull
    public Item dropItem(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<? super Item> function);

    /**
     * Drops an item at the specified {@link Location} with a random offset
     *
     * @param location Location to drop the item
     * @param item ItemStack to drop
     * @return ItemDrop entity created as a result of this method
     */
    @NotNull
    default Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item) {
        return this.dropItemNaturally(location, item, null);
    }

    /**
     * Drops an item at the specified {@link Location} with a random offset
     * Note that functions will run before the entity is spawned
     *
     * @param location Location to drop the item
     * @param item ItemStack to drop
     * @param function the function to be run before the entity is spawned.
     * @return ItemDrop entity created as a result of this method
     */
    @NotNull
    public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<? super Item> function);

    /**
     * Creates an {@link Arrow} entity at the given {@link Location}
     *
     * @param location Location to spawn the arrow
     * @param direction Direction to shoot the arrow in
     * @param speed Speed of the arrow. A recommend speed is 0.6
     * @param spread Spread of the arrow. A recommend spread is 12
     * @return Arrow entity spawned as a result of this method
     */
    @NotNull
    default Arrow spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread) {
        return this.spawnArrow(location, direction, speed, spread, Arrow.class);
    }

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
    @NotNull
    public <T extends AbstractArrow> T spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread, @NotNull Class<T> clazz);

    /**
     * Creates a tree at the given {@link Location}
     *
     * @param location Location to spawn the tree
     * @param type Type of the tree to create
     * @return true if the tree was created successfully, otherwise false
     * @deprecated in favor of {@link #generateTree(Location, java.util.Random, TreeType)} to specify its own random instance
     * and this method is not accessible through {@link RegionAccessor}
     */
    @Deprecated(since = "1.21.6")
    public boolean generateTree(@NotNull Location location, @NotNull TreeType type);

    /**
     * Creates a tree at the given {@link Location}
     *
     * @param loc Location to spawn the tree
     * @param type Type of the tree to create
     * @param delegate A class to call for each block changed as a result of
     *     this method
     * @return true if the tree was created successfully, otherwise false
     * @see #generateTree(org.bukkit.Location, java.util.Random, org.bukkit.TreeType, java.util.function.Consumer)
     * @deprecated this method does not handle block entities (bee nests)
     */
    @Deprecated(since = "1.17.1")
    public boolean generateTree(@NotNull Location loc, @NotNull TreeType type, @NotNull BlockChangeDelegate delegate);

    /**
     * Strikes lightning at the given {@link Location}
     *
     * @param loc The location to strike lightning
     * @return The lightning entity.
     */
    @NotNull
    public LightningStrike strikeLightning(@NotNull Location loc);

    /**
     * Strikes lightning at the given {@link Location} without doing damage
     *
     * @param loc The location to strike lightning
     * @return The lightning entity.
     */
    @NotNull
    public LightningStrike strikeLightningEffect(@NotNull Location loc);

    // Paper start
    /**
     * Finds the location of the nearest unobstructed Lightning Rod in a 128-block
     * radius around the given location. Returns {@code null} if no Lightning Rod is found.
     *
     * <p>Note: To activate a Lightning Rod, the position one block above it must be struck by lightning.</p>
     *
     * @param location {@link Location} to search for Lightning Rod around
     * @return {@link Location} of Lightning Rod or {@code null}
     */
    @Nullable
    public Location findLightningRod(@NotNull Location location);

    /**
     * Finds a target {@link Location} for lightning to strike.
     * <p>It selects from (in the following order):</p>
     * <ol>
     *  <li>the block above the nearest Lightning Rod, found using {@link World#findLightningRod(Location)}</li>
     *  <li>a random {@link LivingEntity} that can see the sky in a 6x6 cuboid
     *      around input X/Z coordinates. Y ranges from <i>the highest motion-blocking
     *      block at the input X/Z - 3</i> to <i>the height limit + 3</i></li>
     * </ol>
     * <p>Returns {@code null} if no target is found.</p>
     *
     * @param location {@link Location} to search for target around
     * @return lightning target or {@code null}
     */
    @Nullable
    public Location findLightningTarget(@NotNull Location location);
    // Paper end

    /**
     * Get a list of all entities in this World
     *
     * @return A List of all Entities currently residing in this world
     */
    @NotNull
    public List<Entity> getEntities();

    /**
     * Get a list of all living entities in this World
     *
     * @return A List of all LivingEntities currently residing in this world
     */
    @NotNull
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
    @Deprecated(since = "1.1")
    @NotNull
    public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes);

    /**
     * Get a collection of all entities in this World matching the given
     * class/interface
     *
     * @param <T> an entity subclass
     * @param cls The class representing the type of entity to match
     * @return A List of all Entities currently residing in this world that
     *     match the given class/interface
     */
    @NotNull
    public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls);

    /**
     * Get a collection of all entities in this World matching any of the
     * given classes/interfaces
     *
     * @param classes The classes representing the types of entity to match
     * @return A List of all Entities currently residing in this world that
     *     match one or more of the given classes/interfaces
     */
    @NotNull
    public Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes);

    // Paper start - additional getNearbyEntities API
    /**
     * Gets nearby LivingEntities within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param radius Radius
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    default @NotNull Collection<LivingEntity> getNearbyLivingEntities(final @NotNull Location loc, final double radius) {
        return this.getNearbyEntitiesByType(LivingEntity.class, loc, radius, radius, radius);
    }

    /**
     * Gets nearby LivingEntities within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param xzRadius X/Z Radius
     * @param yRadius Y Radius
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    default @NotNull Collection<LivingEntity> getNearbyLivingEntities(final @NotNull Location loc, final double xzRadius, final double yRadius) {
        return this.getNearbyEntitiesByType(LivingEntity.class, loc, xzRadius, yRadius, xzRadius);
    }

    /**
     * Gets nearby LivingEntities within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z radius
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    default @NotNull Collection<LivingEntity> getNearbyLivingEntities(final @NotNull Location loc, final double xRadius, final double yRadius, final double zRadius) {
        return this.getNearbyEntitiesByType(LivingEntity.class, loc, xRadius, yRadius, zRadius);
    }

    /**
     * Gets nearby LivingEntities within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param radius X Radius
     * @param predicate a predicate used to filter results
     * @return the collection of living entities near location. This will always be a non-null collection
     */
    default @NotNull Collection<LivingEntity> getNearbyLivingEntities(final @NotNull Location loc, final double radius, final @Nullable Predicate<? super LivingEntity> predicate) {
        return this.getNearbyEntitiesByType(LivingEntity.class, loc, radius, radius, radius, predicate);
    }

    /**
     * Gets nearby LivingEntities within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param xzRadius X/Z Radius
     * @param yRadius Y Radius
     * @param predicate a predicate used to filter results
     * @return the collection of living entities near location. This will always be a non-null collection
     */
    default @NotNull Collection<LivingEntity> getNearbyLivingEntities(final @NotNull Location loc, final double xzRadius, final double yRadius, final @Nullable Predicate<? super LivingEntity> predicate) {
        return this.getNearbyEntitiesByType(LivingEntity.class, loc, xzRadius, yRadius, xzRadius, predicate);
    }

    /**
     * Gets nearby LivingEntities within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z radius
     * @param predicate a predicate used to filter results
     * @return the collection of living entities near location. This will always be a non-null collection.
     */
    default @NotNull Collection<LivingEntity> getNearbyLivingEntities(final @NotNull Location loc, final double xRadius, final double yRadius, final double zRadius, final @Nullable Predicate<? super LivingEntity> predicate) {
        return this.getNearbyEntitiesByType(LivingEntity.class, loc, xRadius, yRadius, zRadius, predicate);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param radius X/Y/Z Radius
     * @return the collection of living entities near location. This will always be a non-null collection.
     */
    default @NotNull Collection<Player> getNearbyPlayers(final @NotNull Location loc, final double radius) {
        return this.getNearbyEntitiesByType(Player.class, loc, radius, radius, radius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param xzRadius X/Z Radius
     * @param yRadius Y Radius
     * @return the collection of living entities near location. This will always be a non-null collection.
     */
    default @NotNull Collection<Player> getNearbyPlayers(final @NotNull Location loc, final double xzRadius, final double yRadius) {
        return this.getNearbyEntitiesByType(Player.class, loc, xzRadius, yRadius, xzRadius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z Radius
     * @return the collection of players near location. This will always be a non-null collection.
     */
    default @NotNull Collection<Player> getNearbyPlayers(final @NotNull Location loc, final double xRadius, final double yRadius, final double zRadius) {
        return this.getNearbyEntitiesByType(Player.class, loc, xRadius, yRadius, zRadius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param radius X/Y/Z Radius
     * @param predicate a predicate used to filter results
     * @return the collection of players near location. This will always be a non-null collection.
     */
    default @NotNull Collection<Player> getNearbyPlayers(final @NotNull Location loc, final double radius, final @Nullable Predicate<? super Player> predicate) {
        return this.getNearbyEntitiesByType(Player.class, loc, radius, radius, radius, predicate);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param xzRadius X/Z Radius
     * @param yRadius Y Radius
     * @param predicate a predicate used to filter results
     * @return the collection of players near location. This will always be a non-null collection.
     */
    default @NotNull Collection<Player> getNearbyPlayers(final @NotNull Location loc, final double xzRadius, final double yRadius, final @Nullable Predicate<? super Player> predicate) {
        return this.getNearbyEntitiesByType(Player.class, loc, xzRadius, yRadius, xzRadius, predicate);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param loc Center location
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z Radius
     * @param predicate a predicate used to filter results
     * @return the collection of players near location. This will always be a non-null collection.
     */
    default @NotNull Collection<Player> getNearbyPlayers(final @NotNull Location loc, final double xRadius, final double yRadius, final double zRadius, final @Nullable Predicate<? super Player> predicate) {
        return this.getNearbyEntitiesByType(Player.class, loc, xRadius, yRadius, zRadius, predicate);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius (bounding box)
     *
     * @param clazz Type to filter by
     * @param loc Center location
     * @param radius X/Y/Z radius to search within
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    default @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final @NotNull Location loc, final double radius) {
        return this.getNearbyEntitiesByType(clazz, loc, radius, radius, radius, null);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius, with x and x radius matching (bounding box)
     *
     * @param clazz Type to filter by
     * @param loc Center location
     * @param xzRadius X/Z radius to search within
     * @param yRadius Y radius to search within
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    default @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final @NotNull Location loc, final double xzRadius, final double yRadius) {
        return this.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius, xzRadius, null);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius (bounding box)
     *
     * @param clazz Type to filter by
     * @param loc Center location
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z Radius
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    default @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final @NotNull Location loc, final double xRadius, final double yRadius, final double zRadius) {
        return this.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius, null);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius (bounding box)
     *
     * @param clazz Type to filter by
     * @param loc Center location
     * @param radius X/Y/Z radius to search within
     * @param predicate a predicate used to filter results
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    default @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final @NotNull Location loc, final double radius, final @Nullable Predicate<? super T> predicate) {
        return this.getNearbyEntitiesByType(clazz, loc, radius, radius, radius, predicate);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius, with x and x radius matching (bounding box)
     *
     * @param clazz Type to filter by
     * @param loc Center location
     * @param xzRadius X/Z radius to search within
     * @param yRadius Y radius to search within
     * @param predicate a predicate used to filter results
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    default @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final @NotNull Location loc, final double xzRadius, final double yRadius, final @Nullable Predicate<? super T> predicate) {
        return this.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius, xzRadius, predicate);
    }

     /**
      * Gets all nearby entities of the specified type, within the specified radius (bounding box)
      *
      * @param clazz Type to filter by
      * @param loc Center location
      * @param xRadius X Radius
      * @param yRadius Y Radius
      * @param zRadius Z Radius
      * @param predicate a predicate used to filter results
      * @param <T> the entity type
      * @return the collection of entities near location. This will always be a non-null collection.
      */
    default <T extends Entity> @NotNull Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, final @NotNull Location loc, final double xRadius, final double yRadius, final double zRadius, final @Nullable Predicate<? super T> predicate) {
        final List<T> nearby = new ArrayList<>();
        for (final Entity entity : this.getNearbyEntities(loc, xRadius, yRadius, zRadius)) {
            //noinspection unchecked
            if ((clazz == null || clazz.isInstance(entity)) && (predicate == null || predicate.test((T) entity))) {
                //noinspection unchecked
                nearby.add((T) entity);
            }
        }
        return nearby;
    }
    // Paper end - additional getNearbyEntities API

    // Paper start - async chunks API
    /**
     * This is the Legacy API before Java 8 was supported. Java 8 Consumer is provided,
     * as well as future support
     *
     * Used by {@link World#getChunkAtAsync(Location,ChunkLoadCallback)} methods
     * to request a {@link Chunk} to be loaded, with this callback receiving
     * the chunk when it is finished.
     *
     * This callback will be executed on synchronously on the main thread.
     *
     * Timing and order this callback is fired is intentionally not defined
     * and subject to change.
     *
     * @deprecated Use either the Future or the Consumer based methods
     */
    @Deprecated(since = "1.13.1")
    public static interface ChunkLoadCallback extends java.util.function.Consumer<Chunk> {
        public void onLoad(@NotNull Chunk chunk);

        // backwards compat to old api
        @Override
        default void accept(@NotNull Chunk chunk) {
            onLoad(chunk);
        }
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link ChunkLoadCallback} will always be executed synchronously
     * on the main Server Thread.
     *
     * @deprecated Use either the Future or the Consumer based methods
     * @param x Chunk x-coordinate
     * @param z Chunk z-coordinate
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    @Deprecated(since = "1.13.1")
    public default void getChunkAtAsync(int x, int z, @NotNull ChunkLoadCallback cb) {
        this.getChunkAtAsync(x, z, (java.util.function.Consumer<Chunk>)cb);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given {@link Location}
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link ChunkLoadCallback} will always be executed synchronously
     * on the main Server Thread.
     *
     * @deprecated Use either the Future or the Consumer based methods
     * @param loc Location of the chunk
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    @Deprecated(since = "1.13.1")
    public default void getChunkAtAsync(@NotNull Location loc, @NotNull ChunkLoadCallback cb) {
        this.getChunkAtAsync(loc.getBlockX() >> 4, loc.getBlockZ() >> 4, cb);
    }

    /**
     * Requests {@link Chunk} to be loaded that contains the given {@link Block}
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link ChunkLoadCallback} will always be executed synchronously
     * on the main Server Thread.
     *
     * @deprecated Use either the Future or the Consumer based methods
     * @param block Block to get the containing chunk from
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    @Deprecated(since = "1.13.1")
    public default void getChunkAtAsync(@NotNull Block block, @NotNull ChunkLoadCallback cb) {
        this.getChunkAtAsync(block.getX() >> 4, block.getZ() >> 4, cb);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link java.util.function.Consumer} will always be executed synchronously
     * on the main Server Thread.
     *
     * @param x Chunk x-coordinate
     * @param z Chunk z-coordinate
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    default void getChunkAtAsync(final int x, final int z, final @NotNull Consumer<? super Chunk> cb) {
        this.getChunkAtAsync(x, z, true, cb);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link java.util.function.Consumer} will always be executed synchronously
     * on the main Server Thread.
     *
     * @param x Chunk x-coordinate
     * @param z Chunk z-coordinate
     * @param gen Should we generate a chunk if it doesn't exist or not
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    default void getChunkAtAsync(final int x, final int z, final boolean gen, final @NotNull Consumer<? super Chunk> cb) {
        this.getChunkAtAsync(x, z, gen, false, cb);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link java.util.function.Consumer} will always be executed synchronously
     * on the main Server Thread.
     *
     * @param x Chunk x-coordinate
     * @param z Chunk z-coordinate
     * @param gen Should we generate a chunk if it doesn't exist or not
     * @param urgent If true, the chunk may be prioritised to be loaded above other chunks in queue
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    void getChunkAtAsync(final int x, final int z, final boolean gen, final boolean urgent, final @NotNull Consumer<? super Chunk> cb);

    /**
     * Requests all chunks with x between [minX, maxZ] and z
     * between [minZ, maxZ] to be loaded.
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will invoke the callback at possibly a later time.
     *
     * You should use this method if you need chunks loaded but do not need them
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link Runnable} will always be executed synchronously
     * on the main Server Thread, and when invoked all chunks requested will be loaded.
     *
     * @param minX Minimum Chunk x-coordinate
     * @param minZ Minimum Chunk z-coordinate
     * @param maxX Maximum Chunk x-coordinate
     * @param maxZ Maximum Chunk z-coordinate
     * @param urgent If true, the chunks may be prioritised to be loaded above other chunks in queue
     * @param cb Callback to invoke when all chunks are loaded.
     *           Will be executed synchronously
     * @see Chunk
     */
    void getChunksAtAsync(final int minX, final int minZ, final int maxX, final int maxZ, final boolean urgent,
                          final @NotNull Runnable cb);

    /**
     * Requests a {@link Chunk} to be loaded at the given {@link Location}
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link java.util.function.Consumer} will always be executed synchronously
     * on the main Server Thread.
     *
     * @param loc Location of the chunk
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    default void getChunkAtAsync(final @NotNull Location loc, final @NotNull Consumer<? super Chunk> cb) {
        this.getChunkAtAsync((int) Math.floor(loc.getX()) >> 4, (int) Math.floor(loc.getZ()) >> 4, true, cb);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given {@link Location}
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link java.util.function.Consumer} will always be executed synchronously
     * on the main Server Thread.
     *
     * @param loc Location of the chunk
     * @param gen Should the chunk generate if it doesn't exist
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    default void getChunkAtAsync(final @NotNull Location loc, final boolean gen, final @NotNull Consumer<? super Chunk> cb) {
        this.getChunkAtAsync((int) Math.floor(loc.getX()) >> 4, (int) Math.floor(loc.getZ()) >> 4, gen, cb);
    }

    /**
     * Requests {@link Chunk} to be loaded that contains the given {@link Block}
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link java.util.function.Consumer} will always be executed synchronously
     * on the main Server Thread.
     *
     * @param block Block to get the containing chunk from
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    default void getChunkAtAsync(final @NotNull Block block, final @NotNull Consumer<? super Chunk> cb) {
        this.getChunkAtAsync(block.getX() >> 4, block.getZ() >> 4, true, cb);
    }

    /**
     * Requests {@link Chunk} to be loaded that contains the given {@link Block}
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The {@link java.util.function.Consumer} will always be executed synchronously
     * on the main Server Thread.
     *
     * @param block Block to get the containing chunk from
     * @param gen Should the chunk generate if it doesn't exist
     * @param cb Callback to receive the chunk when it is loaded.
     *           will be executed synchronously
     */
    default void getChunkAtAsync(final @NotNull Block block, final boolean gen, final @NotNull Consumer<? super Chunk> cb) {
        this.getChunkAtAsync(block.getX() >> 4, block.getZ() >> 4, gen, cb);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The future will always be executed synchronously
     * on the main Server Thread.
     * @param loc Location to load the corresponding chunk from
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsync(final @NotNull Location loc) {
        return this.getChunkAtAsync((int) Math.floor(loc.getX()) >> 4, (int) Math.floor(loc.getZ()) >> 4, true);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The future will always be executed synchronously
     * on the main Server Thread.
     * @param loc Location to load the corresponding chunk from
     * @param gen Should the chunk generate if it doesn't exist
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsync(final @NotNull Location loc, final boolean gen) {
        return this.getChunkAtAsync((int) Math.floor(loc.getX()) >> 4, (int) Math.floor(loc.getZ()) >> 4, gen);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The future will always be executed synchronously
     * on the main Server Thread.
     * @param block Block to load the corresponding chunk from
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsync(final @NotNull Block block) {
        return this.getChunkAtAsync(block.getX() >> 4, block.getZ() >> 4, true);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The future will always be executed synchronously
     * on the main Server Thread.
     * @param block Block to load the corresponding chunk from
     * @param gen Should the chunk generate if it doesn't exist
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsync(final @NotNull Block block, final boolean gen) {
        return this.getChunkAtAsync(block.getX() >> 4, block.getZ() >> 4, gen);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The future will always be executed synchronously
     * on the main Server Thread.
     *
     * @param x Chunk x-coordinate
     * @param z Chunk z-coordinate
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsync(final int x, final int z) {
        return this.getChunkAtAsync(x, z, true);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     *
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     *
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     *
     * The future will always be executed synchronously
     * on the main Server Thread.
     *
     * @param x Chunk x-coordinate
     * @param z Chunk z-coordinate
     * @param gen Should we generate a chunk if it doesn't exist or not
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsync(final int x, final int z, final boolean gen) {
        return this.getChunkAtAsync(x, z, gen, false);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     * <p>
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     * <p>
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish for it to be prioritised over other
     * chunk loads in queue.
     * <p>
     * The future will always be executed synchronously
     * on the main Server Thread.
     * @param loc Location to load the corresponding chunk from
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsyncUrgently(final @NotNull Location loc) {
        return this.getChunkAtAsync((int) Math.floor(loc.getX()) >> 4, (int) Math.floor(loc.getZ()) >> 4, true, true);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     * <p>
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     * <p>
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish for it to be prioritised over other
     * chunk loads in queue.
     * <p>
     * The future will always be executed synchronously
     * on the main Server Thread.
     * @param loc Location to load the corresponding chunk from
     * @param gen Should the chunk generate if it doesn't exist
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsyncUrgently(final @NotNull Location loc, final boolean gen) {
        return this.getChunkAtAsync((int) Math.floor(loc.getX()) >> 4, (int) Math.floor(loc.getZ()) >> 4, gen, true);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     * <p>
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     * <p>
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish for it to be prioritised over other
     * chunk loads in queue.
     * <p>
     * The future will always be executed synchronously
     * on the main Server Thread.
     * @param block Block to load the corresponding chunk from
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsyncUrgently(final @NotNull Block block) {
        return this.getChunkAtAsync(block.getX() >> 4, block.getZ() >> 4, true, true);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     * <p>
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     * <p>
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish for it to be prioritised over other
     * chunk loads in queue.
     * <p>
     * The future will always be executed synchronously
     * on the main Server Thread.
     * @param block Block to load the corresponding chunk from
     * @param gen Should the chunk generate if it doesn't exist
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsyncUrgently(final @NotNull Block block, final boolean gen) {
        return this.getChunkAtAsync(block.getX() >> 4, block.getZ() >> 4, gen, true);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates
     * <p>
     * This method makes no guarantee on how fast the chunk will load,
     * and will complete the future at a later time.
     * <p>
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish for it to be prioritised over other
     * chunk loads in queue.
     * <p>
     * The future will always be executed synchronously
     * on the main Server Thread.
     *
     * @param x Chunk x-coordinate
     * @param z Chunk z-coordinate
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsyncUrgently(final int x, final int z) {
        return this.getChunkAtAsync(x, z, true, true);
    }

    /**
     * Requests a {@link Chunk} to be loaded at the given coordinates.
     * <p>
     * This method makes no guarantee on how fast the chunk will load,
     * and will return the chunk to the callback at a later time.
     * <p>
     * You should use this method if you need a chunk but do not need it
     * immediately, and you wish to let the server control the speed
     * of chunk loads, keeping performance in mind.
     * <p>
     * The future will always be executed synchronously
     * on the main Server Thread.
     *
     * @param x Chunk x-coordinate
     * @param z Chunk z-coordinate
     * @param gen Should the chunk generate if it doesn't exist
     * @param urgent If true, the chunk may be prioritised to be loaded above other chunks in queue
     *
     * @return Future that will resolve when the chunk is loaded
     */
    default @NotNull java.util.concurrent.CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen, boolean urgent) {
        java.util.concurrent.CompletableFuture<Chunk> ret = new java.util.concurrent.CompletableFuture<>();
        this.getChunkAtAsync(x, z, gen, urgent, ret::complete);
        return ret;
    }
    // Paper end - async chunks API

    /**
     * Get a list of all players in this World
     *
     * @return A list of all Players currently residing in this world
     */
    @NotNull
    public List<Player> getPlayers();

    // Paper start
    @NotNull
    @Override
    default Iterable<? extends net.kyori.adventure.audience.Audience> audiences() {
        return this.getPlayers();
    }
    // Paper end

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
    @NotNull
    default Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
        return this.getNearbyEntities(location, x, y, z, null);
    }

    // Paper start - getEntity by UUID API
    /**
     * Gets an entity in this world by its UUID
     *
     * @param uuid the UUID of the entity
     * @return the entity with the given UUID, or null if it isn't found
     */
    @Nullable
    public Entity getEntity(@NotNull java.util.UUID uuid);
    // Paper end

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
    @NotNull
    public Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z, @Nullable Predicate<? super Entity> filter);

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
    @NotNull
    default Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
        return this.getNearbyEntities(boundingBox, null);
    }

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
    @NotNull
    public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, @Nullable Predicate<? super Entity> filter);

    /**
     * Performs a ray trace that checks for entity collisions.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the maximum
     * distance.
     * <p>
     * <b>Note:</b> Due to display entities having a zero size hitbox, this method will not detect them.
     * To detect display entities use {@link #rayTraceEntities(Location, Vector, double, double)} with a positive raySize
     *
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @return the closest ray trace hit result, or <code>null</code> if there
     *     is no hit
     * @see #rayTraceEntities(Location, Vector, double, double, Predicate)
     */
    @Nullable
    default RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
        return this.rayTraceEntities(start, direction, maxDistance, null);
    }

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
     *     shrunk) by this value before doing collision checks
     * @return the closest ray trace hit result, or <code>null</code> if there
     *     is no hit
     * @see #rayTraceEntities(Location, Vector, double, double, Predicate)
     */
    @Nullable
    default RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize) {
        return this.rayTraceEntities(start, direction, maxDistance, raySize, null);
    }

    /**
     * Performs a ray trace that checks for entity collisions.
     * <p>
     * This may not consider entities in currently unloaded chunks. Some
     * implementations may impose artificial restrictions on the maximum
     * distance.
     * <p>
     * <b>Note:</b> Due to display entities having a zero size hitbox, this method will not detect them.
     * To detect display entities use {@link #rayTraceEntities(Location, Vector, double, double, Predicate)} with a positive raySize
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
    @Nullable
    default RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, @Nullable Predicate<? super Entity> filter) {
        return this.rayTraceEntities(start, direction, maxDistance, 0.0D, filter);
    }

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
     *     shrunk) by this value before doing collision checks
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @return the closest ray trace hit result, or <code>null</code> if there
     *     is no hit
     */
    @Nullable
    default RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize, @Nullable Predicate<? super Entity> filter) {
        return rayTraceEntities((io.papermc.paper.math.Position) start, direction, maxDistance, raySize, filter);
    }

    // Paper start
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
     *     shrunk) by this value before doing collision checks
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @return the closest ray trace hit result, or <code>null</code> if there
     *     is no hit
     */
    @Nullable RayTraceResult rayTraceEntities(io.papermc.paper.math.@NotNull Position start, @NotNull Vector direction, double maxDistance, double raySize, @Nullable Predicate<? super Entity> filter);
    // Paper end

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
    @Nullable
    default RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
        return this.rayTraceBlocks(start, direction, maxDistance, FluidCollisionMode.NEVER);
    }

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
    @Nullable
    default RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, false);
    }

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
    @Nullable
    default RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        return this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks, null);
    }

    // Paper start
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
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param fluidCollisionMode the fluid collision mode
     * @param ignorePassableBlocks whether to ignore passable but collidable
     *     blocks (ex. tall grass, signs, fluids, ..)
     * @param canCollide predicate for blocks the ray can potentially collide
     *     with, or <code>null</code> to consider all blocks
     * @return the ray trace hit result, or <code>null</code> if there is no hit
     */
    @Nullable RayTraceResult rayTraceBlocks(io.papermc.paper.math.@NotNull Position start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, @Nullable Predicate<? super Block> canCollide);
    // Paper end

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
     *     shrunk) by this value before doing collision checks
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @return the closest ray trace hit result with either a block or an
     *     entity, or <code>null</code> if there is no hit
     */
    @Nullable
    default RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, @Nullable Predicate<? super Entity> filter) {
        return this.rayTrace(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks, raySize, filter, null);
    }

    // Paper start
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
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param fluidCollisionMode the fluid collision mode
     * @param ignorePassableBlocks whether to ignore passable but collidable
     *     blocks (ex. tall grass, signs, fluids, ..)
     * @param raySize entity bounding boxes will be uniformly expanded (or
     *     shrunk) by this value before doing collision checks
     * @param filter only entities that fulfill this predicate are considered,
     *     or <code>null</code> to consider all entities
     * @param canCollide predicate for blocks the ray can potentially collide
     *     with, or <code>null</code> to consider all blocks
     * @return the closest ray trace hit result with either a block or an
     *     entity, or <code>null</code> if there is no hit
     */
    @Nullable RayTraceResult rayTrace(io.papermc.paper.math.@NotNull Position start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, @Nullable Predicate<? super Entity> filter, @Nullable Predicate<? super Block> canCollide);
    // Paper end

    /**
     * Performs a ray trace that checks for collisions with the specified
     * targets.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param builderConsumer a consumer to configure the ray trace configuration.
     *     The received builder is not valid for use outside the consumer
     * @return the closest ray trace hit result with either a block or an
     *     entity, or <code>null</code> if there is no hit
     */
    @Nullable RayTraceResult rayTrace(@NotNull Consumer<PositionedRayTraceConfigurationBuilder> builderConsumer);

    /**
     * Gets the default spawn {@link Location} of this world
     *
     * @return The spawn location of this world
     * @see Server#getRespawnWorld()
     */
    @NotNull
    public Location getSpawnLocation();

    /**
     * Sets the spawn location of the world.
     * <br>
     * The location provided must be equal to this world.
     *
     * @param location The {@link Location} to set the spawn for this world at.
     * @return True if it was successfully set.
     * @see Server#setRespawnWorld(World)
     */
    public boolean setSpawnLocation(@NotNull Location location);

    /**
     * Sets the spawn location of the world
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param yaw the yaw
     * @return True if it was successfully set.
     * @see Server#setRespawnWorld(World)
     */
    public boolean setSpawnLocation(int x, int y, int z, float yaw);

    /**
     * Sets the spawn location of the world
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return True if it was successfully set.
     * @see Server#setRespawnWorld(World)
     */
    default boolean setSpawnLocation(int x, int y, int z) {
        return this.setSpawnLocation(x, y, z, 0.0F);
    }

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

    // Paper start

    /**
     * Check if it is currently daytime in this world
     *
     * @return True if it is daytime
     */
    public boolean isDayTime();
    // Paper end

    /**
     * Gets the full in-game time on this world since the world generation
     *
     * @return The current absolute time since the world generation
     * @see #getTime() Returns a relative time of this world
     * @see #getFullTime() Returns an absolute time of this world
     */
    public long getGameTime();

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
     * This will implicitly call {@link #setClearWeatherDuration(int)} with 0
     * ticks to reset the world's clear weather.
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
     * This will implicitly call {@link #setClearWeatherDuration(int)} with 0
     * ticks to reset the world's clear weather.
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
     * Returns whether the world has clear weather.
     *
     * This will be true such that {@link #isThundering()} and
     * {@link #hasStorm()} are both false.
     *
     * @return true if clear weather
     */
    public boolean isClearWeather();

    /**
     * Set the clear weather duration.
     *
     * The clear weather ticks determine whether or not the world will be
     * allowed to rain or storm. If clear weather ticks are &gt; 0, the world will
     * not naturally do either until the duration has elapsed.
     *
     * This method is equivalent to calling {@code /weather clear} with a set
     * amount of ticks.
     *
     * @param duration duration in ticks
     */
    public void setClearWeatherDuration(int duration);

    /**
     * Get the clear weather duration.
     *
     * @return duration in ticks
     */
    public int getClearWeatherDuration();

    /**
     * Creates explosion at given coordinates with given power
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param power The power of explosion, where 4F is TNT
     * @return false if explosion was canceled, otherwise true
     */
    default boolean createExplosion(double x, double y, double z, float power) {
        return this.createExplosion(x, y, z, power, false, true);
    }

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
    default boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return this.createExplosion(x, y, z, power, setFire, true);
    }

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
    default boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return this.createExplosion(x, y, z, power, setFire, breakBlocks, null);
    }

    /**
     * Creates explosion at given coordinates with given power and optionally
     * setting blocks on fire or breaking blocks.
     * <p>
     * Note that if a non-null {@code source} Entity is provided and {@code
     * breakBlocks} is {@code true}, the value of {@code breakBlocks} will be
     * ignored if {@link GameRule#MOB_GRIEFING} is {@code false} in the world
     * in which the explosion occurs. In other words, the mob griefing gamerule
     * will take priority over {@code breakBlocks} if explosions are not allowed.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @param breakBlocks Whether or not to have blocks be destroyed
     * @param source the source entity, used for tracking damage
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source);

    /**
     * Creates explosion at given coordinates with given power
     *
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @return false if explosion was canceled, otherwise true
     */
    default boolean createExplosion(@NotNull Location loc, float power) {
        return this.createExplosion(loc, power, false);
    }

    /**
     * Creates explosion at given coordinates with given power and optionally
     * setting blocks on fire.
     *
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @return false if explosion was canceled, otherwise true
     */
    default boolean createExplosion(@NotNull Location loc, float power, boolean setFire) {
        return this.createExplosion(loc, power, setFire, true);
    }

    // Paper start
    /**
     * Creates explosion at given location with given power and optionally
     * setting blocks on fire, with the specified entity as the source.
     *
     * @param source The source entity of the explosion
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @param breakBlocks Whether or not to have blocks be destroyed
     * @param excludeSourceFromDamage whether the explosion should exclude the passed source from taking damage like vanilla explosions do.
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire, boolean breakBlocks, boolean excludeSourceFromDamage);

    /**
     * Creates explosion at given location with given power and optionally
     * setting blocks on fire, with the specified entity as the source.
     *
     * @param source The source entity of the explosion
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @param breakBlocks Whether or not to have blocks be destroyed
     * @return false if explosion was canceled, otherwise true
     */
    default boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(source, loc, power, setFire, breakBlocks, true);
    }

    /**
     * Creates explosion at given location with given power and optionally
     * setting blocks on fire, with the specified entity as the source.
     *
     * Will destroy other blocks
     *
     * @param source The source entity of the explosion
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @return false if explosion was canceled, otherwise true
     */
    public default boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire) {
        return createExplosion(source, loc, power, setFire, true);
    }
    /**
     * Creates explosion at given location with given power, with the specified entity as the source.
     * Will set blocks on fire and destroy blocks.
     *
     * @param source The source entity of the explosion
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @return false if explosion was canceled, otherwise true
     */
    public default boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power) {
        return createExplosion(source, loc, power, true, true);
    }
    /**
     * Creates explosion at given entities location with given power and optionally
     * setting blocks on fire, with the specified entity as the source.
     *
     * @param source The source entity of the explosion
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @param breakBlocks Whether or not to have blocks be destroyed
     * @return false if explosion was canceled, otherwise true
     */
    public default boolean createExplosion(@NotNull Entity source, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(source, source.getLocation(), power, setFire, breakBlocks);
    }
    /**
     * Creates explosion at given entities location with given power and optionally
     * setting blocks on fire, with the specified entity as the source.
     *
     * Will destroy blocks.
     *
     * @param source The source entity of the explosion
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @return false if explosion was canceled, otherwise true
     */
    public default boolean createExplosion(@NotNull Entity source, float power, boolean setFire) {
        return createExplosion(source, source.getLocation(), power, setFire, true);
    }

    /**
     * Creates explosion at given entities location with given power and optionally
     * setting blocks on fire, with the specified entity as the source.
     *
     * @param source The source entity of the explosion
     * @param power The power of explosion, where 4F is TNT
     * @return false if explosion was canceled, otherwise true
     */
    public default boolean createExplosion(@NotNull Entity source, float power) {
        return createExplosion(source, source.getLocation(), power, true, true);
    }
    // Paper end

    /**
     * Creates explosion at given coordinates with given power and optionally
     * setting blocks on fire or breaking blocks.
     *
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @param breakBlocks Whether or not to have blocks be destroyed
     * @return false if explosion was canceled, otherwise true
     */
    default boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks) {
        return this.createExplosion(loc, power, setFire, breakBlocks, null);
    }

    /**
     * Creates explosion at given coordinates with given power and optionally
     * setting blocks on fire or breaking blocks.
     * <p>
     * Note that if a non-null {@code source} Entity is provided and {@code
     * breakBlocks} is {@code true}, the value of {@code breakBlocks} will be
     * ignored if {@link GameRule#MOB_GRIEFING} is {@code false} in the world
     * in which the explosion occurs. In other words, the mob griefing gamerule
     * will take priority over {@code breakBlocks} if explosions are not allowed.
     *
     * @param loc Location to blow up
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether or not to set blocks on fire
     * @param breakBlocks Whether or not to have blocks be destroyed
     * @param source the source entity, used for tracking damage
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source);

    /**
     * Gets the current PVP setting for this world.
     *
     * @return True if PVP is enabled
     * @deprecated use {@link GameRule#PVP} instead
     */
    @Deprecated(since = "1.21.9")
    public boolean getPVP();

    /**
     * Sets the PVP setting for this world.
     *
     * @param pvp True/False whether PVP should be Enabled.
     * @deprecated use {@link GameRule#PVP} instead
     */
    @Deprecated(since = "1.21.9")
    public void setPVP(boolean pvp);

    /**
     * Gets the chunk generator for this world
     *
     * @return ChunkGenerator associated with this world
     */
    @Nullable
    public ChunkGenerator getGenerator();

    /**
     * Gets the biome provider for this world
     *
     * @return BiomeProvider associated with this world
     */
    @Nullable
    public BiomeProvider getBiomeProvider();

    /**
     * Saves the world to disk
     */
    default void save() {
        save(false);
    }

    /**
     * Saves the world to disk
     * @param flush Whether to wait for the chunk writer to finish
     */
    void save(boolean flush);

    /**
     * Gets a list of all applied {@link BlockPopulator}s for this World
     *
     * @return List containing any or none BlockPopulators
     */
    @NotNull
    public List<BlockPopulator> getPopulators();

    /**
     * Creates a new entity at the given {@link Location} with the supplied
     * function run before the entity is added to the world.
     * <br>
     * Note that when the function is run, the entity will not be actually in
     * the world. Any operation involving such as teleporting the entity is undefined
     * until after this function returns.
     * The passed function however is run after the potential entity's spawn
     * randomization and hence already allows access to the values of the mob,
     * whether or not those were randomized, such as attributes or the entity
     * equipment.
     *
     * @param location      the location at which the entity will be spawned.
     * @param clazz         the class of the {@link LivingEntity} that is to be spawned.
     * @param <T>           the generic type of the entity that is being created.
     * @param spawnReason   the reason provided during the {@link CreatureSpawnEvent} call.
     * @param randomizeData whether or not the entity's data should be randomised
     *                      before spawning. By default entities are randomised
     *                      before spawning in regards to their equipment, age,
     *                      attributes, etc.
     *                      An example of this randomization would be the color of
     *                      a sheep, random enchantments on the equipment of mobs
     *                      or even a zombie becoming a chicken jockey.
     *                      If set to false, the entity will not be randomised
     *                      before spawning, meaning all their data will remain
     *                      in their default state and not further modifications
     *                      to the entity will be made.
     *                      Notably only entities that extend the
     *                      {@link org.bukkit.entity.Mob} interface provide
     *                      randomization logic for their spawn.
     *                      This parameter is hence useless for any other type
     *                      of entity.
     * @param function      the function to be run before the entity is spawned.
     * @return the spawned entity instance.
     * @throws IllegalArgumentException if either the world or clazz parameter are null.
     */
    @NotNull
    public <T extends LivingEntity> T spawn(@NotNull Location location, @NotNull Class<T> clazz, @NotNull CreatureSpawnEvent.SpawnReason spawnReason, boolean randomizeData, @Nullable Consumer<? super T> function) throws IllegalArgumentException;

    /**
     * Spawn a {@link FallingBlock} entity at the given {@link Location} of
     * the specified {@link MaterialData}. The MaterialData dictates what is falling.
     * When the FallingBlock hits the ground, it will place that block.
     * <p>
     * The Material must be a block type, check with {@link Material#isBlock()
     * data.getItemType().isBlock()}. The Material may not be air.
     *
     * @param location The {@link Location} to spawn the FallingBlock
     * @param data The block data
     * @return The spawned {@link FallingBlock} instance
     * @throws IllegalArgumentException if {@link Location} or {@link
     *     MaterialData} are null or {@link Material} of the {@link MaterialData} is not a block
     * @deprecated Use {@link #spawn(Location, Class, Consumer)} (or a variation thereof) in combination with {@link FallingBlock#setBlockData(BlockData)}
     */
    @NotNull
    @Deprecated(since = "1.20.2", forRemoval = true)
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull MaterialData data) throws IllegalArgumentException;

    /**
     * Spawn a {@link FallingBlock} entity at the given {@link Location} of
     * the specified {@link BlockData}. The BlockData dictates what is falling.
     * When the FallingBlock hits the ground, it will place that block.
     *
     * @param location The {@link Location} to spawn the FallingBlock
     * @param data The {@link BlockData} of the FallingBlock to spawn
     * @return The spawned {@link FallingBlock} instance
     * @throws IllegalArgumentException if {@link Location} or {@link
     *     BlockData} are null
     * @deprecated Use {@link #spawn(Location, Class, Consumer)} (or a variation thereof) in combination with {@link FallingBlock#setBlockData(BlockData)}
     */
    @NotNull
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.20.2") // Paper
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData data) throws IllegalArgumentException;

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
     * @deprecated Magic value. Use {@link #spawn(Location, Class, Consumer)} (or a variation thereof) in combination with {@link FallingBlock#setBlockData(BlockData)}
     */
    @Deprecated(since = "1.7.5", forRemoval = true)
    @NotNull
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte data) throws IllegalArgumentException;

    /**
     * Plays an effect to all players within a default radius around a given
     * location.
     *
     * @param location the {@link Location} around which players must be to
     *     hear the sound
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     */
    default void playEffect(@NotNull Location location, @NotNull Effect effect, int data) {
        this.playEffect(location, effect, data, 64);
    }

    /**
     * Plays an effect to all players within a given radius around a location.
     *
     * @param location the {@link Location} around which players must be to
     *     hear the effect
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     * @param radius the radius around the location
     */
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data, int radius);

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
    default <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data) {
        this.playEffect(location, effect, data, 64);
    }

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
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data, int radius);

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
    @NotNull
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
     * @deprecated biomes are now 3-dimensional
     */
    @NotNull
    @Deprecated(since = "1.15")
    default Biome getBiome(int x, int z) {
        return this.getBiome(x, 0, z);
    }

    /**
     * Sets the biome for the given block coordinates
     *
     * @param x X coordinate of the block
     * @param z Z coordinate of the block
     * @param bio new Biome type for this block
     * @deprecated biomes are now 3-dimensional
     */
    @Deprecated(since = "1.15")
    void setBiome(int x, int z, @NotNull Biome bio);

    /**
     * Gets the temperature for the given block coordinates.
     * <p>
     * It is safe to run this method when the block does not exist, it will
     * not create the block.
     *
     * @param x X coordinate of the block
     * @param z Z coordinate of the block
     * @return Temperature of the requested block
     * @deprecated biomes are now 3-dimensional
     */
    @Deprecated(since = "1.15")
    default double getTemperature(int x, int z) {
        return this.getTemperature(x, 0, z);
    }

    /**
     * Gets the temperature for the given block coordinates.
     * <p>
     * It is safe to run this method when the block does not exist, it will
     * not create the block.
     *
     * @param x X coordinate of the block
     * @param y Y coordinate of the block
     * @param z Z coordinate of the block
     * @return Temperature of the requested block
     */
    public double getTemperature(int x, int y, int z);

    /**
     * Gets the humidity for the given block coordinates.
     * <p>
     * It is safe to run this method when the block does not exist, it will
     * not create the block.
     *
     * @param x X coordinate of the block
     * @param z Z coordinate of the block
     * @return Humidity of the requested block
     * @deprecated biomes are now 3-dimensional
     */
    @Deprecated(since = "1.15")
    default double getHumidity(int x, int z) {
        return this.getHumidity(x, 0, z);
    }

    /**
     * Gets the humidity for the given block coordinates.
     * <p>
     * It is safe to run this method when the block does not exist, it will
     * not create the block.
     *
     * @param x X coordinate of the block
     * @param y Y coordinate of the block
     * @param z Z coordinate of the block
     * @return Humidity of the requested block
     */
    public double getHumidity(int x, int y, int z);

    /**
     * Gets the maximum height to which chorus fruits and nether portals can
     * bring players within this dimension.
     *
     * This excludes portals that were already built above the limit as they
     * still connect normally. May not be greater than {@link #getMaxHeight()}.
     *
     * @return maximum logical height for chorus fruits and nether portals
     */
    public int getLogicalHeight();

    /**
     * Gets if this world is natural.
     *
     * When false, compasses spin randomly, and using a bed to set the respawn
     * point or sleep, is disabled. When true, nether portals can spawn
     * zombified piglins.
     *
     * @return true if world is natural
     */
    public boolean isNatural();

    /**
     * Gets if beds work in this world.
     *
     * A non-working bed will blow up when trying to sleep. {@link #isNatural()}
     * defines if a bed can be used to set spawn point.
     *
     * @return true if beds work in this world
     */
    public boolean isBedWorks();

    /**
     * Gets if this world has skylight access.
     *
     * @return true if this world has skylight access
     */
    public boolean hasSkyLight();

    /**
     * Gets if this world has a ceiling.
     *
     * @return true if this world has a bedrock ceiling
     */
    public boolean hasCeiling();

    /**
     * Gets if this world allow to piglins to survive without shaking and
     * transforming to zombified piglins.
     *
     * @return true if piglins will not transform to zombified piglins
     */
    public boolean isPiglinSafe();

    /**
     * Gets if this world allows players to charge and use respawn anchors.
     *
     * @return true if players can charge and use respawn anchors
     */
    public boolean isRespawnAnchorWorks();

    /**
     * Gets if players with the bad omen effect in this world will trigger a
     * raid.
     *
     * @return true if raids will be triggered
     */
    public boolean hasRaids();

    /**
     * Gets if various water/lava mechanics will be triggered in this world, eg:
     * <br>
     * <ul>
     * <li>Water is evaporated</li>
     * <li>Sponges dry</li>
     * <li>Lava spreads faster and further</li>
     * </ul>
     *
     * @return true if this world has the above mechanics
     */
    public boolean isUltraWarm();

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
     * @deprecated No longer functional since 1.21.9, the vanilla server does not have the concept of spawn chunks anymore.
     */
    @Deprecated(since = "1.20.5", forRemoval = true)
    default boolean getKeepSpawnInMemory() {
        return false;
    }

    /**
     * Sets whether the world's spawn area should be kept loaded into memory
     * or not.
     *
     * @param keepLoaded if true then the world's spawn area will be kept
     *     loaded into memory.
     * @deprecated No longer functional since 1.21.9, the vanilla server does not have the concept of spawn chunks anymore.
     */
    @Deprecated(since = "1.20.5", forRemoval = true)
    default void setKeepSpawnInMemory(boolean keepLoaded) {

    }

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
     * @apiNote This does not disable saving entirely, the world will still be saved on shutdown.<br>
     * The intended use of this method is to disable the periodical autosave by the game.
     */
    public void setAutoSave(boolean value);

    /**
     * Sets the Difficulty of the world.
     *
     * @param difficulty the new difficulty you want to set the world to
     */
    public void setDifficulty(@NotNull Difficulty difficulty);

    /**
     * Gets the Difficulty of the world.
     *
     * @return The difficulty of the world.
     */
    @NotNull
    public Difficulty getDifficulty();

    /**
     * Returns the view distance used for this world.
     *
     * @return the view distance used for this world
     */
    int getViewDistance();

    /**
     * Returns the simulation distance used for this world.
     *
     * @return the simulation distance used for this world
     */
    int getSimulationDistance();

    /**
     * Gets the folder of this world on disk.
     *
     * @return The folder of this world.
     */
    @NotNull
    default File getWorldFolder() {
        return getWorldPath().toFile();
    }

    /**
     * Gets the path of this world on disk.
     *
     * @return The path of this world.
     */
    @NotNull
    Path getWorldPath();

    /**
     * Gets the type of this world.
     *
     * @return Type of this world.
     * @deprecated world type is only used to select the default word generation
     * settings and is not stored in Vanilla worlds, making it impossible for
     * this method to always return the correct value.
     */
    @Nullable
    @Deprecated(since = "1.16.1")
    public WorldType getWorldType();

    /**
     * Gets whether or not structures are being generated.
     *
     * @return True if structures are being generated.
     */
    public boolean canGenerateStructures();

    /**
     * Checks if the bonus chest is enabled.
     *
     * @return {@code true} if the bonus chest is enabled, {@code false} otherwise
     */
    boolean hasBonusChest();

    /**
     * Gets whether the world is hardcore or not.
     *
     * In a hardcore world the difficulty is locked to hard.
     *
     * @return hardcore status
     */
    public boolean isHardcore();

    /**
     * Sets whether the world is hardcore or not.
     *
     * In a hardcore world the difficulty is locked to hard.
     *
     * @param hardcore Whether the world is hardcore
     */
    public void setHardcore(boolean hardcore);

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
     * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default long getTicksPerAnimalSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.ANIMAL);
    }

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
     * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        this.setTicksPerSpawns(SpawnCategory.ANIMAL, ticksPerAnimalSpawns);
    }

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
     * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default long getTicksPerMonsterSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.MONSTER);
    }

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
     * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        this.setTicksPerSpawns(SpawnCategory.MONSTER, ticksPerMonsterSpawns);
    }

    /**
     * Gets the world's ticks per water mob spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn water mobs.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn water mobs in
     *     this world every tick.
     * <li>A value of 400 will mean the server will attempt to spawn water mobs
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, water mobs spawning will be disabled for this world.
     * <p>
     * Minecraft default: 1.
     *
     * @return The world's ticks per water mob spawns value
     * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default long getTicksPerWaterSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_ANIMAL);
    }

    /**
     * Sets the world's ticks per water mob spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn water mobs.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn water mobs in
     *     this world on every tick.
     * <li>A value of 400 will mean the server will attempt to spawn water mobs
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, water mobs spawning will be disabled for this world.
     * <p>
     * Minecraft default: 1.
     *
     * @param ticksPerWaterSpawns the ticks per water mob spawns value you
     *     want to set the world to
     * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
        this.setTicksPerSpawns(SpawnCategory.WATER_ANIMAL, ticksPerWaterSpawns);
    }

    /**
     * Gets the default ticks per water ambient mob spawns value.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn water ambient mobs
     *     every tick.
     * <li>A value of 400 will mean the server will attempt to spawn water ambient mobs
     *     every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b> If set to 0, ambient mobs spawning will be disabled.
     * <p>
     * Minecraft default: 1.
     *
     * @return the default ticks per water ambient mobs spawn value
     * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default long getTicksPerWaterAmbientSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_AMBIENT);
    }

    /**
     * Sets the world's ticks per water ambient mob spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn water ambient mobs.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn water ambient mobs in
     *     this world on every tick.
     * <li>A value of 400 will mean the server will attempt to spawn water ambient mobs
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, water ambient mobs spawning will be disabled for this world.
     * <p>
     * Minecraft default: 1.
     *
     * @param ticksPerWaterAmbientSpawns the ticks per water ambient mob spawns value you
     *     want to set the world to
     * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setTicksPerWaterAmbientSpawns(int ticksPerWaterAmbientSpawns) {
        this.setTicksPerSpawns(SpawnCategory.WATER_AMBIENT, ticksPerWaterAmbientSpawns);
    }

    /**
     * Gets the default ticks per water underground creature spawns value.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn water underground creature
     *     every tick.
     * <li>A value of 400 will mean the server will attempt to spawn water underground creature
     *     every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b> If set to 0, water underground creature spawning will be disabled.
     * <p>
     * Minecraft default: 1.
     *
     * @return the default ticks per water underground creature spawn value
     * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default long getTicksPerWaterUndergroundCreatureSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    /**
     * Sets the world's ticks per water underground creature spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn water underground creature.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn water underground creature in
     *     this world on every tick.
     * <li>A value of 400 will mean the server will attempt to spawn water underground creature
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, water underground creature spawning will be disabled for this world.
     * <p>
     * Minecraft default: 1.
     *
     * @param ticksPerWaterUndergroundCreatureSpawns the ticks per water underground creature spawns value you
     *     want to set the world to
     * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setTicksPerWaterUndergroundCreatureSpawns(int ticksPerWaterUndergroundCreatureSpawns) {
        this.setTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE, ticksPerWaterUndergroundCreatureSpawns);
    }

    /**
     * Gets the world's ticks per ambient mob spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn ambient mobs.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn ambient mobs in
     *     this world every tick.
     * <li>A value of 400 will mean the server will attempt to spawn ambient mobs
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, ambient mobs spawning will be disabled for this world.
     * <p>
     * Minecraft default: 1.
     *
     * @return the default ticks per ambient mobs spawn value
     * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default long getTicksPerAmbientSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.AMBIENT);
    }

    /**
     * Sets the world's ticks per ambient mob spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn ambient mobs.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn ambient mobs in
     *     this world on every tick.
     * <li>A value of 400 will mean the server will attempt to spawn ambient mobs
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, ambient mobs spawning will be disabled for this world.
     * <p>
     * Minecraft default: 1.
     *
     * @param ticksPerAmbientSpawns the ticks per ambient mob spawns value you
     *     want to set the world to
     * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
        this.setTicksPerSpawns(SpawnCategory.AMBIENT, ticksPerAmbientSpawns);
    }

    /**
     * Gets the world's ticks per {@link SpawnCategory} mob spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn {@link SpawnCategory} mobs.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn {@link SpawnCategory} mobs in
     *     this world every tick.
     * <li>A value of 400 will mean the server will attempt to spawn {@link SpawnCategory} mobs
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, {@link SpawnCategory} mobs spawning will be disabled for this world.
     * <p>
     * Minecraft default: 1.
     *
     * @param spawnCategory the category spawn
     * @return The world's ticks per {@link SpawnCategory} mob spawns value
     */
    public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory);

    /**
     * Sets the world's ticks per {@link SpawnCategory} mob spawns value
     * <p>
     * This value determines how many ticks there are between attempts to
     * spawn {@link SpawnCategory} mobs.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn {@link SpawnCategory} mobs in
     *     this world on every tick.
     * <li>A value of 400 will mean the server will attempt to spawn {@link SpawnCategory} mobs
     *     in this world every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b>
     * If set to 0, {@link SpawnCategory} mobs spawning will be disabled for this world.
     * <p>
     * Minecraft default: 1.
     *
     * @param spawnCategory the category spawn
     * @param ticksPerCategorySpawn the ticks per {@link SpawnCategory} mob spawns value you
     *     want to set the world to
     */
    public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int ticksPerCategorySpawn);

    /**
     * Gets limit for number of monsters that can spawn in a chunk in this
     * world
     *
     * @return The monster spawn limit
     * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default int getMonsterSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.MONSTER);
    }

    /**
     * Sets the limit for number of monsters that can spawn in a chunk in this
     * world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     *
     * @param limit the new mob limit
     * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setMonsterSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.MONSTER, limit);
    }

    /**
     * Gets the limit for number of animals that can spawn in a chunk in this
     * world
     *
     * @return The animal spawn limit
     * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default int getAnimalSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.ANIMAL);
    }

    /**
     * Sets the limit for number of animals that can spawn in a chunk in this
     * world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     *
     * @param limit the new mob limit
     * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default void setAnimalSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.ANIMAL, limit);
    }

    /**
     * Gets the limit for number of water animals that can spawn in a chunk in
     * this world
     *
     * @return The water animal spawn limit
     * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default int getWaterAnimalSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_ANIMAL);
    }

    /**
     * Sets the limit for number of water animals that can spawn in a chunk in
     * this world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     *
     * @param limit the new mob limit
     * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setWaterAnimalSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.WATER_ANIMAL, limit);
    }

    /**
     * Gets the limit for number of water underground creature that can spawn in a chunk in
     * this world
     *
     * @return The water underground creature spawn limit
     * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default int getWaterUndergroundCreatureSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    /**
     * Sets the limit for number of water underground creature that can spawn in a chunk in
     * this world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     *
     * @param limit the new mob limit
     * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setWaterUndergroundCreatureSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE, limit);
    }

    /**
     * Gets user-specified limit for number of water ambient mobs that can spawn
     * in a chunk.
     *
     * @return the water ambient spawn limit
     * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default int getWaterAmbientSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_AMBIENT);
    }

    /**
     * Sets the limit for number of water ambient mobs that can spawn in a chunk
     * in this world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     *
     * @param limit the new mob limit
     * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setWaterAmbientSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.WATER_AMBIENT, limit);
    }

    /**
     * Gets the limit for number of ambient mobs that can spawn in a chunk in
     * this world
     *
     * @return The ambient spawn limit
     * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
     */
    @Deprecated(since = "1.18.1")
    default int getAmbientSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.AMBIENT);
    }

    /**
     * Sets the limit for number of ambient mobs that can spawn in a chunk in
     * this world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     *
     * @param limit the new mob limit
     * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
     */
    @Deprecated(since = "1.18.1")
    default void setAmbientSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.AMBIENT, limit);
    }

    /**
     * Gets the limit for number of {@link SpawnCategory} entities that can spawn in a chunk in
     * this world
     *
     * @param spawnCategory the entity category
     * @return The ambient spawn limit
     */
    int getSpawnLimit(@NotNull SpawnCategory spawnCategory);

    /**
     * Sets the limit for number of {@link SpawnCategory} entities that can spawn in a chunk in
     * this world
     * <p>
     * <b>Note:</b> If set to a negative number the world will use the
     * server-wide spawn limit instead.
     *
     * @param spawnCategory the entity category
     * @param limit the new mob limit
     */
    void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int limit);

    /**
     * Play a note at the provided Location in the World. <br>
     * This <i>will</i> work with cake.
     * <p>
     * This method will fail silently when called with {@link Instrument#CUSTOM_HEAD}.
     *
     * @param loc The location to play the note
     * @param instrument The instrument
     * @param note The note
     */
    default void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
        this.playSound(loc, instrument.getSound(), SoundCategory.RECORDS, 3f, note.getPitch());
    }

    /**
     * Play a Sound at the provided Location in the World.
     * <p>
     * This function will fail silently if Location or Sound are null.
     *
     * @param location The location to play the sound
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    default void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        this.playSound(location, sound, SoundCategory.MASTER, volume, pitch);
    }

    /**
     * Play a Sound at the provided Location in the World.
     * <p>
     * This function will fail silently if Location or Sound are null. No
     * sound will be heard by the players if their clients do not have the
     * respective sound for the value passed.
     *
     * @param location The location to play the sound
     * @param sound The internal sound name to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    default void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
        this.playSound(location, sound, SoundCategory.MASTER, volume, pitch);
    }

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
    void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch);

    /**
     * Play a Sound at the provided Location in the World.
     * <p>
     * This function will fail silently if Location or Sound are null. No sound
     * will be heard by the players if their clients do not have the respective
     * sound for the value passed.
     *
     * @param location The location to play the sound
     * @param sound The internal sound name to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch);

    /**
     * Play a Sound at the provided Location in the World. For sounds with multiple
     * variations passing the same seed will always play the same variation.
     * <p>
     * This function will fail silently if Location or Sound are null.
     *
     * @param location The location to play the sound
     * @param sound The sound to play
     * @param category the category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param seed The seed for the sound
     */
    void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch, long seed);

    /**
     * Play a Sound at the provided Location in the World. For sounds with multiple
     * variations passing the same seed will always play the same variation.
     * <p>
     * This function will fail silently if Location or Sound are null. No sound will
     * be heard by the players if their clients do not have the respective sound for
     * the value passed.
     *
     * @param location The location to play the sound
     * @param sound The internal sound name to play
     * @param category the category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param seed The seed for the sound
     */
    void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch, long seed);

    /**
     * Play a Sound at the location of the provided entity in the World.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    default void playSound(@NotNull Entity entity, @NotNull Sound sound, float volume, float pitch) {
        this.playSound(entity, sound, SoundCategory.MASTER, volume, pitch);
    }

    /**
     * Play a Sound at the location of the provided entity in the World.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    default void playSound(@NotNull Entity entity, @NotNull String sound, float volume, float pitch) {
        this.playSound(entity, sound, SoundCategory.MASTER, volume, pitch);
    }

    /**
     * Play a Sound at the location of the provided entity in the World.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch);

    /**
     * Play a Sound at the location of the provided entity in the World.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    void playSound(@NotNull Entity entity, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch);

    /**
     * Play a Sound at the location of the provided entity in the World. For sounds
     * with multiple variations passing the same seed will always play the same
     * variation.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param seed The seed for the sound
     */
    void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch, long seed);

    /**
     * Play a Sound at the location of the provided entity in the World. For sounds
     * with multiple variations passing the same seed will always play the same
     * variation.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param seed The seed for the sound
     */
    void playSound(@NotNull Entity entity, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch, long seed);

    /**
     * Get an array containing the names of all the {@link GameRule}s.
     *
     * @return An array of {@link GameRule} names.
     */
    public @NotNull String @NotNull [] getGameRules();

    /**
     * Gets the current state of the specified rule
     * <p>
     * Will return null if rule passed is null
     *
     * @param rule Rule to look up value of
     * @return String value of rule
     * @deprecated use {@link #getGameRuleValue(GameRule)} instead
     */
    @Deprecated(since = "1.13")
    @Contract("null -> null; !null -> !null")
    @Nullable
    public String getGameRuleValue(@Nullable String rule);

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
    @Deprecated(since = "1.13")
    public boolean setGameRuleValue(@NotNull String rule, @NotNull String value);

    /**
     * Checks if string is a valid game rule
     *
     * @param rule Rule to check
     * @return True if rule exists
     */
    public boolean isGameRule(@NotNull String rule);

    /**
     * Get the current value for a given {@link GameRule}.
     *
     * @param rule the GameRule to check
     * @param <T> the GameRule's type
     * @return the current value
     */
    @Nullable
    public <T> T getGameRuleValue(@NotNull GameRule<T> rule);

    /**
     * Get the default value for a given {@link GameRule}. This value is not
     * guaranteed to match the current value.
     *
     * @param rule the rule to return a default value for
     * @param <T> the type of GameRule
     * @return the default value
     */
    @Nullable
    public <T> T getGameRuleDefault(@NotNull GameRule<T> rule);

    /**
     * Set the given {@link GameRule}'s new value.
     *
     * @param rule the GameRule to update
     * @param newValue the new value
     * @param <T> the value type of the GameRule
     * @return true if the value was successfully set
     */
    public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue);

    /**
     * Gets the world border for this world.
     *
     * @return The world border for this world.
     */
    @NotNull
    public WorldBorder getWorldBorder();

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     */
    default void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

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
    default void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
        this.spawnParticle(particle, x, y, z, count, null);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {
        this.spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

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
    default void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

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
    default void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
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
    default <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

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
    default void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

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
    default void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
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
    default <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
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
    default <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        this.spawnParticle(particle, null, null, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, true); // todo this is never called actually
    }

    // Paper start - Expand Particle API
    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param receivers List of players to receive the particles, or null for all in world
     * @param source Source of the particles to be used in visibility checks, or null if no player source
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
     * @param <T> Type
     */
    public default <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @NotNull Player source, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) { spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, true); }
    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param receivers List of players to receive the particles, or null for all in world
     * @param source Source of the particles to be used in visibility checks, or null if no player source
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
     * @param <T> Type
     * @param force allows the particle to be seen further away from the player
     *              and shows to players using any vanilla client particle settings
     */
    public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @Nullable Player source, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force);
    // Paper end


    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
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
    default <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
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
    default <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {
        this.spawnParticle(particle, null, null, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    /**
     * Find the closest nearby structure of a given {@link StructureType}.
     * Finding unexplored structures can, and will, block if the world is
     * looking in chunks that have not generated yet. This can lead to the world
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
     * @see #locateNearestStructure(Location, Structure, int, boolean)
     * @see #locateNearestStructure(Location, StructureType, int, boolean)
     * @deprecated Use
     * {@link #locateNearestStructure(Location, Structure, int, boolean)} or
     * {@link #locateNearestStructure(Location, StructureType, int, boolean)}
     * instead.
     */
    @Nullable
    @Deprecated(since = "1.19")
    public Location locateNearestStructure(@NotNull Location origin, @NotNull org.bukkit.StructureType structureType, int radius, boolean findUnexplored);

    /**
     * Find the closest nearby structure of a given {@link StructureType}.
     * Finding unexplored structures can, and will, block if the world is
     * looking in chunks that have not generated yet. This can lead to the world
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
     * <p>
     * The difference between searching for a {@link StructureType} and a
     * {@link Structure} is, that a {@link StructureType} can refer to multiple
     * {@link Structure Structures} while searching for a {@link Structure}
     * while only search for the given {@link Structure}.
     *
     * @param origin where to start looking for a structure
     * @param structureType the type of structure to find
     * @param radius the radius, in chunks, around which to search
     * @param findUnexplored true to only find unexplored structures
     * @return the closest {@link Location} and {@link Structure}, or null if no
     * structure of the specified type exists.
     * @see #locateNearestStructure(Location, Structure, int, boolean)
     */
    @Nullable
    StructureSearchResult locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType, int radius, boolean findUnexplored);

    /**
     * Find the closest nearby structure of a given {@link Structure}. Finding
     * unexplored structures can, and will, block if the world is looking in
     * chunks that have not generated yet. This can lead to the world
     * temporarily freezing while locating an unexplored structure.
     * <p>
     * The {@code radius} is not a rigid square radius. Each structure may alter
     * how many chunks to check for each iteration. Do not assume that only a
     * radius x radius chunk area will be checked. For example,
     * {@link Structure#MANSION} can potentially check up to 20,000 blocks away
     * (or more) regardless of the radius used.
     * <p>
     * This will <i>not</i> load or generate chunks. This can also lead to
     * instances where the server can hang if you are only looking for
     * unexplored structures. This is because it will keep looking further and
     * further out in order to find the structure.
     * <p>
     * The difference between searching for a {@link StructureType} and a
     * {@link Structure} is, that a {@link StructureType} can refer to multiple
     * {@link Structure Structures} while searching for a {@link Structure}
     * while only search for the given {@link Structure}.
     *
     * @param origin where to start looking for a structure
     * @param structure the structure to find
     * @param radius the radius, in chunks, around which to search
     * @param findUnexplored true to only find unexplored structures
     * @return the closest {@link Location} and {@link Structure}, or null if no
     * structure was found.
     * @see #locateNearestStructure(Location, StructureType, int, boolean)
     */
    @Nullable
    StructureSearchResult locateNearestStructure(@NotNull Location origin, @NotNull Structure structure, int radius, boolean findUnexplored);

    // Paper start
    /**
     * Locates the nearest biome based on an origin, biome type, and radius to search.
     * Step defaults to {@code 8}.
     *
     * @param origin Origin location
     * @param biome Biome to find
     * @param radius radius to search
     * @return Location of biome or null if not found in specified radius
     * @deprecated use {@link #locateNearestBiome(Location, int, Biome...)}
     */
    @Deprecated
    @Nullable
    default Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius) {
        return java.util.Optional.ofNullable(this.locateNearestBiome(origin, radius, 8, 8, biome)).map(BiomeSearchResult::getLocation).orElse(null);
    }

    /**
     * Locates the nearest biome based on an origin, biome type, and radius to search
     * and step
     *
     * @param origin Origin location
     * @param biome Biome to find
     * @param radius radius to search
     * @param step Search step 1 would mean checking every block, 8 would be every 8th block
     * @return Location of biome or null if not found in specified radius
     * @deprecated use {@link #locateNearestBiome(Location, int, int, int, Biome...)}
     */
    @Deprecated
    @Nullable
    default Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius, int step) {
        return java.util.Optional.ofNullable(this.locateNearestBiome(origin, radius, step, step, biome)).map(BiomeSearchResult::getLocation).orElse(null);
    }

    /**
     * Gets the coordinate scaling of this world.
     *
     * @return the coordinate scale
     */
    double getCoordinateScale();

    /**
     * Checks if this world has a fixed time
     *
     * @return whether this world has fixed time
     */
    boolean isFixedTime();

    /**
     * Gets the collection of materials that burn infinitely in this world.
     *
     * @return the materials that will forever stay lit by fire
     */
    @NotNull
    Collection<Material> getInfiniburn();

    /**
     * Posts a specified game event at a location
     *
     * @param sourceEntity optional source entity
     * @param gameEvent the game event to post
     * @param position the position in the world where to post the event to listeners
     */
    void sendGameEvent(@Nullable Entity sourceEntity, @NotNull GameEvent gameEvent, @NotNull Vector position);
    // Paper end

    // Spigot start
    @Deprecated(forRemoval = true) // Paper
    public class Spigot {

        /**
         * Strikes lightning at the given {@link Location} and possibly without sound
         *
         * @param loc The location to strike lightning
         * @param isSilent Whether this strike makes no sound
         * @return The lightning entity.
         * @deprecated sound is now client side and cannot be removed
         * @see World#strikeLightning(org.bukkit.Location)
         */
        @NotNull
        @Deprecated(since = "1.20.4")
        public LightningStrike strikeLightning(@NotNull Location loc, boolean isSilent) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Strikes lightning at the given {@link Location} without doing damage and possibly without sound
         *
         * @param loc The location to strike lightning
         * @param isSilent Whether this strike makes no sound
         * @return The lightning entity.
         * @deprecated sound is now client side and cannot be removed
         * @see World#strikeLightningEffect(org.bukkit.Location)
         */
        @NotNull
        @Deprecated(since = "1.20.4")
        public LightningStrike strikeLightningEffect(@NotNull Location loc, boolean isSilent) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * @deprecated Unsupported api
     */
    @NotNull
    @Deprecated // Paper
    Spigot spigot();
    // Spigot end

    /**
     * Find the closest nearby location with a biome matching the provided
     * {@link Biome}(s). Finding biomes can, and will, block if the world is looking
     * in chunks that have not generated yet. This can lead to the world temporarily
     * freezing while locating a biome.
     * <p>
     * <b>Note:</b> This will <i>not</i> reflect changes made to the world after
     * generation, this method only sees the biome at the time of world generation.
     * This will <i>not</i> load or generate chunks.
     * <p>
     * If multiple biomes are provided {@link BiomeSearchResult#getBiome()} will
     * indicate which one was located.
     * <p>
     * This method will use a horizontal interval of 32 and a vertical interval of
     * 64, equal to the /locate command.
     *
     * @param origin where to start looking for a biome
     * @param radius the radius, in blocks, around which to search
     * @param biomes the biomes to search for
     * @return a BiomeSearchResult containing the closest {@link Location} and
     *         {@link Biome}, or null if no biome was found.
     * @see #locateNearestBiome(Location, int, int, int, Biome...)
     */
    @Nullable
    default BiomeSearchResult locateNearestBiome(@NotNull Location origin, int radius, @NotNull Biome... biomes) {
        return this.locateNearestBiome(origin, radius, 32, 64, biomes);
    }

    /**
     * Find the closest nearby location with a biome matching the provided
     * {@link Biome}(s). Finding biomes can, and will, block if the world is looking
     * in chunks that have not generated yet. This can lead to the world temporarily
     * freezing while locating a biome.
     * <p>
     * <b>Note:</b> This will <i>not</i> reflect changes made to the world after
     * generation, this method only sees the biome at the time of world generation.
     * This will <i>not</i> load or generate chunks.
     * <p>
     * If multiple biomes are provided {@link BiomeSearchResult#getBiome()} will
     * indicate which one was located. Higher values for {@code horizontalInterval}
     * and {@code verticalInterval} will result in faster searches, but may lead to
     * small biomes being missed.
     *
     * @param origin             where to start looking for a biome
     * @param radius             the radius, in blocks, around which to search
     * @param horizontalInterval the horizontal distance between each check
     * @param verticalInterval   the vertical distance between each check
     * @param biomes             the biomes to search for
     * @return a BiomeSearchResult containing the closest {@link Location} and
     *         {@link Biome}, or null if no biome was found.
     * @see #locateNearestBiome(Location, int, Biome...)
     */
    @Nullable
    BiomeSearchResult locateNearestBiome(@NotNull Location origin, int radius, int horizontalInterval, int verticalInterval, @NotNull Biome... biomes);

    /**
     * Finds the nearest raid close to the given location.
     *
     * @param location the origin location
     * @param radius the radius
     * @return the closest {@link Raid}, or null if no raids were found
     */
    @Nullable
    public Raid locateNearestRaid(@NotNull Location location, int radius);

    // Paper start - more Raid API
    /**
     * Get a raid with the specific id from {@link Raid#getId}
     * from this world.
     *
     * @param id the id of the raid
     * @return the raid or null if none with that id
     */
    @Nullable Raid getRaid(int id);
    // Paper end - more Raid API

    /**
     * Gets all raids that are going on over this world.
     *
     * @return the list of all active raids
     */
    @NotNull
    public List<Raid> getRaids();

    /**
     * Get the {@link DragonBattle} associated with this world.
     *
     * If this world's environment is not {@link Environment#THE_END}, null will
     * be returned.
     * <p>
     * If an end world, a dragon battle instance will be returned regardless of
     * whether or not a dragon is present in the world or a fight sequence has
     * been activated. The dragon battle instance acts as a state holder.
     *
     * @return the dragon battle instance
     */
    @Nullable
    public DragonBattle getEnderDragonBattle();

    /**
     * Get all {@link FeatureFlag} enabled in this world.
     *
     * @return all enabled {@link FeatureFlag}
     */
    @NotNull
    public Set<FeatureFlag> getFeatureFlags();

    // Paper start - view distance api
    /**
     * Sets the view distance for this world.
     * @param viewDistance view distance in [2, 32]
     */
    void setViewDistance(int viewDistance);

    /**
     * Sets the simulation distance for this world.
     * @param simulationDistance simulation distance in [2, 32]
     */
    void setSimulationDistance(int simulationDistance);

    /**
     * Returns the no-tick view distance for this world.
     * <p>
     * No-tick view distance is the view distance where chunks will load, however the chunks and their entities will not
     * be set to tick.
     * </p>
     * @return The no-tick view distance for this world.
     * @deprecated Use {@link #getViewDistance()}
     */
    @Deprecated
    default int getNoTickViewDistance() {
        return this.getViewDistance();
    }

    /**
     * Sets the no-tick view distance for this world.
     * <p>
     * No-tick view distance is the view distance where chunks will load, however the chunks and their entities will not
     * be set to tick.
     * </p>
     * @param viewDistance view distance in [2, 32]
     * @deprecated Use {@link #setViewDistance(int)}
     */
    @Deprecated
    default void setNoTickViewDistance(int viewDistance) {
        this.setViewDistance(viewDistance);
    }

    /**
     * Gets the sending view distance for this world.
     * <p>
     * Sending view distance is the view distance where chunks will load in for players in this world.
     * </p>
     * @return The sending view distance for this world.
     */
    int getSendViewDistance();

    /**
     * Sets the sending view distance for this world.
     * <p>
     * Sending view distance is the view distance where chunks will load in for players in this world.
     * </p>
     * @param viewDistance view distance in [2, 32] or -1
     */
    void setSendViewDistance(int viewDistance);
    // Paper end - view distance api

    /**
     * Gets all generated structures that intersect the chunk at the given
     * coordinates. <br>
     * If no structures are present an empty collection will be returned.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return a collection of placed structures in the chunk at the given
     * coordinates
     */
    @NotNull
    public Collection<GeneratedStructure> getStructures(int x, int z);

    /**
     * Gets all generated structures of a given {@link Structure} that intersect
     * the chunk at the given coordinates. <br>
     * If no structures are present an empty collection will be returned.
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @param structure the structure to find
     * @return a collection of placed structures in the chunk at the given
     * coordinates
     */
    @NotNull
    public Collection<GeneratedStructure> getStructures(int x, int z, @NotNull Structure structure);

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
        THE_END(1),
        /**
         * Represents a custom dimension
         */
        CUSTOM(-999);

        private final int id;
        private static final Map<Integer, Environment> lookup = new HashMap<Integer, Environment>();

        private Environment(int id) {
            this.id = id;
        }

        /**
         * Gets the dimension ID of this environment
         *
         * @return dimension ID
         * @apiNote Internal Use Only
         */
        @org.jetbrains.annotations.ApiStatus.Internal // Paper
        public int getId() {
            return id;
        }

        /**
         * Get an environment by ID
         *
         * @param id The ID of the environment
         * @return The environment
         * @apiNote Internal Use Only
         */
        @org.jetbrains.annotations.ApiStatus.Internal // Paper
        @Nullable
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
