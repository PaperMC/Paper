package io.papermc.paper.world.chunk;

import org.bukkit.World;

/**
 * The generation state of a chunk, as reported by {@link World#getChunkGenerationState(int, int)}
 * and {@link World#getChunkGenerationStateAsync(int, int)}.
 */
public enum ChunkGenerationState {
    /**
     * The chunk is not loaded and has no saved data.
     */
    NOT_GENERATED,
    /**
     * Generation of the chunk has started but has not completed. Loading the chunk
     * will complete generation.
     */
    PARTIAL,
    /**
     * The chunk has saved data that must be upgraded before it can be loaded, for
     * example because it was saved by an older version, or it has pending world
     * generation from a world height change. Loading the chunk will modify it.
     *
     * <p>
     * The chunk may be partially or fully generated; generation progress is not
     * determined for chunks in this state, as doing so would require upgrading
     * the chunk data.
     * </p>
     */
    OUTDATED,
    /**
     * The chunk is fully generated and up to date. Loading it will not change it.
     */
    GENERATED
}
