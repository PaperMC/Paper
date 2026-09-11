package io.papermc.paper.block.stateprovider;

import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

@ApiStatus.NonExtendable
public interface BlockStateProvider {

    /**
     * Creates a simple provider that always returns the default state of the given block type.
     *
     * @param blockType block type to provide
     * @return a simple block state provider
     */
    @Contract(value = "_ -> new", pure = true)
    static SimpleBlockStateProvider simple(final BlockType blockType) {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record SimpleProviderImpl(BlockType blockType) implements SimpleBlockStateProvider {
        }
        //</editor-fold>
        return new SimpleProviderImpl(blockType);
    }

    /**
     * Creates a provider that copies properties from the current block into the given target block type.
     *
     * @param blockType target block type
     * @return a copy-properties block state provider
     */
    @Contract(value = "_ -> new", pure = true)
    static CopyPropertiesBlockStateProvider copyPropertiesFrom(final BlockType blockType) {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record CopyPropertiesProviderImpl(BlockType blockType) implements CopyPropertiesBlockStateProvider {
        }
        //</editor-fold>
        return new CopyPropertiesProviderImpl(blockType);
    }
}
