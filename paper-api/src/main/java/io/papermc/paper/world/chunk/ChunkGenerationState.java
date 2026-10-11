package io.papermc.paper.world.chunk;

import org.bukkit.World;

/**
 * The generation state of a chunk, as reported by {@link World#getChunkGenerationStateAsync(int, int)}.
 *
 * <p>
 * New values may be added in future versions; callers switching over this enum should
 * handle unknown values.
 * </p>
 */
public enum ChunkGenerationState {
    /**
     * The chunk has no data in memory or on disk.
     */
    NOT_GENERATED,
    /**
     * Generation of the chunk has started but has not completed. Loading the chunk
     * will complete generation.
     */
    PARTIAL,
    /**
     * The chunk is fully generated, but loading it will modify it, for example because
     * its data was saved by an older version, or it has pending world generation from a
     * world height change.
     */
    GENERATED_OUTDATED,
    /**
     * The chunk is fully generated and up to date. Loading it will not change it.
     */
    GENERATED
}
