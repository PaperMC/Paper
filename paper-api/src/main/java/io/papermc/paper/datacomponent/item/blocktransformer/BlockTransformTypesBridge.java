package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.block.stateprovider.BlockStateProvider;
import java.util.Optional;
import java.util.ServiceLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
interface BlockTransformTypesBridge {

    Optional<BlockTransformTypesBridge> BRIDGE = ServiceLoader.load(BlockTransformTypesBridge.class, BlockTransformTypesBridge.class.getClassLoader()).findFirst();

    static BlockTransformTypesBridge bridge() {
        return BRIDGE.orElseThrow();
    }

    BlockTransformData.Builder blockTransformData(BlockStateProvider blockStateProvider);

    BlockTransformData.Builder blockTransformData(BlockPredicate predicate, BlockStateProvider blockStateProvider);
}
