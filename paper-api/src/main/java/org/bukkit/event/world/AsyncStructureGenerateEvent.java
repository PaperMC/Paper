package org.bukkit.event.world;

import java.util.Map;
import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.BlockTransformer;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EntityTransformer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

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
public interface AsyncStructureGenerateEvent extends WorldEventNew {

    /**
     * Gets the event cause.
     *
     * @return the event cause
     */
    Cause getCause();

    /**
     * Get the structure reference that is generated.
     *
     * @return the structure
     */
    Structure getStructure();

    /**
     * Get the bounding box of the structure.
     *
     * @return the bounding box
     */
    BoundingBox getBoundingBox();

    /**
     * Get the x coordinate of the origin chunk of the structure.
     *
     * @return the chunk x coordinate
     */
    int getChunkX();

    /**
     * Get the z coordinate of the origin chunk of the structure.
     *
     * @return the chunk z coordinate
     */
    int getChunkZ();

    /**
     * Gets a block transformer by key.
     *
     * @param key the key of the block transformer
     *
     * @return the block transformer or {@code null}
     */
    @Nullable BlockTransformer getBlockTransformer(NamespacedKey key);

    /**
     * Sets a block transformer to a key.
     *
     * @param key the key
     * @param transformer the block transformer
     */
    void setBlockTransformer(NamespacedKey key, BlockTransformer transformer);

    /**
     * Removes a block transformer.
     *
     * @param key the key of the block transformer
     */
    void removeBlockTransformer(NamespacedKey key);

    /**
     * Removes all block transformers.
     */
    void clearBlockTransformers();

    /**
     * Gets all block transformers in an unmodifiable map.
     *
     * @return the block transformers in a map
     */
    @Unmodifiable Map<NamespacedKey, BlockTransformer> getBlockTransformers();

    /**
     * Gets an entity transformer by key.
     *
     * @param key the key of the entity transformer
     *
     * @return the entity transformer or {@code null}
     */
    @Nullable EntityTransformer getEntityTransformer(NamespacedKey key);

    /**
     * Sets an entity transformer to a key.
     *
     * @param key the key
     * @param transformer the entity transformer
     */
    void setEntityTransformer(NamespacedKey key, EntityTransformer transformer);

    /**
     * Removes an entity transformer.
     *
     * @param key the key of the entity transformer
     */
    void removeEntityTransformer(NamespacedKey key);

    /**
     * Removes all entity transformers.
     */
    void clearEntityTransformers();

    /**
     * Gets all entity transformers in an unmodifiable map.
     *
     * @return the entity transformers in a map
     */
    @Unmodifiable Map<NamespacedKey, EntityTransformer> getEntityTransformers();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Cause {
        COMMAND,
        WORLD_GENERATION,
        CUSTOM
    }
}
