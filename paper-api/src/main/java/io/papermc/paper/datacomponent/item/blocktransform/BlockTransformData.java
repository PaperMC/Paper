package io.papermc.paper.datacomponent.item.blocktransform;

import io.papermc.paper.block.BlockPredicate;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockFace;
import org.bukkit.loot.LootTable;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.NonExtendable
public interface BlockTransformData {

    @Contract(pure = true)
    BlockPredicate predicate();

    @Contract(pure = true)
    Key sound();

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
}
