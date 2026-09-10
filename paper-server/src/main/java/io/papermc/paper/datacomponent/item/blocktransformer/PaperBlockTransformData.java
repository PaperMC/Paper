package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.registry.PaperRegistries;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.levelgen.blockpredicates.MatchingBlocksPredicate;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.loot.LootTable;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public record PaperBlockTransformData(
    net.minecraft.core.component.BlockTransformer.BlockTransformData impl
) implements BlockTransformData, Handleable<BlockTransformer.BlockTransformData> {

    @Override
    public net.minecraft.core.component.BlockTransformer.BlockTransformData getHandle() {
        return this.impl;
    }

    @Override
    public io.papermc.paper.block.stateprovider.BlockStateProvider blockStateProvider() {
        final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider provider = unwrapStateProvider(this.impl.blockStateProvider().value());
        if (provider instanceof SimpleStateProvider(net.minecraft.world.level.block.state.BlockState state)) {
            return io.papermc.paper.block.stateprovider.BlockStateProvider.simple(CraftBlockData.createData(state));
        }
        throw new UnsupportedOperationException("Unsupported block state provider type: " + provider.getClass().getSimpleName());
    }

    @Override
    public Key sound() {
        return PaperAdventure.asAdventure(this.impl.sound().value().location());
    }

    @Override
    public TransformParticle particle() {
        return TransformParticle.valueOf(this.impl.particle().name());
    }

    @Override
    public @Unmodifiable List<BlockFace> disallowedFaces() {
        return this.impl.disallowedFaces().stream().map(CraftBlock::notchToBlockFace).toList();
    }

    @Override
    public @Nullable LootTable lootTable() {
        return this.impl.loot().map(CraftLootTable::minecraftToBukkit).orElse(null);
    }

    @Override
    public DropStrategy dropStrategy() {
        return DropStrategy.valueOf(this.impl.dropStrategy().name());
    }

    @Override
    public boolean updateFromNeighbors() {
        return this.impl.updateFromNeighbors();
    }

    @Override
    public TransformType transformType() {
        return TransformType.valueOf(this.impl.transformType().name());
    }

    @Override
    public boolean consumeOnUse() {
        return this.impl.consumeOnUse();
    }

    @Override
    public @NonNegative int itemDamagePerUse() {
        return this.impl.itemDamagePerUse();
    }

    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this.blockStateProvider())
            .sound(this.sound())
            .particle(this.particle())
            .disallowedFaces(this.disallowedFaces())
            .dropStrategy(this.dropStrategy())
            .updateFromNeighbors(this.updateFromNeighbors())
            .transformType(this.transformType())
            .consumeOnUse(this.consumeOnUse())
            .itemDamagePerUse(this.itemDamagePerUse());
    }

    public static net.minecraft.core.component.BlockTransformer.BlockTransformData toVanilla(final BlockTransformData data) {
        if (data instanceof final PaperBlockTransformData paperBlockTransformData) {
            return paperBlockTransformData.getHandle();
        }
        return new net.minecraft.core.component.BlockTransformer.BlockTransformData(
            Holder.direct(toVanillaStateProvider(data.blockStateProvider())),
            PaperAdventure.resolveSound(data.sound()),
            net.minecraft.core.component.BlockTransformer.TransformParticle.valueOf(data.particle().name()),
            data.disallowedFaces().stream().map(PaperBlockTransformData::toVanillaDirection).toList(),
            java.util.Optional.empty(),
            net.minecraft.core.component.BlockTransformer.DropStrategy.valueOf(data.dropStrategy().name()),
            data.updateFromNeighbors(),
            net.minecraft.core.component.BlockTransformer.TransformType.valueOf(data.transformType().name()),
            data.consumeOnUse(),
            data.itemDamagePerUse()
        );
    }

    private static Direction toVanillaDirection(final BlockFace blockFace) {
        final Direction direction = CraftBlock.blockFaceToNotch(blockFace);
        if (direction == null) {
            throw new IllegalArgumentException("Unsupported block face: " + blockFace);
        }
        return direction;
    }

    private static net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toVanillaStateProvider(
        final io.papermc.paper.block.stateprovider.BlockStateProvider provider
    ) {
        final BlockData simple = provider.simple();
        if (simple == null) {
            throw new UnsupportedOperationException("Unsupported block state provider type");
        }
        if (!(simple instanceof final CraftBlockData craftBlockData)) {
            throw new IllegalArgumentException("Unsupported BlockData implementation: " + simple.getClass().getName());
        }
        return new SimpleStateProvider(craftBlockData.getState());
    }

    private static net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider unwrapStateProvider(
        final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider provider
    ) {
        if (provider instanceof RuleBasedStateProvider(
            Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> fallback,
            List<RuleBasedStateProvider.Rule> rules
        )
            && fallback == null
            && rules.size() == 1) {
            return unwrapStateProvider(rules.getFirst().then().value());
        }
        return provider;
    }

    static final class BuilderImpl implements Builder {

        private final io.papermc.paper.block.stateprovider.BlockStateProvider blockStateProvider;
        private @Nullable BlockPredicate predicate;
        private Key sound = Key.key("minecraft:empty");
        private TransformParticle particle = TransformParticle.NONE;
        private final List<BlockFace> disallowedFaces = new ArrayList<>();
        private Optional<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTable = Optional.empty();
        private DropStrategy dropStrategy = DropStrategy.FROM_MIDDLE;
        private boolean updateFromNeighbors = true;
        private TransformType transformType = TransformType.SINGLE_BLOCK;
        private boolean consumeOnUse = true;
        private int itemDamagePerUse = 1;

        BuilderImpl(final io.papermc.paper.block.stateprovider.BlockStateProvider blockStateProvider) {
            this.blockStateProvider = blockStateProvider;
        }

        BuilderImpl(final BlockPredicate blockPredicate, final io.papermc.paper.block.stateprovider.BlockStateProvider blockStateProvider) {
            this.predicate = blockPredicate;
            this.blockStateProvider = blockStateProvider;
        }

        @Override
        public Builder sound(final Key sound) {
            this.sound = sound;
            return this;
        }

        @Override
        public Builder particle(final TransformParticle particle) {
            this.particle = particle;
            return this;
        }

        @Override
        public Builder disallowedFaces(final List<BlockFace> disallowedFaces) {
            this.disallowedFaces.clear();
            this.disallowedFaces.addAll(disallowedFaces);
            return this;
        }

        @Override
        public Builder addDisallowedFace(final BlockFace disallowedFace) {
            this.disallowedFaces.add(disallowedFace);
            return this;
        }

        @Override
        public Builder lootTable(@Nullable final LootTable lootTable) {
            this.lootTable = Optional.ofNullable(lootTable).map(CraftLootTable::bukkitToMinecraft);
            return this;
        }

        @Override
        public Builder dropStrategy(final DropStrategy dropStrategy) {
            this.dropStrategy = dropStrategy;
            return this;
        }

        @Override
        public Builder updateFromNeighbors(final boolean updateFromNeighbors) {
            this.updateFromNeighbors = updateFromNeighbors;
            return this;
        }

        @Override
        public Builder transformType(final TransformType transformType) {
            this.transformType = transformType;
            return this;
        }

        @Override
        public Builder consumeOnUse(final boolean consumeOnUse) {
            this.consumeOnUse = consumeOnUse;
            return this;
        }

        @Override
        public Builder itemDamagePerUse(final @NonNegative int itemDamagePerUse) {
            this.itemDamagePerUse = requireNonNegative(itemDamagePerUse, "itemDamagePerUse");
            return this;
        }

        @Override
        public BlockTransformData build() {
            final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider targetProvider = toVanillaStateProvider(this.blockStateProvider);
            final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider provider = this.predicate == null
                ? targetProvider
                : RuleBasedStateProvider.builder().ifTrueThenProvide(toVanillaBlockPredicate(this.predicate), targetProvider).build();
            return new PaperBlockTransformData(new net.minecraft.core.component.BlockTransformer.BlockTransformData(
                Holder.direct(provider),
                PaperAdventure.resolveSound(this.sound),
                net.minecraft.core.component.BlockTransformer.TransformParticle.valueOf(this.particle.name()),
                this.disallowedFaces.stream().map(PaperBlockTransformData::toVanillaDirection).toList(),
                this.lootTable,
                net.minecraft.core.component.BlockTransformer.DropStrategy.valueOf(this.dropStrategy.name()),
                this.updateFromNeighbors,
                net.minecraft.core.component.BlockTransformer.TransformType.valueOf(this.transformType.name()),
                this.consumeOnUse,
                this.itemDamagePerUse
            ));
        }
    }

    private static net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate toVanillaBlockPredicate(final BlockPredicate predicate) {
        if (predicate.blocks() == null) {
            return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.alwaysTrue();
        }
        final List<Holder<Block>> blockHolders = predicate.blocks().values().stream()
            .map(PaperRegistries::<Block, org.bukkit.block.BlockType>toNms)
            .map(key -> {
                final Block block = BuiltInRegistries.BLOCK.getValue(key.identifier());
                if (block == null) {
                    throw new IllegalArgumentException("Unknown block key: " + key.identifier());
                }
                return BuiltInRegistries.BLOCK.wrapAsHolder(block);
            })
            .toList();
        return new MatchingBlocksPredicate(
            Vec3i.ZERO,
            HolderSet.direct(blockHolders)
        );
    }
}
