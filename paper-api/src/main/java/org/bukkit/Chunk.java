package org.bukkit;

import java.util.Collection;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a chunk of blocks
 */
public interface Chunk {

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
     * @param y 0-255
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
    ChunkSnapshot getChunkSnapshot();

    /**
     * Capture thread-safe read-only snapshot of chunk data
     *
     * @param includeMaxblocky - if true, snapshot includes per-coordinate
     *     maximum Y values
     * @param includeBiome - if true, snapshot includes per-coordinate biome
     *     type
     * @param includeBiomeTempRain - if true, snapshot includes per-coordinate
     *     raw biome temperature and rainfall
     * @return ChunkSnapshot
     */
    @NotNull
    ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome, boolean includeBiomeTempRain);

    /**
     * Get a list of all entities in the chunk.
     *
     * @return The entities.
     */
    @NotNull
    Entity[] getEntities();

    /**
     * Get a list of all tile entities in the chunk.
     *
     * @return The tile entities.
     */
    @NotNull
    BlockState[] getTileEntities();

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
     * Note that the time is incremented once per tick per player in the chunk.
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
}
