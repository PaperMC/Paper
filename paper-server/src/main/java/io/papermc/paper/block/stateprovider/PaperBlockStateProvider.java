package io.papermc.paper.block.stateprovider;

import io.papermc.paper.block.PaperBlockPredicate;
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
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockType;

import static io.papermc.paper.registry.data.util.Checks.asArgument;

public final class PaperBlockStateProvider {

    private PaperBlockStateProvider() {
    }

    public static net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toVanilla(final BlockStateProvider provider) {
        switch (provider) {
            case final SimpleBlockStateProvider simple -> {
                return new SimpleStateProvider(CraftBlockType.bukkitToMinecraftNew(simple.blockType()).defaultBlockState());
            }
            case final CopyPropertiesBlockStateProvider copyProperties -> {
                return new CopyPropertiesProvider(
                    CraftBlockType.bukkitToMinecraftNew(copyProperties.blockType())
                );
            }
            case final RandomBlockStateProvider randomBlock -> {
                final List<Holder<Block>> blocks = new ArrayList<>();
                for (final var key : randomBlock.blocks().values()) {
                    final Block block = BuiltInRegistries.BLOCK.getValue(PaperRegistries.toNms(key).identifier());
                    blocks.add(BuiltInRegistries.BLOCK.wrapAsHolder(block));
                }
                return new RandomBlockProvider(HolderSet.direct(blocks));
            }
            case final RotatedBlockStateProvider rotated -> {
                final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider source = toVanilla(rotated.stateProvider());
                final Optional<Direction> direction = Optional.ofNullable(rotated.direction())
                    .map(face -> asArgument(CraftBlock.blockFaceToNotch(face), "direction"));
                return new RotatedBlockProvider(Holder.direct(source), direction);
            }
            case final RuleBasedBlockStateProvider ruleBased -> {
                Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> holderFallback = null;
                if (ruleBased.fallback() != null) {
                    holderFallback = Holder.direct(toVanilla(ruleBased.fallback()));
                }
                final List<RuleBasedStateProvider.Rule> rules = ruleBased.rules().stream()
                    .map(rule -> new RuleBasedStateProvider.Rule(
                        PaperBlockPredicate.toVanilla(rule.ifTrue()),
                        Holder.direct(toVanilla(rule.thenProvide()))
                    ))
                    .toList();
                return new RuleBasedStateProvider(holderFallback, rules);
            }
            default -> {
                throw new UnsupportedOperationException("Unsupported block state provider type: " + provider.getClass().getSimpleName());
            }
        }
    }

    public static BlockStateProvider toApi(final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider provider) {
        return switch (provider) {
            case SimpleStateProvider(net.minecraft.world.level.block.state.BlockState state) ->
                BlockStateProvider.simple(CraftBlockType.minecraftToBukkitNew(state.getBlock()));
            case CopyPropertiesProvider(
                Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> source
            ) when source.value() instanceof SimpleStateProvider(
                net.minecraft.world.level.block.state.BlockState sourceState
            ) -> BlockStateProvider.copyPropertiesFrom(CraftBlockType.minecraftToBukkitNew(sourceState.getBlock()));
            case RandomBlockProvider(HolderSet<Block> blocks) ->
                BlockStateProvider.randomBlock(PaperRegistrySets.convertToApi(RegistryKey.BLOCK, blocks));
            case RotatedBlockProvider(
                Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> state,
                Optional<Direction> direction
            ) -> BlockStateProvider.rotated(
                toApi(state.value()),
                direction.map(CraftBlock::notchToBlockFace).orElse(null)
            );
            case RuleBasedStateProvider(
                Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> fallback,
                List<RuleBasedStateProvider.Rule> rules
            ) -> BlockStateProvider.ruleBased(
                fallback == null ? null : toApi(fallback.value()),
                rules.stream()
                    .map(rule -> BlockStateProvider.rule(
                        rule.ifTrue() == net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.alwaysTrue()
                            ? io.papermc.paper.block.BlockPredicate.predicate().build()
                            : PaperBlockPredicate.toApi(rule.ifTrue()),
                        toApi(rule.then().value())
                    ))
                    .toList()
            );
            default ->
                throw new UnsupportedOperationException("Unsupported block state provider type: " + provider.getClass().getSimpleName());
        };
    }
}
