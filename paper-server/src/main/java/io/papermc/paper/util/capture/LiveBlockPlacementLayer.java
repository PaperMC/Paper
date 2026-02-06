package io.papermc.paper.util.capture;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public record LiveBlockPlacementLayer(ServerLevel level) implements BlockPlacementPredictor {
    @Override
    public Optional<BlockState> getLatestBlockAt(BlockPos pos) {
        return Optional.of(this.level.getBlockState(pos));
    }

    @Override
    public Optional<LoadedBlockState> getLatestBlockAtIfLoaded(BlockPos pos) {
        BlockState state = this.level.getBlockStateIfLoaded(pos);
        if (state == null) {
            return LoadedBlockState.UNLOADED;
        }

        return Optional.of(new LoadedBlockState(true, state));
    }

    @Override
    public Optional<BlockEntityPlacement> getLatestTileAt(BlockPos pos) {
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (blockEntity == null) {
            return BlockEntityPlacement.ABSENT;
        }

        return Optional.of(new BlockEntityPlacement(false, blockEntity));
    }
}
