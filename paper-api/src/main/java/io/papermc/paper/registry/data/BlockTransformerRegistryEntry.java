package io.papermc.paper.registry.data;

import io.papermc.paper.datacomponent.item.blocktransformer.BlockTransformData;
import io.papermc.paper.datacomponent.item.blocktransformer.BlockTransformer;
import io.papermc.paper.registry.RegistryBuilder;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A data-centric version-specific registry entry for the {@link BlockTransformer} type.
 */
@ApiStatus.NonExtendable
public interface BlockTransformerRegistryEntry {

    /**
     * Gets the ordered transforms for this block transformer.
     *
     * @return the transforms
     */
    @Contract(pure = true)
    @Unmodifiable List<BlockTransformData> transforms();

    /**
     * A mutable builder for the {@link BlockTransformerRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #addTransform(BlockTransformData)} or {@link #addTransforms(List)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends BlockTransformerRegistryEntry, RegistryBuilder<BlockTransformer> {

        /**
         * Adds a transform to this block transformer.
         *
         * @param transform transform to add
         * @return this builder instance
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addTransform(BlockTransformData transform);

        /**
         * Adds transforms to this block transformer.
         *
         * @param transforms transforms to add
         * @return this builder instance
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addTransforms(List<BlockTransformData> transforms);
    }
}
