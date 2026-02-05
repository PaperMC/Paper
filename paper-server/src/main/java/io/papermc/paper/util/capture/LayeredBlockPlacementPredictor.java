package io.papermc.paper.util.capture;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

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
    public Optional<@Nullable BlockEntityPlacement> getLatestTileAt(BlockPos pos) {
        for (BlockPlacementPredictor predictor : this.predictors) {
            Optional<BlockEntityPlacement> state = predictor.getLatestTileAt(pos);
            if (state.isPresent()) {
                return state;
            }
        }


        return Optional.empty();
    }
}
