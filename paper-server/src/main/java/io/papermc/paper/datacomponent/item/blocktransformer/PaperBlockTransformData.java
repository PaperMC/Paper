package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.block.stateprovider.BlockStateProvider;
import io.papermc.paper.block.stateprovider.PaperBlockStateProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.loot.LootTable;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public record PaperBlockTransformData(
    net.minecraft.core.component.BlockTransformer.BlockTransformData impl
) implements BlockTransformData, Handleable<BlockTransformer.BlockTransformData> {

    @Override
    public net.minecraft.core.component.BlockTransformer.BlockTransformData getHandle() {
        return this.impl;
    }

    @Override
    public BlockStateProvider blockStateProvider() {
        return PaperBlockStateProvider.toApi(this.impl.blockStateProvider().value());
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
            Holder.direct(PaperBlockStateProvider.toVanilla(data.blockStateProvider())),
            PaperAdventure.resolveSound(data.sound()),
            net.minecraft.core.component.BlockTransformer.TransformParticle.valueOf(data.particle().name()),
            data.disallowedFaces().stream().map(CraftBlock::blockFaceToNotch).filter(Objects::nonNull).toList(),
            java.util.Optional.empty(),
            net.minecraft.core.component.BlockTransformer.DropStrategy.valueOf(data.dropStrategy().name()),
            data.updateFromNeighbors(),
            net.minecraft.core.component.BlockTransformer.TransformType.valueOf(data.transformType().name()),
            data.consumeOnUse(),
            data.itemDamagePerUse()
        );
    }

    static final class BuilderImpl implements Builder {

        private static final Holder<SoundEvent> DEFAULT_SOUND = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY);

        private final Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> blockStateProvider;
        private final @Nullable BlockPredicate predicate;
        private Holder<SoundEvent> sound = DEFAULT_SOUND;
        private BlockTransformer.TransformParticle particle = BlockTransformer.TransformParticle.NONE;
        private final List<Direction> disallowedFaces = new ArrayList<>();
        private Optional<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTable = Optional.empty();
        private BlockTransformer.DropStrategy dropStrategy = BlockTransformer.DropStrategy.FROM_MIDDLE;
        private boolean updateFromNeighbors = true;
        private BlockTransformer.TransformType transformType = BlockTransformer.TransformType.SINGLE_BLOCK;
        private boolean consumeOnUse = true;
        private int itemDamagePerUse = 1;

        BuilderImpl(final BlockStateProvider blockStateProvider) {
            this(null, blockStateProvider);
        }

        BuilderImpl(final @Nullable BlockPredicate predicate, final BlockStateProvider blockStateProvider) {
            this.predicate = predicate;
            this.blockStateProvider = Holder.direct(PaperBlockStateProvider.toVanilla(blockStateProvider));
        }

        @Override
        public Builder sound(final @Nullable Key sound) {
            this.sound = (sound == null) ? DEFAULT_SOUND : PaperAdventure.resolveSound(sound);
            return this;
        }

        @Override
        public Builder particle(final TransformParticle particle) {
            this.particle = BlockTransformer.TransformParticle.valueOf(asArgument(particle, "particle").name());
            return this;
        }

        @Override
        public Builder disallowedFaces(final List<BlockFace> disallowedFaces) {
            this.disallowedFaces.clear();
            disallowedFaces.forEach(this::addDisallowedFace);
            return this;
        }

        @Override
        public Builder addDisallowedFace(final BlockFace disallowedFace) {
            this.disallowedFaces.add(asArgument(CraftBlock.blockFaceToNotch(disallowedFace), "disallowedFace"));
            return this;
        }

        @Override
        public Builder lootTable(@Nullable final LootTable lootTable) {
            this.lootTable = Optional.ofNullable(lootTable).map(CraftLootTable::bukkitToMinecraft);
            return this;
        }

        @Override
        public Builder dropStrategy(final DropStrategy dropStrategy) {
            this.dropStrategy = BlockTransformer.DropStrategy.valueOf(asArgument(dropStrategy, "dropStrategy").name());
            return this;
        }

        @Override
        public Builder updateFromNeighbors(final boolean updateFromNeighbors) {
            this.updateFromNeighbors = updateFromNeighbors;
            return this;
        }

        @Override
        public Builder transformType(final TransformType transformType) {
            this.transformType = BlockTransformer.TransformType.valueOf(asArgument(transformType, "transformType").name());
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
            return new PaperBlockTransformData(new net.minecraft.core.component.BlockTransformer.BlockTransformData(
                Holder.direct(this.predicate == null
                    ? this.blockStateProvider.value()
                    : new net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider(
                        null,
                        List.of(new net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider.Rule(
                            io.papermc.paper.block.PaperBlockPredicate.toVanilla(this.predicate),
                            this.blockStateProvider
                        ))
                    )),
                this.sound,
                this.particle,
                this.disallowedFaces,
                this.lootTable,
                this.dropStrategy,
                this.updateFromNeighbors,
                this.transformType,
                this.consumeOnUse,
                this.itemDamagePerUse
            ));
        }
    }
}
