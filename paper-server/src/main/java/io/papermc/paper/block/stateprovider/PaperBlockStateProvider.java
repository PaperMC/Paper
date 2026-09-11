package io.papermc.paper.block.stateprovider;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomBlockProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RotatedBlockProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockType;

import static io.papermc.paper.registry.data.util.Checks.asArgument;

public final class PaperBlockStateProvider {

    private PaperBlockStateProvider() {
    }

    public static net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toVanilla(final BlockStateProvider provider) {
        if (provider instanceof final SimpleBlockStateProvider simple) {
            return new SimpleStateProvider(CraftBlockType.bukkitToMinecraftNew(simple.blockType()).defaultBlockState());
        }
        if (provider instanceof final CopyPropertiesBlockStateProvider copyProperties) {
            return new CopyPropertiesProvider(
                CraftBlockType.bukkitToMinecraftNew(copyProperties.blockType())
            );
        }
        if (provider instanceof final RandomBlockStateProvider randomBlock) {
            final List<Holder<Block>> blocks = new ArrayList<>();
            for (final var key : randomBlock.blocks().values()) {
                final Block block = BuiltInRegistries.BLOCK.getValue(PaperRegistries.toNms(key).identifier());
                blocks.add(BuiltInRegistries.BLOCK.wrapAsHolder(block));
            }
            return new RandomBlockProvider(HolderSet.direct(blocks));
        }
        if (provider instanceof final RotatedBlockStateProvider rotated) {
            final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider source = toVanilla(rotated.stateProvider());
            final Optional<Direction> direction = Optional.ofNullable(rotated.direction())
                .map(face -> asArgument(CraftBlock.blockFaceToNotch(face), "direction"));
            return new RotatedBlockProvider(Holder.direct(source), direction);
        }
        throw new UnsupportedOperationException("Unsupported block state provider type: " + provider.getClass().getSimpleName());
    }

    public static BlockStateProvider toApi(final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider provider) {
        if (provider instanceof SimpleStateProvider(net.minecraft.world.level.block.state.BlockState state)) {
            return BlockStateProvider.simple(CraftBlockType.minecraftToBukkitNew(state.getBlock()));
        }
        if (provider instanceof CopyPropertiesProvider(Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> source)
            && source.value() instanceof SimpleStateProvider(net.minecraft.world.level.block.state.BlockState sourceState)) {
            return BlockStateProvider.copyPropertiesFrom(CraftBlockType.minecraftToBukkitNew(sourceState.getBlock()));
        }
        if (provider instanceof RandomBlockProvider(HolderSet<net.minecraft.world.level.block.Block> blocks)) {
            return BlockStateProvider.randomBlock(PaperRegistrySets.convertToApi(RegistryKey.BLOCK, blocks));
        }
        if (provider instanceof RotatedBlockProvider(
            Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> state,
            Optional<Direction> direction
        )) {
            return BlockStateProvider.rotated(
                toApi(state.value()),
                direction.map(CraftBlock::notchToBlockFace).orElse(null)
            );
        }
        throw new UnsupportedOperationException("Unsupported block state provider type: " + provider.getClass().getSimpleName());
    }
}
