package io.papermc.paper.util.capture;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public record LayeredBlockPlacementPredictor(
    BlockPlacementPredictor... predictors
) implements BlockPlacementPredictor {

    @Override
    public Optional<BlockState> getLatestBlockAt(BlockPos pos) {
        for (BlockPlacementPredictor predictor : this.predictors) {
            Optional<BlockState> state = predictor.getLatestBlockAt(pos);
            if (state.isPresent()) {
                return state;
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<LoadedBlockState> getLatestBlockAtIfLoaded(BlockPos pos) {
        for (BlockPlacementPredictor predictor : this.predictors) {
            Optional<LoadedBlockState> state = predictor.getLatestBlockAtIfLoaded(pos);
            if (state.isPresent()) {
                return state;
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<BlockEntityPlacement> getLatestBlockEntityAt(BlockPos pos) {
        for (BlockPlacementPredictor predictor : this.predictors) {
            Optional<BlockEntityPlacement> state = predictor.getLatestBlockEntityAt(pos);
            if (state.isPresent()) {
                return state;
            }
        }

        return Optional.empty();
    }
}
