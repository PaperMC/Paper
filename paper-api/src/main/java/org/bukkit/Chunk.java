package org.bukkit;

import java.util.Collection;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a chunk of blocks.
 *
 * If the chunk is not yet fully generated and data is requested from the chunk,
 * then the chunk will only be generated as far as it needs to provide the
 * requested data.
 */
public interface Chunk extends PersistentDataHolder {

    /**
     * Gets the X-coordinate of this chunk
     *
     * @return X-coordinate
     */
    int getX();

    /**
     * Gets the Z-coordinate of this chunk
     *
     * @return Z-coordinate
     */
    int getZ();

    // Paper start
    /**
     * @return The Chunks X and Z coordinates packed into a long
     */
    default long getChunkKey() {
        return getChunkKey(getX(), getZ());
    }

    /**
     * @param loc Location to get chunk key
     * @return Location's chunk coordinates packed into a long
     */
    static long getChunkKey(@NotNull Location loc) {
        return getChunkKey((int) Math.floor(loc.getX()) >> 4, (int) Math.floor(loc.getZ()) >> 4);
    }

    /**
     * @param x X Coordinate
     * @param z Z Coordinate
     * @return Chunk coordinates packed into a long
     */
    static long getChunkKey(int x, int z) {
        return (long) x & 0xffffffffL | ((long) z & 0xffffffffL) << 32;
    }
    // Paper end

    /**
     * Gets the world containing this chunk
     *
     * @return Parent World
     */
    @NotNull
    World getWorld();

    /**
     * Gets a block from this chunk
     *
     * @param x 0-15
     * @param y world minHeight (inclusive) - world maxHeight (exclusive)
     * @param z 0-15
     * @return the Block
     */
    @NotNull
    Block getBlock(int x, int y, int z);

    /**
     * Capture thread-safe read-only snapshot of chunk data
     *
     * @return ChunkSnapshot
     */
    @NotNull
    default ChunkSnapshot getChunkSnapshot() {
        return this.getChunkSnapshot(true, false, false);
    }

    /**
     * Capture thread-safe read-only snapshot of chunk data
     *
     * @param includeMaxBlockY - if true, snapshot includes per-coordinate
     *     maximum Y values
     * @param includeBiome - if true, snapshot includes per-coordinate biome
     *     type
     * @param includeBiomeTempRain - if true, snapshot includes per-coordinate
     *     raw biome temperature and rainfall
     * @return ChunkSnapshot
     */
    @NotNull
    default ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        return this.getChunkSnapshot(includeMaxBlockY, includeBiome, includeBiomeTempRain, true);
    }

    // Paper start - Add getChunkSnapshot includeLightData parameter
    /**
     * Capture thread-safe read-only snapshot of chunk data
     *
     * @param includeMaxBlockY if true, snapshot includes per-coordinate
     *     maximum Y values
     * @param includeBiome if true, snapshot includes per-coordinate biome
     *     type
     * @param includeBiomeTempRain if true, snapshot includes per-coordinate
     *     raw biome temperature and rainfall
     * @param includeLightData Whether to include per-coordinate light emitted by blocks and sky light data
     * @return ChunkSnapshot
     */
    @NotNull
    ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain, boolean includeLightData);
    // Paper end - Add getChunkSnapshot includeLightData parameter

    /**
     * Checks if entities in this chunk are loaded.
     *
     * @return True if entities are loaded.
     */
    boolean isEntitiesLoaded();

    /**
     * Get a list of all entities in the chunk.
     * This will force load any entities, which are not loaded.
     *
     * @return The entities.
     */
    @NotNull Entity @NotNull [] getEntities();

    /**
     * Get a list of all block entities in the chunk.
     *
     * @return The block entities.
     */
    @NotNull
    // Paper start
    default BlockState @NotNull [] getTileEntities() {
        return getTileEntities(true);
    }

    /**
     * Get a list of all block entities in the chunk.
     *
     * @param useSnapshot Take snapshots or direct references
     * @return The block entities.
     */
    @NotNull
    BlockState @NotNull [] getTileEntities(boolean useSnapshot);

    /**
     * Get a list of all block entities that match a given predicate in the chunk.
     *
     * @param blockPredicate The predicate of blocks to return block entities for
     * @param useSnapshot Take snapshots or direct references
     * @return The block entities.
     */
    @NotNull
    Collection<BlockState> getTileEntities(java.util.function.@NotNull Predicate<? super Block> blockPredicate, boolean useSnapshot);
    // Paper end

    /**
     * Checks if the chunk is fully generated.
     *
     * @return True if it is fully generated.
     */
    boolean isGenerated();

    /**
     * Checks if the chunk is loaded.
     *
     * @return True if it is loaded.
     */
    boolean isLoaded();

    /**
     * Loads the chunk.
     *
     * @param generate Whether or not to generate a chunk if it doesn't
     *     already exist
     * @return true if the chunk has loaded successfully, otherwise false
     */
    boolean load(boolean generate);

    /**
     * Loads the chunk.
     *
     * @return true if the chunk has loaded successfully, otherwise false
     */
    boolean load();

    /**
     * Unloads and optionally saves the Chunk
     *
     * @param save Controls whether the chunk is saved
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    boolean unload(boolean save);

    /**
     * Unloads and optionally saves the Chunk
     *
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    boolean unload();

    /**
     * Checks if this chunk can spawn slimes without being a swamp biome.
     *
     * @return true if slimes are able to spawn in this chunk
     */
    boolean isSlimeChunk();

    /**
     * Gets whether the chunk at the specified chunk coordinates is force
     * loaded.
     * <p>
     * A force loaded chunk will not be unloaded due to lack of player activity.
     *
     * @return force load status
     * @see World#isChunkForceLoaded(int, int)
     */
    boolean isForceLoaded();

    /**
     * Sets whether the chunk at the specified chunk coordinates is force
     * loaded.
     * <p>
     * A force loaded chunk will not be unloaded due to lack of player activity.
     *
     * @param forced force load status
     * @see World#setChunkForceLoaded(int, int, boolean)
     */
    void setForceLoaded(boolean forced);

    /**
     * Adds a plugin ticket for this chunk, loading this chunk if it is not
     * already loaded.
     * <p>
     * A plugin ticket will prevent a chunk from unloading until it is
     * explicitly removed. A plugin instance may only have one ticket per chunk,
     * but each chunk can have multiple plugin tickets.
     * </p>
     *
     * @param plugin Plugin which owns the ticket
     * @return {@code true} if a plugin ticket was added, {@code false} if the
     * ticket already exists for the plugin
     * @throws IllegalStateException If the specified plugin is not enabled
     * @see World#addPluginChunkTicket(int, int, Plugin)
     */
    boolean addPluginChunkTicket(@NotNull Plugin plugin);

    /**
     * Removes the specified plugin's ticket for this chunk
     * <p>
     * A plugin ticket will prevent a chunk from unloading until it is
     * explicitly removed. A plugin instance may only have one ticket per chunk,
     * but each chunk can have multiple plugin tickets.
     * </p>
     *
     * @param plugin Plugin which owns the ticket
     * @return {@code true} if the plugin ticket was removed, {@code false} if
     * there is no plugin ticket for the chunk
     * @see World#removePluginChunkTicket(int, int, Plugin)
     */
    boolean removePluginChunkTicket(@NotNull Plugin plugin);

    /**
     * Retrieves a collection specifying which plugins have tickets for this
     * chunk. This collection is not updated when plugin tickets are added or
     * removed to this chunk.
     * <p>
     * A plugin ticket will prevent a chunk from unloading until it is
     * explicitly removed. A plugin instance may only have one ticket per chunk,
     * but each chunk can have multiple plugin tickets.
     * </p>
     *
     * @return unmodifiable collection containing which plugins have tickets for
     * this chunk
     * @see World#getPluginChunkTickets(int, int)
     */
    @NotNull
    Collection<Plugin> getPluginChunkTickets();

    /**
     * Gets the amount of time in ticks that this chunk has been inhabited.
     *
     * Note that the time is incremented once per tick per player within mob
     * spawning distance of this chunk.
     *
     * @return inhabited time
     */
    long getInhabitedTime();

    /**
     * Sets the amount of time in ticks that this chunk has been inhabited.
     *
     * @param ticks new inhabited time
     */
    void setInhabitedTime(long ticks);

    /**
     * Tests if this chunk contains the specified block.
     *
     * @param block block to test
     * @return if the block is contained within
     */
    boolean contains(@NotNull BlockData block);

    /**
     * Tests if this chunk contains the specified biome.
     *
     * @param biome biome to test
     * @return if the biome is contained within
     */
    boolean contains(@NotNull Biome biome);

    /**
     * Gets the load level of this chunk, which determines what game logic is
     * processed.
     *
     * @return the load level
     */
    @NotNull
    LoadLevel getLoadLevel();

    /**
     * Gets all generated structures that intersect this chunk. <br>
     * If no structures are present an empty collection will be returned.
     *
     * @return a collection of placed structures in this chunk
     */
    @NotNull
    Collection<GeneratedStructure> getStructures();

    /**
     * Gets all generated structures of a given {@link Structure} that intersect
     * this chunk. <br>
     * If no structures are present an empty collection will be returned.
     *
     * @param structure the structure to find
     * @return a collection of placed structures in this chunk
     */
    @NotNull
    Collection<GeneratedStructure> getStructures(@NotNull Structure structure);

    /**
     * Get a list of all players who are can view the chunk from their client
     * <p>
     * This list will be empty if no players are viewing the chunk, or the chunk
     * is unloaded.
     *
     * @return collection of players who can see the chunk
     */
    @NotNull
    public Collection<Player> getPlayersSeeingChunk();

    /**
     * An enum to specify the load level of a chunk.
     */
    public enum LoadLevel {

        /**
         * No game logic is processed, world generation may still occur.
         */
        INACCESSIBLE,
        /**
         * Most game logic is not processed, including entities and redstone.
         */
        BORDER,
        /**
         * All game logic except entities is processed.
         */
        TICKING,
        /**
         * All game logic is processed.
         */
        ENTITY_TICKING,
        /**
         * This chunk is not loaded.
         */
        UNLOADED;
    }
}
