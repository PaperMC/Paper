package org.bukkit.event.world;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.BlockTransformer;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EntityTransformer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * This event will sometimes fire synchronously, depending on how it was
 * triggered.
 * <p>
 * The constructor provides a boolean to indicate if the event was fired
 * synchronously or asynchronously. When asynchronous, this event can be called
 * from any thread, sans the main thread, and has limited access to the API.
 * <p>
 * If a {@link Structure} is naturally placed in a chunk of the world, this
 * event will be asynchronous. If a player executes the '/place structure'
 * command, this event will be synchronous.
 * <br>
 * Allows to register transformers that can modify the blocks placed and
 * entities spawned by the structure.
 * <p>
 * Care should be taken to check {@link #isAsynchronous()} and treat the event
 * appropriately.
 * <p>
 */
@ApiStatus.Experimental
public class AsyncStructureGenerateEvent extends WorldEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Cause cause;

    private final Structure structure;
    private final BoundingBox boundingBox;

    private final int chunkX, chunkZ;

    private final Map<NamespacedKey, BlockTransformer> blockTransformers = new LinkedHashMap<>();
    private final Map<NamespacedKey, EntityTransformer> entityTransformers = new LinkedHashMap<>();

    @ApiStatus.Internal
    public AsyncStructureGenerateEvent(@NotNull World world, boolean async, @NotNull Cause cause, @NotNull Structure structure, @NotNull BoundingBox boundingBox, int chunkX, int chunkZ) {
        super(world, async);
        this.cause = cause;
        this.structure = structure;
        this.boundingBox = boundingBox;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    /**
     * Gets the event cause.
     *
     * @return the event cause
     */
    @NotNull
    public Cause getCause() {
        return this.cause;
    }

    /**
     * Get the structure reference that is generated.
     *
     * @return the structure
     */
    @NotNull
    public Structure getStructure() {
        return this.structure;
    }

    /**
     * Get the bounding box of the structure.
     *
     * @return the bounding box
     */
    @NotNull
    public BoundingBox getBoundingBox() {
        return this.boundingBox.clone();
    }

    /**
     * Get the x coordinate of the origin chunk of the structure.
     *
     * @return the chunk x coordinate
     */
    public int getChunkX() {
        return this.chunkX;
    }

    /**
     * Get the z coordinate of the origin chunk of the structure.
     *
     * @return the chunk z coordinate
     */
    public int getChunkZ() {
        return this.chunkZ;
    }

    /**
     * Gets a block transformer by key.
     *
     * @param key the key of the block transformer
     *
     * @return the block transformer or {@code null}
     */
    @Nullable
    public BlockTransformer getBlockTransformer(@NotNull NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        return this.blockTransformers.get(key);
    }

    /**
     * Sets a block transformer to a key.
     *
     * @param key the key
     * @param transformer the block transformer
     */
    public void setBlockTransformer(@NotNull NamespacedKey key, @NotNull BlockTransformer transformer) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        Preconditions.checkArgument(transformer != null, "BlockTransformer cannot be null");
        this.blockTransformers.put(key, transformer);
    }

    /**
     * Removes a block transformer.
     *
     * @param key the key of the block transformer
     */
    public void removeBlockTransformer(@NotNull NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        this.blockTransformers.remove(key);
    }

    /**
     * Removes all block transformers.
     */
    public void clearBlockTransformers() {
        this.blockTransformers.clear();
    }

    /**
     * Gets all block transformers in an unmodifiable map.
     *
     * @return the block transformers in a map
     */
    @NotNull
    public @Unmodifiable Map<NamespacedKey, BlockTransformer> getBlockTransformers() {
        return Collections.unmodifiableMap(this.blockTransformers);
    }

    /**
     * Gets an entity transformer by key.
     *
     * @param key the key of the entity transformer
     *
     * @return the entity transformer or {@code null}
     */
    @Nullable
    public EntityTransformer getEntityTransformer(@NotNull NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        return this.entityTransformers.get(key);
    }

    /**
     * Sets an entity transformer to a key.
     *
     * @param key the key
     * @param transformer the entity transformer
     */
    public void setEntityTransformer(@NotNull NamespacedKey key, @NotNull EntityTransformer transformer) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        Preconditions.checkArgument(transformer != null, "EntityTransformer cannot be null");
        this.entityTransformers.put(key, transformer);
    }

    /**
     * Removes an entity transformer.
     *
     * @param key the key of the entity transformer
     */
    public void removeEntityTransformer(@NotNull NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        this.entityTransformers.remove(key);
    }

    /**
     * Removes all entity transformers.
     */
    public void clearEntityTransformers() {
        this.entityTransformers.clear();
    }

    /**
     * Gets all entity transformers in an unmodifiable map.
     *
     * @return the entity transformers in a map
     */
    @NotNull
    public @Unmodifiable Map<NamespacedKey, EntityTransformer> getEntityTransformers() {
        return Collections.unmodifiableMap(this.entityTransformers);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum Cause {
        COMMAND,
        WORLD_GENERATION,
        CUSTOM
    }
}
