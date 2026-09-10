package io.papermc.paper.block.stateprovider;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A provider for {@link BlockData} that can be used to provide block states for various purposes.
 */
@ApiStatus.NonExtendable
public interface BlockStateProvider {

    /**
     * Creates a simple {@link BlockStateProvider} that always provides the given {@link BlockData}.
     *
     * @param blockData the block data to provide
     * @return a new {@link BlockStateProvider}
     */
    @Contract(value = "_ -> new", pure = true)
    static BlockStateProvider simple(final BlockData blockData) {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record SimpleProviderImpl(@Nullable BlockData blockData) implements BlockStateProvider {
            @Override
            public @Nullable BlockData simple() {
                return this.blockData;
            }
        }
        //</editor-fold>

        return new SimpleProviderImpl(blockData);
    }

    /**
     * Returns the simple {@link BlockData} if this provider is a simple provider, or null otherwise.
     *
     * @return the simple {@link BlockData} or null
     */
    @Nullable BlockData simple();

}
