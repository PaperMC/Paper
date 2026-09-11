package io.papermc.paper.block;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;

public final class PaperBlockPredicate {

    private PaperBlockPredicate() {
    }

    public static net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate toVanilla(final @Nullable BlockPredicate predicate) {
        if (predicate == null) {
            return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.alwaysTrue();
        }

        if (predicate instanceof final BlockPredicate.AnyOf anyOf) {
            return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.anyOf(
                anyOf.predicates().stream().map(PaperBlockPredicate::toVanilla).toList()
            );
        }
        if (predicate instanceof final BlockPredicate.AllOf allOf) {
            return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.allOf(
                allOf.predicates().stream().map(PaperBlockPredicate::toVanilla).toList()
            );
        }

        final List<net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate> predicates = new ArrayList<>();
        final Direction direction = predicate.direction() == null ? null : asArgument(CraftBlock.blockFaceToNotch(predicate.direction()), "direction");

        final RegistryKeySet<BlockType> blocksKeySet = predicate.blocks();
        if (blocksKeySet != null) {
            final List<Block> blocks = blocksKeySet.values().stream()
                .map(PaperRegistries::<Block, BlockType>toNms)
                .map(key -> BuiltInRegistries.BLOCK.getValue(key.identifier()))
                .toList();
            final Block[] array = blocks.toArray(Block[]::new);
            if (direction == null) {
                predicates.add(net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesBlocks(array));
            } else {
                predicates.add(net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesBlocks(direction, array));
            }
        }

        if (predicates.isEmpty()) {
            return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.alwaysTrue();
        }

        return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.allOf(predicates);
    }
}
