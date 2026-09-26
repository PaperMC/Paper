package io.papermc.paper.block;

import io.papermc.paper.annotation.MinecraftVersionDependent;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.List;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@MinecraftVersionDependent
@ApiStatus.NonExtendable
public interface BlockPredicate {

    @Contract(value = "_ -> new", pure = true)
    static MatchesBlocks matchesBlocks(final @Nullable RegistryKeySet<BlockType> blocks) {
        return predicate().blocks(blocks).build();
    }

    @Contract(value = "_, _ -> new", pure = true)
    static MatchesDirection matchesDirection(final BlockFace direction, final @Nullable RegistryKeySet<BlockType> blocks) {
        record MatchesDirectionPredicateImpl(@Nullable RegistryKeySet<BlockType> blocks, @Nullable BlockFace direction)
            implements MatchesDirection {
        }
        return new MatchesDirectionPredicateImpl(blocks, direction);
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

    static DirectionalBuilder predicate() {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record MatchesBlocksPredicateImpl(@Nullable RegistryKeySet<BlockType> blocks) implements MatchesBlocks {
        }

        record MatchesDirectionPredicateImpl(@Nullable RegistryKeySet<BlockType> blocks, @Nullable BlockFace direction)
            implements MatchesDirection {
        }

        class BuilderImpl implements DirectionalBuilder {

            private @Nullable RegistryKeySet<BlockType> blocks;
            private @Nullable BlockFace direction;

            @Override
            public Builder blocks(final @Nullable RegistryKeySet<BlockType> blocks) {
                this.blocks = blocks;
                return this;
            }

            @Override
            public DirectionalBuilder direction(final @Nullable BlockFace direction) {
                this.direction = direction;
                return this;
            }

            @Override
            public MatchesBlocks build() {
                return this.direction == null
                    ? new MatchesBlocksPredicateImpl(this.blocks)
                    : new MatchesDirectionPredicateImpl(this.blocks, this.direction);
            }
        }
        //</editor-fold>
        return new BuilderImpl();
    }

    @ApiStatus.NonExtendable
    interface MatchesBlocks extends BlockPredicate {

        @Nullable RegistryKeySet<BlockType> blocks();
    }

    @ApiStatus.NonExtendable
    interface MatchesDirection extends MatchesBlocks {

        @Override
        @Nullable RegistryKeySet<BlockType> blocks();

        @Nullable BlockFace direction();
    }

    @ApiStatus.NonExtendable
    interface DirectionalBuilder extends Builder {

        @Contract(value = "_ -> this", mutates = "this")
        DirectionalBuilder direction(@Nullable BlockFace direction);
    }

    @ApiStatus.NonExtendable
    interface Builder {

        @Contract(value = "_ -> this", mutates = "this")
        Builder blocks(@Nullable RegistryKeySet<BlockType> blocks);

        MatchesBlocks build();
    }

    @ApiStatus.NonExtendable
    interface AnyOf extends BlockPredicate {

        List<BlockPredicate> predicates();
    }

    @ApiStatus.NonExtendable
    interface AllOf extends BlockPredicate {

        List<BlockPredicate> predicates();
    }
}
