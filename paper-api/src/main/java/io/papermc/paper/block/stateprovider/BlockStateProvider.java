package io.papermc.paper.block.stateprovider;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.NonExtendable
public interface BlockStateProvider {

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

    @Nullable BlockData simple();

}
