package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.block.stateprovider.BlockStateProvider;
import io.papermc.paper.datacomponent.BuildableDataComponent;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockFace;
import org.bukkit.loot.LootTable;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface BlockTransformData extends BuildableDataComponent<BlockTransformData, BlockTransformData.Builder> {

    @Contract(value = "_ -> new", pure = true)
    static BlockTransformData.Builder blockTransformData(final BlockStateProvider blockStateProvider) {
        return BlockTransformTypesBridge.bridge().blockTransformData(blockStateProvider);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static BlockTransformData.Builder blockTransformData(final BlockPredicate predicate, final BlockStateProvider blockStateProvider) {
        return BlockTransformTypesBridge.bridge().blockTransformData(predicate, blockStateProvider);
    }

    @Contract(pure = true)
    BlockStateProvider blockStateProvider();

    @Contract(pure = true)
    @Nullable Key sound();

    @Contract(pure = true)
    TransformParticle particle();

    @Contract(pure = true)
    @Unmodifiable List<BlockFace> disallowedFaces();

    @Nullable LootTable lootTable();

    @Contract(pure = true)
    DropStrategy dropStrategy();

    @Contract(pure = true)
    boolean updateFromNeighbors();

    @Contract(pure = true)
    TransformType transformType();

    @Contract(pure = true)
    boolean consumeOnUse();

    @Contract(pure = true)
    @NonNegative int itemDamagePerUse();

    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BlockTransformData> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(@Nullable Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder particle(TransformParticle particle);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disallowedFaces(List<BlockFace> disallowedFaces);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addDisallowedFace(BlockFace disallowedFace);

        @Contract(value = "_ -> this", mutates = "this")
        Builder lootTable(@Nullable LootTable lootTable);

        @Contract(value = "_ -> this", mutates = "this")
        Builder dropStrategy(DropStrategy dropStrategy);

        @Contract(value = "_ -> this", mutates = "this")
        Builder updateFromNeighbors(boolean updateFromNeighbors);

        @Contract(value = "_ -> this", mutates = "this")
        Builder transformType(TransformType transformType);

        @Contract(value = "_ -> this", mutates = "this")
        Builder consumeOnUse(boolean consumeOnUse);

        @Contract(value = "_ -> this", mutates = "this")
        Builder itemDamagePerUse(@NonNegative int itemDamagePerUse);
    }
}
