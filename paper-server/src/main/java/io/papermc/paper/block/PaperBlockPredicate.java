package io.papermc.paper.block;

import io.papermc.paper.block.fluid.FluidPredicate;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.CraftFluid;
import org.jspecify.annotations.Nullable;

public final class PaperBlockPredicate {

    private PaperBlockPredicate() {
    }

    public static net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate toVanilla(final @Nullable BlockPredicate predicate) {
        switch (predicate) {
            case null -> {
                return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.alwaysTrue();
            }
            case final FluidPredicate.MatchesDirection fluidMatchesDirection -> {
                final List<Fluid> fluids = fluidMatchesDirection.fluids().values().stream()
                    .map(TypedKey::key)
                    .map(Registry.FLUID::get)
                    .filter(java.util.Objects::nonNull)
                    .map(CraftFluid::bukkitToMinecraft)
                    .toList();
                final Direction direction = Direction.valueOf(fluidMatchesDirection.direction().name());
                return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesFluids(
                    direction.getStep(),
                    fluids
                );
            }
            case final FluidPredicate.MatchesBlocks fluidMatchesBlocks -> {
                final List<Fluid> fluids = fluidMatchesBlocks.fluids().values().stream()
                    .map(TypedKey::key)
                    .map(Registry.FLUID::get)
                    .filter(java.util.Objects::nonNull)
                    .map(CraftFluid::bukkitToMinecraft)
                    .toList();
                return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesFluids(Vec3i.ZERO, fluids);
            }
            case final BlockPredicate.AnyOf anyOf -> {
                return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.anyOf(
                    anyOf.predicates().stream().map(PaperBlockPredicate::toVanilla).toList()
                );
            }
            case final BlockPredicate.AllOf allOf -> {
                return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.allOf(
                    allOf.predicates().stream().map(PaperBlockPredicate::toVanilla).toList()
                );
            }
            default -> {
                final List<net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate> predicates = new ArrayList<>();
                if (predicate instanceof final BlockPredicate.MatchesBlocks matchesBlocks) {
                    final RegistryKeySet<BlockType> blocksKeySet = matchesBlocks.blocks();
                    final List<Block> blocks = blocksKeySet.values().stream()
                        .map(PaperRegistries::<Block, BlockType>toNms)
                        .map(key -> BuiltInRegistries.BLOCK.getValue(key.identifier()))
                        .toList();
                    final Block[] array = blocks.toArray(Block[]::new);
                    predicates.add(net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesBlocks(array));
                }

                if (predicates.isEmpty()) {
                    return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.alwaysTrue();
                }

                return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.allOf(predicates);
            }
        }
    }

    public static BlockPredicate toApi(final net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate predicate) {
        if (predicate == net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.alwaysTrue()) {
            return BlockPredicate.predicate().build();
        }
        if (predicate instanceof final net.minecraft.world.level.levelgen.blockpredicates.AnyOfPredicate anyOf) {
            return BlockPredicate.anyOf(anyOf.predicates.stream().map(PaperBlockPredicate::toApi).toList());
        }
        if (predicate instanceof final net.minecraft.world.level.levelgen.blockpredicates.AllOfPredicate allOf) {
            return BlockPredicate.allOf(allOf.predicates.stream().map(PaperBlockPredicate::toApi).toList());
        }
        if (predicate instanceof final net.minecraft.world.level.levelgen.blockpredicates.MatchingBlocksPredicate matchingBlocks) {
            final RegistryKeySet<BlockType> blockSet = RegistrySet.keySetFromValues(
                RegistryKey.BLOCK,
                matchingBlocks.blocks.stream().map(holder -> CraftBlockType.minecraftToBukkitNew(holder.value())).toList()
            );
            final Vec3i offset = matchingBlocks.offset;
            final Direction direction = Direction.getNearest(offset, null);
            return direction == null
                ? BlockPredicate.matchesBlocks(blockSet)
                : BlockPredicate.matchesDirection(CraftBlock.notchToBlockFace(direction), blockSet);
        }
        if (predicate instanceof final net.minecraft.world.level.levelgen.blockpredicates.MatchingFluidsPredicate matchingFluids) {
            final java.util.List<TypedKey<org.bukkit.Fluid>> fluidKeys = matchingFluids.fluids.stream()
                .map(holder -> PaperRegistries.fromNms(holder.unwrapKey().orElseThrow()))
                .map(key -> TypedKey.create(RegistryKey.FLUID, key.key()))
                .toList();
            final var fluidSet = RegistrySet.keySet(RegistryKey.FLUID, fluidKeys);
            final Vec3i offset = matchingFluids.offset;
            final Direction direction = Direction.getNearest(offset, null);
            return direction == null
                ? io.papermc.paper.block.fluid.FluidPredicate.matchesFluids(fluidSet)
                : io.papermc.paper.block.fluid.FluidPredicate.matchesDirection(CraftBlock.notchToBlockFace(direction), fluidSet);
        }

        throw new UnsupportedOperationException("Unsupported block predicate type: " + predicate.getClass().getSimpleName());
    }
}
