package io.papermc.paper.block;

import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BlockPredicate {

    static Builder predicate() {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record BlockPredicateImpl(@Nullable RegistryKeySet<BlockType> blocks) implements BlockPredicate {
        }

        class BuilderImpl implements Builder {

            private @Nullable RegistryKeySet<BlockType> blocks;

            @Override
            public Builder blocks(final @Nullable RegistryKeySet<BlockType> blocks) {
                this.blocks = blocks;
                return this;
            }

            @Override
            public BlockPredicate build() {
                return new BlockPredicateImpl(this.blocks);
            }
        }
        //</editor-fold>
        return new BuilderImpl();
    }

    @Nullable RegistryKeySet<BlockType> blocks();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder {

        @Contract(value = "_ -> this", mutates = "this")
        Builder blocks(@Nullable RegistryKeySet<BlockType> blocks);

        BlockPredicate build();
    }
}
