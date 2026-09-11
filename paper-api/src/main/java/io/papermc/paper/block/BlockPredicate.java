package io.papermc.paper.block;

import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.List;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.NonExtendable
public interface BlockPredicate {

    @Contract(value = "_ -> new", pure = true)
    static BlockPredicate matchesBlocks(final @Nullable RegistryKeySet<BlockType> blocks) {
        return predicate().blocks(blocks).build();
    }

    @Contract(value = "_, _ -> new", pure = true)
    static BlockPredicate matchesDirection(final BlockFace direction, final @Nullable RegistryKeySet<BlockType> blocks) {
        return predicate().direction(direction).blocks(blocks).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static AnyOf anyOf(final List<BlockPredicate> predicates) {
        return () -> List.copyOf(predicates);
    }

    @Contract(value = "_ -> new", pure = true)
    static AnyOf anyOf(final BlockPredicate... predicates) {
        return anyOf(List.of(predicates));
    }

    @Contract(value = "_ -> new", pure = true)
    static AllOf allOf(final List<BlockPredicate> predicates) {
        return () -> List.copyOf(predicates);
    }

    @Contract(value = "_ -> new", pure = true)
    static AllOf allOf(final BlockPredicate... predicates) {
        return allOf(List.of(predicates));
    }

    static Builder predicate() {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record BlockPredicateImpl(@Nullable RegistryKeySet<BlockType> blocks, @Nullable BlockFace direction)
            implements BlockPredicate {
            @Override
            public @Nullable RegistryKeySet<BlockType> blocks() {
                return this.blocks;
            }

            @Override
            public @Nullable BlockFace direction() {
                return this.direction;
            }
        }

        class BuilderImpl implements Builder {

            private @Nullable RegistryKeySet<BlockType> blocks;
            private @Nullable BlockFace direction;

            @Override
            public Builder blocks(final @Nullable RegistryKeySet<BlockType> blocks) {
                this.blocks = blocks;
                return this;
            }

            @Override
            public Builder direction(final @Nullable BlockFace direction) {
                this.direction = direction;
                return this;
            }

            @Override
            public BlockPredicate build() {
                return new BlockPredicateImpl(this.blocks, this.direction);
            }
        }
        //</editor-fold>
        return new BuilderImpl();
    }

    @Nullable RegistryKeySet<BlockType> blocks();

    default @Nullable BlockFace direction() {
        return null;
    }

    @ApiStatus.NonExtendable
    interface Builder {

        @Contract(value = "_ -> this", mutates = "this")
        Builder blocks(@Nullable RegistryKeySet<BlockType> blocks);

        @Contract(value = "_ -> this", mutates = "this")
        Builder direction(@Nullable BlockFace direction);

        BlockPredicate build();
    }

    @ApiStatus.NonExtendable
    interface AnyOf extends BlockPredicate {

        @Override
        default @Nullable RegistryKeySet<BlockType> blocks() {
            return null;
        }

        List<BlockPredicate> predicates();
    }

    @ApiStatus.NonExtendable
    interface AllOf extends BlockPredicate {

        @Override
        default @Nullable RegistryKeySet<BlockType> blocks() {
            return null;
        }

        List<BlockPredicate> predicates();
    }
}
