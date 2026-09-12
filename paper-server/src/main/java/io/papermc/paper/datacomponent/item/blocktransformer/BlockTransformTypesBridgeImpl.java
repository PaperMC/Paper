package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.block.stateprovider.BlockStateProvider;

public final class BlockTransformTypesBridgeImpl implements BlockTransformTypesBridge {

    @Override
    public BlockTransformData.Builder blockTransformData(final BlockStateProvider blockStateProvider) {
        return new PaperBlockTransformData.BuilderImpl(blockStateProvider);
    }

    @Override
    public BlockTransformData.Builder blockTransformData(final BlockPredicate predicate, final BlockStateProvider blockStateProvider) {
        return new PaperBlockTransformData.BuilderImpl(predicate, blockStateProvider);
    }
}
