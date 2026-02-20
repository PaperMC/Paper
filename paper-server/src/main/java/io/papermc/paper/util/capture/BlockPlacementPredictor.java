package io.papermc.paper.util.capture;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface BlockPlacementPredictor {

    Optional<BlockState> getLatestBlockAt(BlockPos pos);

    Optional<LoadedBlockState> getLatestBlockAtIfLoaded(BlockPos pos);

    Optional<BlockEntityPlacement> getLatestBlockEntityAt(BlockPos pos);

    record BlockEntityPlacement(boolean removed, @Nullable BlockEntity blockEntity) {

        public static final Optional<BlockEntityPlacement> ABSENT = Optional.of(new BlockEntityPlacement(false, null));

        public @Nullable BlockEntity res() {
            return this.removed ? null : this.blockEntity;
        }
    }

    record LoadedBlockState(boolean present, @Nullable BlockState state) {

        public static final Optional<LoadedBlockState> UNLOADED = Optional.of(new LoadedBlockState(false, null));

        public @Nullable BlockState res() {
            return this.present ? this.state : null;
        }
    }
}
