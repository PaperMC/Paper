package io.papermc.paper.util.capture;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface BlockPlacementPredictor {

    Optional<BlockState> getLatestBlockAt(BlockPos pos);

    Optional<LoadedBlockState> getLatestBlockAtIfLoaded(BlockPos pos);

    Optional<BlockEntityPlacement> getLatestTileAt(BlockPos pos);


    record BlockEntityPlacement(boolean removed, BlockEntity blockEntity) {

        public static final Optional<BlockEntityPlacement> ABSENT = Optional.of(new BlockEntityPlacement(false, null));

        public BlockEntity res() {
            return this.removed ? null : this.blockEntity;
        }
    }

    record LoadedBlockState(boolean present, BlockState state) {

        public static final Optional<LoadedBlockState> UNLOADED = Optional.of(new LoadedBlockState(false, null));

        public BlockState res() {
            return this.present ? state : null;
        }
    }
}
