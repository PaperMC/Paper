package io.papermc.paper.block.stateprovider;

import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

@ApiStatus.NonExtendable
public interface CopyPropertiesBlockStateProvider extends BlockStateProvider {

    /**
     * Target block type that receives the copied properties.
     *
     * @return target block type
     */
    @Contract(pure = true)
    BlockType blockType();
}
