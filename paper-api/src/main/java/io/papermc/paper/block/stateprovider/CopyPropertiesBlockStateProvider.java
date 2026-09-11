package io.papermc.paper.block.stateprovider;

import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface CopyPropertiesBlockStateProvider extends BlockStateProvider {

    /**
     * Target block type that receives the copied properties.
     *
     * @return target block type
     */
    BlockType blockType();
}
