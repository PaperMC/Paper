package io.papermc.paper.block.stateprovider;

import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

@ApiStatus.NonExtendable
public interface SimpleBlockStateProvider extends BlockStateProvider {

    /**
     * Target block type whose default state will be provided.
     *
     * @return target block type
     */
    @Contract(pure = true)
    BlockType blockType();
}
