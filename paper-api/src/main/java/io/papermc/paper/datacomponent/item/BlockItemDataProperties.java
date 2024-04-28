package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the {@link BlockData} properties of a block item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#BLOCK_DATA
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BlockItemDataProperties {

    @Contract(value = "-> new", pure = true)
    static BlockItemDataProperties.Builder blockItemStateProperties() {
        return ItemComponentTypesBridge.bridge().blockItemStateProperties();
    }

    /**
     * Creates a new {@link BlockData} instance for the given {@link BlockType}.
     *
     * @param blockType the block type
     * @return the block data
     */
    @Contract(pure = true)
    BlockData createBlockData(BlockType blockType);

    /**
     * Applies the properties to the given {@link BlockData}. Doesn't
     * mutate the parameter, but returns a new instance with the properties applied.
     *
     * @param blockData the block data to apply the properties to
     * @return the block data with the properties applied
     */
    @Contract(pure = true)
    BlockData applyTo(BlockData blockData);

    /**
     * Builder for {@link BlockItemDataProperties}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BlockItemDataProperties> {
        // building this requires BlockProperty API, so an empty builder for now (essentially read-only)
    }
}
