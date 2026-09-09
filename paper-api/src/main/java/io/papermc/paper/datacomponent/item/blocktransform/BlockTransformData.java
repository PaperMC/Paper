package io.papermc.paper.datacomponent.item.blocktransform;

import io.papermc.paper.block.BlockPredicate;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockFace;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

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
    List<BlockFace> disallowedFaces();

    //Optional<ResourceKey<LootTable>> loot();

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
