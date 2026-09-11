package io.papermc.paper.block.stateprovider;

import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface SimpleBlockStateProvider extends BlockStateProvider {

    /**
     * Target block type whose default state will be provided.
     *
     * @return target block type
     */
    BlockType blockType();
}
